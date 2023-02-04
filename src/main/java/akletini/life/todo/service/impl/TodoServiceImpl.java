package akletini.life.todo.service.impl;

import akletini.life.shared.utils.DateUtils;
import akletini.life.todo.exception.custom.TodoNotFoundException;
import akletini.life.todo.exception.custom.TodoStoreException;
import akletini.life.todo.repository.api.TodoRepository;
import akletini.life.todo.repository.entity.Todo;
import akletini.life.todo.service.api.GoogleTaskService;
import akletini.life.todo.service.api.TodoService;
import akletini.life.user.repository.entity.AuthProvider;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    GoogleTaskService googleTaskService;

    @Autowired
    private TodoRepository todoRepository;

    @Override
    public Todo store(Todo todo) {
        if (todo != null) {
            validateCreatedDateUnchanged(todo);
            validateDateFormats(todo);
            if (todo.getAssignedUser().getAuthProvider().equals(AuthProvider.GOOGLE)) {
                googleTaskService.storeTask(todo);
            }

            return todoRepository.save(todo);
        }
        throw new TodoStoreException("Could not store Todo object");
    }

    @Override
    public void delete(Todo todo) {
        if (todo.getAssignedUser().getAuthProvider().equals(AuthProvider.GOOGLE)) {
            googleTaskService.deleteTask(todo);
        }
        todoRepository.deleteById(todo.getId());
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

    private void validateCreatedDateUnchanged(Todo todo) {
        if (todo.getId() != null) {
            Optional<Todo> byId = todoRepository.findById(todo.getId());
            if (byId.isPresent()) {
                Todo loadedTodo = byId.get();
                if (!Objects.equals(loadedTodo.getCreatedAt(), todo.getCreatedAt())) {
                    throw new TodoStoreException("The created date cannot be modified");
                }
            }
        }
    }

    private void validateDateFormats(Todo todo) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DateUtils.DATE_TIME_FORMAT);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.DATE_FORMAT);
        try {
            dateFormat.parse(todo.getDueAt());
        } catch (ParseException e) {
            throw new TodoStoreException("The due date contains the wrong format");
        }
        try {
            dateTimeFormat.parse(todo.getCreatedAt());
        } catch (ParseException e) {
            throw new TodoStoreException("The creation date contains the wrong format");
        }
    }

}
