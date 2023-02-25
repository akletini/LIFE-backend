package akletini.life.todo.controller;

import akletini.life.todo.dto.TodoDto;
import akletini.life.todo.dto.mapper.TodoMapper;
import akletini.life.todo.repository.entity.Todo;
import akletini.life.todo.service.api.TodoService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<TodoDto>> getAllTodos() {
        List<Todo> allTodos = todoService.getAll();
        List<TodoDto> allToDto = allTodos.stream().map(todoMapper::todoToDto).toList();
        return ResponseEntity.status(OK).body(allToDto);
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<TodoDto> getById(@PathVariable Long id) {
        Todo todoById = todoService.getById(id);
        return ResponseEntity.status(OK).body(todoMapper.todoToDto(todoById));
    }
}
