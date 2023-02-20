package akletini.life.chore.validation.rules;

import akletini.life.chore.repository.entity.Chore;
import akletini.life.shared.validation.Errors;
import akletini.life.shared.validation.ValidationRule;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PositiveIntervalRule implements ValidationRule<Chore> {
    @Override
    public Optional<String> validate(Chore chore) {
        if (chore.getInterval().getValue() <= 0) {
            return Optional.of(Errors.getError(Errors.CHORE.POSITIVE_INTERVAL));
        }
        return Optional.empty();
    }
}
