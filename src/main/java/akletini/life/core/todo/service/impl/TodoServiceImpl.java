package akletini.life.core.todo.service.impl;

import akletini.life.core.shared.validation.exception.EntityNotFoundException;
import akletini.life.core.shared.validation.exception.InvalidDataException;
import akletini.life.core.todo.repository.api.TodoRepository;
import akletini.life.core.todo.repository.entity.Todo;
import akletini.life.core.todo.service.api.GoogleTaskService;
import akletini.life.core.todo.service.api.TagService;
import akletini.life.core.todo.service.api.TodoService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static akletini.life.core.shared.constants.FilterConstants.*;

@Service
@AllArgsConstructor
@Log4j2
public class TodoServiceImpl extends TodoService {
    private GoogleTaskService googleTaskService;

    private TodoRepository todoRepository;

    private TagService tagService;


    @Override
    public Todo store(Todo todo) throws InvalidDataException {
        validate(todo);
//            if (AuthProvider.GOOGLE.equals(todo.getAssignedUser().getAuthProvider())) {
//                googleTaskService.storeTask(todo);
//            }
        return todoRepository.save(todo);

    }

    @Override
    public void delete(Todo todo) {
//        if (todo.getAssignedUser().getAuthProvider().equals(AuthProvider.GOOGLE)) {
//            googleTaskService.deleteTask(todo);
//        }
        todoRepository.deleteById(todo.getId());
    }

    @Override
    public Page<Todo> getTodos(int page, int pageSize, Optional<String> sortBy,
                               Optional<String> filterBy, Optional<List<String>> tags) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        if (sortBy.isPresent()) {
            pageRequest = PageRequest.of(page, pageSize, Sort.by(sortBy.get()));
        }
        if (filterBy.isPresent() && !filterBy.get().isEmpty()
                || tags.isPresent() && !tags.get().isEmpty()) {
            List<String> filters =
                    filterBy.map(Collections::singletonList).orElse(Collections.emptyList());
            List<Long> tagIds = new ArrayList<>();
            tags.ifPresent(strings -> strings.forEach(tagName -> {
                try {
                    tagIds.add(tagService.getByName(tagName).getId());
                } catch (EntityNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }));
            return todoRepository.findFiltered(pageRequest,
                    filters.contains(OPEN) ? Todo.State.OPEN : null,
                    filters.contains(DUE) ? LocalDate.now() : null,
                    filters.contains(DONE) ? Todo.State.DONE : null,
                    tagIds.isEmpty() ? null : tagIds);
        }
        return todoRepository.findAll(pageRequest);
    }
}
