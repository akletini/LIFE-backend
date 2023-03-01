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
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static akletini.life.shared.constants.FilterConstants.*;
import static akletini.life.shared.validation.Errors.COULD_NOT_STORE;
import static akletini.life.shared.validation.Errors.ENTITY_NOT_FOUND;

@Service
@Log4j2
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
                TodoStoreException todoStoreException = new TodoStoreException(error.get());
                log.error(todoStoreException);
                throw todoStoreException;
            }
        });
        return true;
    }

    @Override
    public Todo store(Todo todo) {
        if (todo != null) {
            validate(todo);
            if (AuthProvider.GOOGLE.equals(todo.getAssignedUser().getAuthProvider())) {
//                googleTaskService.storeTask(todo);
            }
            return todoRepository.save(todo);
        }
        TodoStoreException exception =
                new TodoStoreException(Errors.getError(COULD_NOT_STORE,
                        Todo.class.getSimpleName()));
        log.error(exception);
        throw exception;
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
                .orElseThrow(() -> {
                    TodoNotFoundException exception =
                            new TodoNotFoundException(Errors.getError(ENTITY_NOT_FOUND,
                                    Todo.class.getSimpleName(), id));
                    log.error(exception);
                    return exception;
                });
    }

    @Override
    public Page<Todo> getTodos(int page, int pageSize, Optional<String> sortBy,
                               Optional<List<String>> filterBy, Optional<List<String>> tags) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        if (sortBy.isPresent()) {
            pageRequest = PageRequest.of(page, pageSize, Sort.by(sortBy.get()));
        }
        if (filterBy.isPresent() && !filterBy.get().isEmpty()
                || tags.isPresent() && !tags.get().isEmpty()) {
            List<String> filters = filterBy.orElse(new ArrayList<>());
            return todoRepository.findFiltered(pageRequest,
                    filters.contains(OPEN) ? Todo.State.OPEN : null,
                    filters.contains(DUE) ? new Date() : null,
                    filters.contains(DONE) ? Todo.State.DONE : null,
                    tags.orElse(null));
        }
        return todoRepository.findAll(pageRequest);
    }
}
