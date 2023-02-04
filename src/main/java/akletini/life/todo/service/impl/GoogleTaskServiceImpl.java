package akletini.life.todo.service.impl;

import akletini.life.todo.repository.entity.Todo;
import akletini.life.todo.service.api.GoogleTaskService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class GoogleTaskServiceImpl implements GoogleTaskService {

    @Autowired
    private Environment env;

    private static final String CREDENTIAL_PATH = "spring.security.oauth2.client.registration.google";

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private static final String APPLICATION_NAME = "LIFE app";

    private static final String TODO_TASK_ID = "c2pXT0NCREp0eFB6ME5HaA";


    @Override
    public void storeTask(Todo todo, String accessToken) {
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

            Tasks service = new Tasks.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(accessToken, HTTP_TRANSPORT))
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
    private Credential getCredentials(String accessToken, HttpTransport transport) throws IOException {
        Credential credential = new GoogleCredential.Builder()
                .setClientSecrets(env.getProperty(CREDENTIAL_PATH + ".client-id"), env.getProperty(CREDENTIAL_PATH + ".client-secret"))
                .setTransport(transport)
                .setJsonFactory(JSON_FACTORY)
                .build();
        credential.setAccessToken(accessToken);

        return credential;
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
