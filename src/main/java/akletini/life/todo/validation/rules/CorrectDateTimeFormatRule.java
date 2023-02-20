package akletini.life.todo.validation.rules;

import akletini.life.shared.utils.DateUtils;
import akletini.life.shared.validation.Errors;
import akletini.life.shared.validation.ValidationRule;
import akletini.life.todo.repository.entity.Todo;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;
@Component
public class CorrectDateTimeFormatRule implements ValidationRule<Todo> {
    @Override
    public Optional<String> validate(Todo todo) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DateUtils.DATE_TIME_FORMAT);
        try {
            dateTimeFormat.parse(todo.getCreatedAt());
        } catch (ParseException e) {
            return Optional.of(Errors.getError(Errors.WRONG_DATE_FORMAT, "created at"));
        }
        return Optional.empty();
    }
}
