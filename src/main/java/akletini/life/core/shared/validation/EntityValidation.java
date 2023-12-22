package akletini.life.core.shared.validation;

import java.util.List;

public interface EntityValidation<T> {
    List<ValidationRule<T>> getValidationRules();
}
