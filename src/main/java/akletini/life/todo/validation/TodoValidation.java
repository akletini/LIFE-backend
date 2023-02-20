package akletini.life.todo.validation;

import akletini.life.shared.validation.EntityValidation;
import akletini.life.shared.validation.ValidationRule;
import akletini.life.todo.repository.entity.Todo;
import akletini.life.todo.validation.rules.CorrectDateFormatRule;
import akletini.life.todo.validation.rules.CorrectDateTimeFormatRule;
import akletini.life.todo.validation.rules.CreatedDateUnchangedRule;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class TodoValidation implements EntityValidation<Todo> {

    private CorrectDateFormatRule correctDateFormatRule;
    private CorrectDateTimeFormatRule correctDateTimeFormatRule;
    private CreatedDateUnchangedRule createdDateUnchangedRule;

    @Override
    public List<ValidationRule<Todo>> getValidationRules() {
        final List<ValidationRule<Todo>> validationRules = new ArrayList<>();
        validationRules.add(correctDateFormatRule);
        validationRules.add(correctDateTimeFormatRule);
        validationRules.add(createdDateUnchangedRule);
        return validationRules;
    }
}
