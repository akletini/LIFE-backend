package akletini.life.chore.validation;

import akletini.life.chore.repository.entity.Chore;
import akletini.life.chore.validation.rules.PositiveIntervalRule;
import akletini.life.chore.validation.rules.StartDateNotInPastRule;
import akletini.life.shared.validation.EntityValidation;
import akletini.life.shared.validation.ValidationRule;
import akletini.life.task.validation.AssignedUserMatchRule;
import akletini.life.task.validation.CreatedDateUnchangedRule;
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

    @Override
    public List<ValidationRule<Chore>> getValidationRules() {
        final List<ValidationRule<Chore>> validationRules = new ArrayList<>();
        validationRules.add(positiveIntervalRule);
        validationRules.add(startDateNotInPastRule);
        validationRules.add(createdDateUnchangedRule);
        validationRules.add(assignedUserMatchRule);
        return validationRules;
    }
}
