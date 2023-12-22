package akletini.life.core.task.validation;

import akletini.life.core.shared.validation.ValidationRule;
import akletini.life.core.task.entity.Task;
import akletini.life.core.user.ContextUtils;
import akletini.life.core.user.repository.entity.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AssignedUserMatchRule<T extends Task> implements ValidationRule<T> {
    @Override
    public Optional<String> validate(T validatable) {
        User currentUser = ContextUtils.getCurrentUser();
//        if (!Objects.equals(currentUser.getId(), validatable.getAssignedUser().getId())) {
//            return Optional.of(Errors.getError(ASSIGNED_USER_MATCH));
//        }
        return Optional.empty();
    }
}
