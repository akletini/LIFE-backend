package akletini.life.todo.service.impl;

import akletini.life.shared.validation.Errors;
import akletini.life.shared.validation.ValidationRule;
import akletini.life.todo.exception.custom.TodoNotFoundException;
import akletini.life.todo.exception.custom.TodoStoreException;
import akletini.life.todo.repository.api.TodoRepository;
import akletini.life.todo.repository.entity.Todo;
import akletini.life.todo.service.api.GoogleTaskService;
import akletini.life.todo.service.api.TodoService;
import akletini.life.todo.validation.TodoValidation;
import akletini.life.user.repository.entity.AuthProvider;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static akletini.life.shared.validation.Errors.COULD_NOT_STORE;
import static akletini.life.shared.validation.Errors.ENTITY_NOT_FOUND;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    GoogleTaskService googleTaskService;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    TodoValidation validation;

    @Override
    public boolean validate(Todo todo) {
        List<ValidationRule<Todo>> validationRules = validation.getValidationRules();
        validationRules.forEach(rule -> {
            Optional<String> error = rule.validate(todo);
            if (error.isPresent()) {
                throw new TodoStoreException(error.get());
            }
        });
        return true;
    }

    @Override
    public Todo store(Todo todo) {
        if (todo != null) {
            validate(todo);
            if (AuthProvider.GOOGLE.equals(todo.getAssignedUser().getAuthProvider())) {
                googleTaskService.storeTask(todo);
            }
            return todoRepository.save(todo);
        }
        throw new TodoStoreException(Errors.getError(COULD_NOT_STORE, Todo.class.getSimpleName()));
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
        return todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(Errors.getError(ENTITY_NOT_FOUND, Todo.class.getSimpleName(), id)));
    }

    @Override
    public List<Todo> getAll() {
        Iterable<Todo> allTodos = todoRepository.findAll();
        return IterableUtils.toList(allTodos);
    }

}
