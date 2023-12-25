package akletini.life.core.shared.validation;

import akletini.life.core.shared.validation.exception.BusinessException;

import java.util.Optional;

@FunctionalInterface
public interface ValidationRule<T> {
    Optional<BusinessException> validate(final T validatable);
}
