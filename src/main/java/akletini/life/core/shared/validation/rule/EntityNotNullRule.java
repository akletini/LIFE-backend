package akletini.life.core.shared.validation.rule;

import akletini.life.core.shared.validation.Errors;
import akletini.life.core.shared.validation.ValidationRule;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EntityNotNullRule<T extends Object> implements ValidationRule<T> {
    @Override
    public Optional<String> validate(Object validatable) {
        if (validatable == null) {
            return Optional.of(Errors.getError(Errors.ENTITY_IS_NULL));
        }
        return Optional.empty();
    }
}
