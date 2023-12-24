package akletini.life.core.todo.controller;

import akletini.life.core.shared.response.HttpResponse;
import akletini.life.core.shared.validation.exception.EntityNotFoundException;
import akletini.life.core.shared.validation.exception.InvalidDataException;
import akletini.life.core.todo.dto.TodoDto;
import akletini.life.core.todo.dto.mapper.TodoMapper;
import akletini.life.core.todo.repository.entity.Todo;
import akletini.life.core.todo.service.api.TodoService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/todos")
@AllArgsConstructor
public class TodoController {

    private TodoService todoService;

    private final TodoMapper todoMapper;

    @PostMapping(value = "/add")
    public ResponseEntity<TodoDto> addTodo(@RequestBody TodoDto todoDto) throws InvalidDataException {
        Todo todo = todoMapper.dtoToTodo(todoDto);
        Todo storedTodo = todoService.store(todo);
        return ResponseEntity.status(OK).body(todoMapper.todoToDto(storedTodo));
    }

    @PutMapping(value = "/update")
    public ResponseEntity<TodoDto> updateTodo(@RequestBody TodoDto todoDto) throws EntityNotFoundException, InvalidDataException {
        Todo todo = todoMapper.dtoToTodo(todoDto);
        todoService.getById(todo.getId());
        Todo updatedtodo = todoService.store(todo);
        return ResponseEntity.status(OK).body(todoMapper.todoToDto(updatedtodo));
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<TodoDto> deleteTodo(@RequestBody TodoDto todoDto) {
        Todo todo = todoMapper.dtoToTodo(todoDto);
        todoService.delete(todo);
        return ResponseEntity.status(OK).build();
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<TodoDto> getById(@PathVariable Long id) throws EntityNotFoundException {
        Todo todoById = todoService.getById(id);
        return ResponseEntity.status(OK).body(todoMapper.todoToDto(todoById));
    }

    @GetMapping(value = "/get")
    public ResponseEntity<HttpResponse> getChores(@RequestParam("page") int page,
                                                  @RequestParam("size") int size,
                                                  @RequestParam("sortBy") Optional<String> sortBy,
                                                  @RequestParam("filterBy") Optional<String> filterBy,
                                                  @RequestParam("tags") Optional<List<String>> tags) {
        Page<Todo> todos = todoService.getTodos(page, size, sortBy, filterBy, tags);
        Page<TodoDto> dtoPage = todos.map(todoMapper::todoToDto);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("page", dtoPage))
                        .message(OK.getReasonPhrase())
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }
}
