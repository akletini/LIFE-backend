package akletini.life.todo.validation;

import akletini.life.shared.validation.EntityValidation;
import akletini.life.shared.validation.ValidationRule;
import akletini.life.task.validation.AssignedUserMatchRule;
import akletini.life.task.validation.CreatedDateUnchangedRule;
import akletini.life.todo.repository.entity.Todo;
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
