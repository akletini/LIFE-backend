package akletini.life.todo.validation.rules;

import akletini.life.shared.utils.DateUtils;
import akletini.life.shared.validation.Errors;
import akletini.life.shared.validation.ValidationRule;
import akletini.life.todo.repository.entity.Todo;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import static akletini.life.shared.validation.Errors.WRONG_DATE_FORMAT;
@Component
public class CorrectDateFormatRule implements ValidationRule<Todo> {
    @Override
    public Optional<String> validate(Todo todo) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.DATE_FORMAT);
        try {
            dateFormat.parse(todo.getDueAt());
        } catch (ParseException e) {
            return Optional.of(Errors.getError(WRONG_DATE_FORMAT));
        }
        return Optional.empty();
    }
}
