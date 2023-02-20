package akletini.life.todo.service.api;
import akletini.life.shared.EntityService;
import akletini.life.todo.repository.entity.Todo;

import java.util.List;
public interface TodoService extends EntityService<Todo> {

    Todo store(Todo todo);

    Todo getById(Long id);

    List<Todo> getAll();

    void delete(Todo todo);

}
