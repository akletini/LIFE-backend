package akletini.life.core.chore.validation;

import akletini.life.core.chore.repository.entity.Chore;
import akletini.life.core.chore.validation.rules.PositiveIntervalRule;
import akletini.life.core.chore.validation.rules.StartDateNotInPastRule;
import akletini.life.core.shared.validation.EntityValidation;
import akletini.life.core.shared.validation.ValidationRule;
import akletini.life.core.shared.validation.rule.EntityNotNullRule;
import akletini.life.core.task.validation.AssignedUserMatchRule;
import akletini.life.core.task.validation.CreatedDateUnchangedRule;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class ChoreValidation implements EntityValidation<Chore> {

    private PositiveIntervalRule positiveIntervalRule;
    private StartDateNotInPastRule startDateNotInPastRule;

    private CreatedDateUnchangedRule<Chore> createdDateUnchangedRule;

    private AssignedUserMatchRule<Chore> assignedUserMatchRule;
    private EntityNotNullRule<Chore> entityNotNullRule;

    @Override
    public List<ValidationRule<Chore>> getValidationRules() {
        final List<ValidationRule<Chore>> validationRules = new ArrayList<>();
        validationRules.add(positiveIntervalRule);
        validationRules.add(startDateNotInPastRule);
        validationRules.add(createdDateUnchangedRule);
        validationRules.add(assignedUserMatchRule);
        validationRules.add(entityNotNullRule);
        return validationRules;
    }
}
