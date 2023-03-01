package akletini.life.todo.service.api;

import akletini.life.shared.EntityService;
import akletini.life.todo.repository.entity.Todo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface TodoService extends EntityService<Todo> {

    Page<Todo> getTodos(int page, int pageSize, Optional<String> sortBy,
                        Optional<List<String>> filterBy, Optional<List<String>> tags);

}
