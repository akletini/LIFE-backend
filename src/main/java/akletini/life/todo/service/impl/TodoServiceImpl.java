package akletini.life.todo.service.impl;

import akletini.life.todo.exception.custom.TodoNotFoundException;
import akletini.life.todo.exception.custom.TodoStoreException;
import akletini.life.todo.repository.api.TodoRepository;
import akletini.life.todo.repository.entity.Todo;
import akletini.life.todo.service.api.TodoService;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;
    @Override
    public Todo store(Todo todo) {
        if (todo != null) {
            return todoRepository.save(todo);
        }
        throw new TodoStoreException("Could not store Todo object");
    }

    @Override
    public Todo getById(Long id) {
        Optional<Todo> todoById = todoRepository.findById(id);
        if (todoById.isPresent()) {
            return todoById.get();
        }
        throw new TodoNotFoundException("Could not find Todo with ID " + id);
    }

    @Override
    public List<Todo> getAll() {
        Iterable<Todo> allTodos = todoRepository.findAll();
        return IterableUtils.toList(allTodos);
    }

    @Override
    public void delete(Long id) {
        todoRepository.deleteById(id);
    }
}
