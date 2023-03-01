package akletini.life.todo.controller;

import akletini.life.shared.response.HttpResponse;
import akletini.life.todo.dto.TodoDto;
import akletini.life.todo.dto.mapper.TodoMapper;
import akletini.life.todo.repository.entity.Todo;
import akletini.life.todo.service.api.TodoService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TodoController {

    @Autowired
    private TodoService todoService;

    private final TodoMapper todoMapper = Mappers.getMapper(TodoMapper.class);

    @PostMapping(value = "/add")
    public ResponseEntity<TodoDto> addTodo(@RequestBody TodoDto todoDto) {
        Todo todo = todoMapper.dtoToTodo(todoDto);
        Todo storedTodo = todoService.store(todo);
        return ResponseEntity.status(OK).body(todoMapper.todoToDto(storedTodo));
    }

    @PutMapping(value = "/update")
    public ResponseEntity<TodoDto> updateTodo(@RequestBody TodoDto todoDto) {
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
    public ResponseEntity<TodoDto> getById(@PathVariable Long id) {
        Todo todoById = todoService.getById(id);
        return ResponseEntity.status(OK).body(todoMapper.todoToDto(todoById));
    }

    @GetMapping(value = "/get")
    public ResponseEntity<HttpResponse> getChores(@RequestParam("page") int page,
                                                  @RequestParam("size") int size,
                                                  @RequestParam("sortBy") Optional<String> sortBy,
                                                  @RequestParam ("filterBy") Optional<List<String>> filterBy,
                                                  @RequestParam("tags") Optional<List<Long>> tags) {
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
