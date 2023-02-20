package akletini.life.shared.validation;

import java.util.Optional;

@FunctionalInterface
public interface ValidationRule<T> {
    Optional<String> validate(final T object);
}
