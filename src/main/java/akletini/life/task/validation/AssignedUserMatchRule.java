package akletini.life.task.validation;

import akletini.life.shared.validation.Errors;
import akletini.life.shared.validation.ValidationRule;
import akletini.life.task.entity.Task;
import akletini.life.user.ContextUtils;
import akletini.life.user.repository.entity.User;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static akletini.life.shared.validation.Errors.TASK.ASSIGNED_USER_MATCH;

@Component
public class AssignedUserMatchRule<T extends Task> implements ValidationRule<T> {
    @Override
    public Optional<String> validate(T validatable) {
        User currentUser = ContextUtils.getCurrentUser();
        if (!Objects.equals(currentUser.getId(), validatable.getAssignedUser().getId())) {
            return Optional.of(Errors.getError(ASSIGNED_USER_MATCH));
        }
        return Optional.empty();
    }
}
