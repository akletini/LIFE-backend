package akletini.life.vaadin.service.todo;

import akletini.life.core.todo.dto.TodoDto;
import akletini.life.core.todo.dto.mapper.TodoMapper;
import akletini.life.core.todo.repository.entity.Todo;
import akletini.life.core.todo.service.api.TodoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        List<TodoDto> todoDtos =
                todos.get().map(todoMapper::todoToDto).collect(Collectors.toList());
        return new PageImpl<TodoDto>(todoDtos, todos.getPageable(), todos.getTotalElements());
    }

    public TodoDto store(TodoDto todoDto) {
        return todoMapper.todoToDto(todoService.store(todoMapper.dtoToTodo(todoDto)));
    }

    public void delete(TodoDto todoDto) {
        todoService.delete(todoMapper.dtoToTodo(todoDto));
    }
}
