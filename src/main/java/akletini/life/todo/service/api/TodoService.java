package akletini.life.todo.service.api;
import akletini.life.todo.repository.entity.Todo;

import java.util.List;
public interface TodoService {

    Todo store(Todo todo);

    Todo getById(Long id);

    List<Todo> getAll();

    void delete(Todo todo);

    void setAccessToken(String accessToken);

    String getAccessToken();
}
