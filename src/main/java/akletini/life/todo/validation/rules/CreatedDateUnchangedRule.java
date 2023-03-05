package akletini.life.todo.validation.rules;

import akletini.life.shared.utils.DateUtils;
import akletini.life.shared.validation.Errors;
import akletini.life.shared.validation.ValidationRule;
import akletini.life.todo.repository.api.TodoRepository;
import akletini.life.todo.repository.entity.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static akletini.life.shared.utils.DateUtils.localDateTimeToDate;
import static akletini.life.shared.validation.Errors.TODO.CREATED_DATE_UNCHANGED;

@Component("todoCreatedDateUnchangedRule")
public class CreatedDateUnchangedRule implements ValidationRule<Todo> {
    @Autowired
    private TodoRepository todoRepository;

    @Override
    public Optional<String> validate(Todo todo) {
        if (todo.getId() != null) {
            Optional<Todo> byId = todoRepository.findById(todo.getId());
            if (byId.isPresent()) {
                Todo loadedTodo = byId.get();
                if (!DateUtils.isSameInstant(localDateTimeToDate(loadedTodo.getCreatedAt()),
                        localDateTimeToDate(todo.getCreatedAt()))) {
                    return Optional.of(Errors.getError(CREATED_DATE_UNCHANGED));
                }
            }
        }
        return Optional.empty();
    }
}
