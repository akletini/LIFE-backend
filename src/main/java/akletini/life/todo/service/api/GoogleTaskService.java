package akletini.life.todo.service.api;

import akletini.life.todo.repository.entity.Todo;

import java.util.List;
public interface GoogleTaskService {

    void storeTask(Todo todo, String accessToken);

    void deleteTask(Todo todo);

    List<Todo> getAllTasks();
}
