package akletini.life.core.chore.validation.rule;

import akletini.life.core.chore.repository.entity.Chore;
import akletini.life.core.shared.validation.Errors;
import akletini.life.core.shared.validation.ValidationRule;
import akletini.life.core.shared.validation.exception.BusinessException;
import akletini.life.core.shared.validation.exception.InvalidDataException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PositiveIntervalRule implements ValidationRule<Chore> {
    @Override
    public Optional<BusinessException> validate(Chore chore) {
        if (chore.getInterval().getValue() <= 0) {
            return Optional.of(new InvalidDataException(Errors.getError(Errors.CHORE.POSITIVE_INTERVAL)));
        }
        return Optional.empty();
    }
}
