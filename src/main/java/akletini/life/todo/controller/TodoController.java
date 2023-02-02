package akletini.life.todo.controller;

import akletini.life.todo.dto.TodoDto;
import akletini.life.todo.dto.mapper.TodoMapper;
import akletini.life.todo.repository.entity.Todo;
import akletini.life.todo.service.api.TodoService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    private TodoMapper todoMapper = Mappers.getMapper(TodoMapper.class);

    @PostMapping(value = "/add")
    public ResponseEntity<TodoDto> addTodo(@RequestBody TodoDto todoDto) {
        Todo todo = todoMapper.dtoToTodo(todoDto);
        Todo storedTodo = todoService.store(todo);
        return ResponseEntity.status(HttpStatus.OK).body(todoMapper.todoToDto(storedTodo));
    }

    @PutMapping(value = "/update")
    public ResponseEntity<TodoDto> updateTodo(@RequestBody TodoDto todoDto) {
        Todo todo = todoMapper.dtoToTodo(todoDto);
        todoService.getById(todo.getId());
        Todo updatedtodo = todoService.store(todo);
        return ResponseEntity.status(HttpStatus.OK).body(todoMapper.todoToDto(updatedtodo));
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<TodoDto> deleteTodo(@PathVariable Long id) {
        todoService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<TodoDto>> getAllTodos() {
        List<Todo> allTodos = todoService.getAll();
        List<TodoDto> allToDto = allTodos.stream().map(todo -> todoMapper.todoToDto(todo)).toList();
        return ResponseEntity.status(HttpStatus.OK).body(allToDto);
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<TodoDto> getById(@PathVariable Long id) {
        Todo todoById = todoService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(todoMapper.todoToDto(todoById));
    }
}
