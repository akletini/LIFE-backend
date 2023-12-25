package akletini.life.core.shared.validation.rule;

import akletini.life.core.shared.validation.Errors;
import akletini.life.core.shared.validation.ValidationRule;
import akletini.life.core.shared.validation.exception.BusinessException;
import akletini.life.core.shared.validation.exception.EntityAlreadyExistsException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EntityNotNullRule<T> implements ValidationRule<T> {
    @Override
    public Optional<BusinessException> validate(Object validatable) {
        if (validatable == null) {
            return Optional.of(new EntityAlreadyExistsException(Errors.getError(Errors.ENTITY_IS_NULL)));
        }
        return Optional.empty();
    }
}
