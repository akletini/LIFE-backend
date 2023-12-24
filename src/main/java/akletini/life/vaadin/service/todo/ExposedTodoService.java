package akletini.life.vaadin.service.todo;

import akletini.life.core.shared.validation.exception.InvalidDataException;
import akletini.life.core.todo.dto.TodoDto;
import akletini.life.core.todo.dto.mapper.TodoMapper;
import akletini.life.core.todo.repository.entity.Todo;
import akletini.life.core.todo.service.api.TodoService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExposedTodoService {
    private final TodoService todoService;
    private final TodoMapper todoMapper;

    public ExposedTodoService(TodoService todoService, TodoMapper todoMapper) {
        this.todoService = todoService;
        this.todoMapper = todoMapper;
    }

    public Page<TodoDto> getTodos(int page, int pageSize, Optional<String> sortBy,
                                  Optional<String> filterBy, Optional<List<String>> tags) {
        Page<Todo> todos = todoService.getTodos(page, pageSize, sortBy, filterBy, tags);
        return todos.map(todoMapper::todoToDto);
    }

    public TodoDto store(TodoDto todoDto) throws InvalidDataException {
        return todoMapper.todoToDto(todoService.store(todoMapper.dtoToTodo(todoDto)));
    }

    public void delete(TodoDto todoDto) {
        todoService.delete(todoMapper.dtoToTodo(todoDto));
    }
}
