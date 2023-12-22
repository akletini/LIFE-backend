package akletini.life.core.todo.service.api;

import akletini.life.core.todo.repository.entity.Todo;

public interface GoogleTaskService {

    void storeTask(Todo todo);

    void deleteTask(Todo todo);
}
