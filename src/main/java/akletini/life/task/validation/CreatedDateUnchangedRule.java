package akletini.life.task.validation;

import akletini.life.shared.utils.DateUtils;
import akletini.life.shared.validation.Errors;
import akletini.life.shared.validation.ValidationRule;
import akletini.life.task.entity.Task;
import akletini.life.task.entity.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static akletini.life.shared.utils.DateUtils.localDateTimeToDate;
import static akletini.life.shared.validation.Errors.TASK.CREATED_DATE_UNCHANGED;

@Component
public class CreatedDateUnchangedRule<T extends Task> implements ValidationRule<T> {

    @Autowired
    private TaskRepository<T> taskRepository;

    @Override
    public Optional<String> validate(Task validatable) {
        if (validatable.getId() != null) {
            Optional<T> byId = taskRepository.findById(validatable.getId());
            if (byId.isPresent()) {
                Task loadedTask = byId.get();
                if (!DateUtils.isSameInstant(localDateTimeToDate(loadedTask.getCreatedAt()),
                        localDateTimeToDate(validatable.getCreatedAt()))) {
                    return Optional.of(Errors.getError(CREATED_DATE_UNCHANGED));
                }
            }
        }
        return Optional.empty();
    }

}
