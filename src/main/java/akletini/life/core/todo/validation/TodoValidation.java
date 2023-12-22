package akletini.life.core.todo.validation;

import akletini.life.core.shared.validation.EntityValidation;
import akletini.life.core.shared.validation.ValidationRule;
import akletini.life.core.task.validation.AssignedUserMatchRule;
import akletini.life.core.task.validation.CreatedDateUnchangedRule;
import akletini.life.core.todo.repository.entity.Todo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class TodoValidation implements EntityValidation<Todo> {
    private CreatedDateUnchangedRule<Todo> createdDateUnchangedRule;

    private AssignedUserMatchRule<Todo> assignedUserMatchRule;

    @Override
    public List<ValidationRule<Todo>> getValidationRules() {
        final List<ValidationRule<Todo>> validationRules = new ArrayList<>();
        validationRules.add(createdDateUnchangedRule);
        validationRules.add(assignedUserMatchRule);
        return validationRules;
    }
}
