package akletini.life.todo.service.impl;

import akletini.life.todo.repository.entity.Todo;
import akletini.life.todo.service.api.GoogleTaskService;
import akletini.life.user.repository.entity.TokenContainer;
import akletini.life.user.repository.entity.User;
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
import org.springframework.core.env.Environment;
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

import static akletini.life.shared.utils.DateUtils.DATE_TIME_FORMAT;

@Service
public class GoogleTaskServiceImpl implements GoogleTaskService {

    @Autowired
    private Environment env;

    private static final String CREDENTIAL_PATH = "spring.security.oauth2.client.registration.google";

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private static final String APPLICATION_NAME = "LIFE app";

    private static final String TODO_TASK_ID = "c2pXT0NCREp0eFB6ME5HaA";


    @Override
    public void storeTask(Todo todo) {
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

            Tasks service = new Tasks.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(todo, HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            Task task = new Task();
            task.setTitle(todo.getTitle());
            task.setKind("tasks#task");
            DateTime dateTime = updateDateTime(todo);
            task.setDue(dateTime.toStringRfc3339());

            service.tasks().insert(TODO_TASK_ID, task).execute();
        } catch (IOException | GeneralSecurityException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void deleteTask(Todo todo) {

    }

    @Override
    public List<Todo> getAllTasks() {
        return null;
    }

    @SuppressWarnings("deprecation")
    private Credential getCredentials(Todo todo, HttpTransport transport) {
        String accessToken = checkTokenValidity(todo);
        todo.getAssignedUser().getTokenContainer().setAccessToken(accessToken);
        Credential credential = new GoogleCredential.Builder()
                .setClientSecrets(env.getProperty(CREDENTIAL_PATH + ".client-id"), env.getProperty(CREDENTIAL_PATH + ".client-secret"))
                .setTransport(transport)
                .setJsonFactory(JSON_FACTORY)
                .build();
        credential.setAccessToken(accessToken);
        return credential;
    }

    private String checkTokenValidity(Todo todo) {
        String accessToken;
        User user = todo.getAssignedUser();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        Date accessTokenCreation = null;
        try {
            accessTokenCreation = sdf.parse(user.getTokenContainer().getAccessTokenCreation());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        accessTokenCreation = DateUtils.addMinutes(accessTokenCreation, 45);
        Date currentDate = new Date();

        if (currentDate.compareTo(accessTokenCreation) >= 0) {
            // Access token expired
            accessToken = fetchNewAccessToken(user);
        } else {
            // Access token still valid
            accessToken = user.getTokenContainer().getAccessToken();
        }
        return accessToken;

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
            params.add(new BasicNameValuePair("client_id", env.getProperty(CREDENTIAL_PATH + ".client-id")));
            params.add(new BasicNameValuePair("client_secret", env.getProperty(CREDENTIAL_PATH + ".client-secret")));
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
        DateTime dateTime = null;
        try {
            Date date = new SimpleDateFormat("dd.MM.yyyy").parse(todo.getDueAt());
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.HOUR, 12);
            date = c.getTime();
            dateTime = new DateTime(date);
        } catch (ParseException e) {
            System.out.println("Invalid date format");
        }
        return dateTime != null ? dateTime : new DateTime(todo.getDueAt());
    }
}
