package akletini.life.core.todo.service.api;

import akletini.life.core.shared.EntityService;
import akletini.life.core.todo.repository.entity.Todo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public abstract class TodoService extends EntityService<Todo> {


    public abstract Page<Todo> getTodos(int page, int pageSize, Optional<String> sortBy,
                                        Optional<String> filterBy, Optional<List<String>> tags);

}
