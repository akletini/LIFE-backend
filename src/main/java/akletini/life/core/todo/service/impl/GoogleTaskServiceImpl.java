package akletini.life.core.todo.service.impl;

import akletini.life.core.shared.validation.exception.EntityNotFoundException;
import akletini.life.core.todo.repository.entity.Todo;
import akletini.life.core.todo.service.api.GoogleTaskService;
import akletini.life.core.todo.service.api.TodoService;
import akletini.life.core.user.repository.entity.TokenContainer;
import akletini.life.core.user.repository.entity.User;
import akletini.life.core.user.service.api.UserService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.Task;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Log4j2
public class GoogleTaskServiceImpl implements GoogleTaskService {

    @Autowired
    private Environment env;

    @Autowired
    private UserService userService;

    @Autowired
    @Lazy
    private TodoService todoService;

    private static final String CREDENTIAL_PATH = "spring.security.oauth2.client.registration" +
            ".google";

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private static final String APPLICATION_NAME = "LIFE app";

    private static final String TODO_TASK_ID = "c2pXT0NCREp0eFB6ME5HaA";

    @Override
    public void storeTask(Todo todo) {
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

            Tasks service = new Tasks.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(todo,
                    HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            if (todo.getId() != null) {
                log.info("Updating Google task with id: {}", todo.getId());
                updateTodo(todo, service);
                return;
            }
            log.info("Creating Google task {}", todo.getTitle());

            insertTask(todo, service);
        } catch (IOException | GeneralSecurityException | EntityNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    private void insertTask(Todo todo, Tasks service) throws IOException {
        Task task = new Task();
        task.setTitle(todo.getTitle());
        task.setKind("tasks#task");
        DateTime dateTime = updateDateTime(todo);
        task.setDue(dateTime.toStringRfc3339());

        service.tasks().insert(TODO_TASK_ID, task).execute();
    }

    @Async
    private void updateTodo(Todo todo, Tasks service) throws IOException, EntityNotFoundException {
        var tasks = service.tasks().list(TODO_TASK_ID).execute();
        List<Task> taskList = tasks.getItems();
        Task currentTask = new Task();
        Todo todoFromDB = todoService.getById(todo.getId());
        for (Task t : taskList) {
            if (t.getTitle().equals(todoFromDB.getTitle())) {
                currentTask = t;
                break;
            }
        }
        if (currentTask.isEmpty()) {
            log.error("Could not find todo with id: {}", todo.getId());
            return;
        }
        Task task = service.tasks().get(TODO_TASK_ID, currentTask.getId()).execute();
        task.setTitle(todo.getTitle());
        task.setStatus(Todo.State.OPEN.equals(todo.getState()) ? "needsAction" : "completed");
        task.setNotes(buildDescription(todo));
        DateTime dateTime = updateDateTime(todo);
        task.setDue(dateTime.toStringRfc3339());
        service.tasks().update(TODO_TASK_ID, currentTask.getId(), task).execute();
    }

    @Override
    @Async
    public void deleteTask(Todo todo) {
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

            Tasks service = new Tasks.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(todo,
                    HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            var tasks = service.tasks().list(TODO_TASK_ID).execute();
            List<Task> taskList = tasks.getItems();
            Task currentTask = new Task();
            for (Task t : taskList) {
                if (t.getTitle().equals(todo.getTitle())) {
                    currentTask = t;
                    break;
                }
            }
            if (currentTask.isEmpty()) {
                return;
            }

            service.tasks().delete(TODO_TASK_ID, currentTask.getId()).execute();
        } catch (IOException | GeneralSecurityException | EntityNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @SuppressWarnings("deprecation")
    private Credential getCredentials(Todo todo, HttpTransport transport) throws EntityNotFoundException {
        String accessToken;
        try {
            accessToken = checkTokenValidity(todo);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw e;
        }

        todo.getAssignedUser().getTokenContainer().setAccessToken(accessToken);
        Credential credential = new GoogleCredential.Builder()
                .setClientSecrets(env.getProperty(CREDENTIAL_PATH + ".client-id"),
                        env.getProperty(CREDENTIAL_PATH + ".client-secret"))
                .setTransport(transport)
                .setJsonFactory(JSON_FACTORY)
                .build();
        credential.setAccessToken(accessToken);
        return credential;
    }

    private String checkTokenValidity(Todo todo) throws EntityNotFoundException {
        String accessToken;
        User user = todo.getAssignedUser();
        user = userService.getById(user.getId());
        SimpleDateFormat sdf =
                new SimpleDateFormat(akletini.life.core.shared.utils.DateUtils.DATE_TIME_FORMAT);
        TokenContainer tokenContainer = user.getTokenContainer();
        Date accessTokenCreation;
        try {
            accessTokenCreation = sdf.parse(tokenContainer.getAccessTokenCreation());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        accessTokenCreation = DateUtils.addMinutes(accessTokenCreation, 55);
        Date currentDate = new Date();

        if (currentDate.compareTo(accessTokenCreation) >= 0) {
            // Access token expired
            accessToken = fetchNewAccessToken(user);
            tokenContainer.setAccessTokenCreation(sdf.format(new Date()));
            tokenContainer.setAccessToken(accessToken);
            todo.getAssignedUser().setTokenContainer(tokenContainer);
        } else {
            // Access token still valid
            accessToken = tokenContainer.getAccessToken();
        }
        return accessToken;
    }

    private String buildDescription(Todo todo) {
        String description = "";
        if (todo.getTag() != null) {
            description += "TAG: " + todo.getTag().getName() + "\n\n";
        }
        if (todo.getDescription() != null) {
            description += todo.getDescription();
        }
        return description;
    }

    private String fetchNewAccessToken(User user) {
        String accessToken;
        TokenContainer tokenContainer = user.getTokenContainer();
        if (StringUtils.isEmpty(tokenContainer.getRefreshToken())) {
            throw new RuntimeException("Cannot refresh access token, refresh token missing.");
        }
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost post = new HttpPost("https://www.googleapis.com/oauth2/v4/token");

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("client_id", env.getProperty(CREDENTIAL_PATH +
                    ".client-id")));
            params.add(new BasicNameValuePair("client_secret", env.getProperty(CREDENTIAL_PATH +
                    ".client-secret")));
            params.add(new BasicNameValuePair("refresh_token", tokenContainer.getRefreshToken()));
            params.add(new BasicNameValuePair("grant_type", "refresh_token"));
            post.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));

            HttpResponse response = httpClient.execute(post);
            HttpEntity responseEntity = response.getEntity();

            String json = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(json);
            accessToken = jsonObject.getString("access_token");
            if (StringUtils.isEmpty(accessToken)) {
                throw new RuntimeException("Access token response was invalid");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return accessToken;

    }

    private DateTime updateDateTime(Todo todo) {
        DateTime dateTime;
        Date date = akletini.life.core.shared.utils.DateUtils.localDateToDate(todo.getDueAt());
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR, 12);
        date = c.getTime();
        dateTime = new DateTime(date);
        return dateTime;
    }
}
