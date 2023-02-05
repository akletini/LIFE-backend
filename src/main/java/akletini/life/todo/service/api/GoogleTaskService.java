package akletini.life.todo.service.api;

import akletini.life.todo.repository.entity.Todo;
public interface GoogleTaskService {

    void storeTask(Todo todo);

    void deleteTask(Todo todo);
}
