package akletini.life.core.task.validation;

import akletini.life.core.shared.utils.DateUtils;
import akletini.life.core.shared.validation.Errors;
import akletini.life.core.shared.validation.ValidationRule;
import akletini.life.core.shared.validation.exception.BusinessException;
import akletini.life.core.shared.validation.exception.InvalidDataException;
import akletini.life.core.task.entity.Task;
import akletini.life.core.task.entity.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CreatedDateUnchangedRule<T extends Task> implements ValidationRule<T> {

    @Autowired
    private TaskRepository<T> taskRepository;

    @Override
    public Optional<BusinessException> validate(Task validatable) {
        if (validatable.getId() != null) {
            Optional<T> byId = taskRepository.findById(validatable.getId());
            if (byId.isPresent()) {
                Task loadedTask = byId.get();
                if (!DateUtils.isSameInstant(DateUtils.localDateTimeToDate(loadedTask.getCreatedAt()),
                        DateUtils.localDateTimeToDate(validatable.getCreatedAt()))) {
                    return Optional.of(new InvalidDataException(Errors.getError(Errors.TASK.CREATED_DATE_UNCHANGED)));
                }
            }
        }
        return Optional.empty();
    }

}
