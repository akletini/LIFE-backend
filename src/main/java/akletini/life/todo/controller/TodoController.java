package akletini.life.todo.controller;

import akletini.life.todo.repository.entity.Todo;
import akletini.life.todo.service.api.TodoService;
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

    @PostMapping(value = "/add")
    public ResponseEntity<Todo> addTodo(@RequestBody Todo todo) {
        Todo storedTodo = todoService.store(todo);
        return ResponseEntity.status(HttpStatus.OK).body(storedTodo);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<Todo> updateTodo(@RequestBody Todo todo) {
        todoService.getById(todo.getId());
        Todo updatedtodo = todoService.store(todo);
        return ResponseEntity.status(HttpStatus.OK).body(updatedtodo);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<Todo> deleteTodo(@PathVariable Long id) {
        todoService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<Todo>> getAllTodos() {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.getAll());
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<Todo> getById(@PathVariable Long id) {
        Todo todoById = todoService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(todoById);
    }
}
