package akletini.life.core.user.validation.rule;

import akletini.life.core.shared.validation.Errors;
import akletini.life.core.shared.validation.ValidationRule;
import akletini.life.core.shared.validation.exception.BusinessException;
import akletini.life.core.shared.validation.exception.InvalidDataException;
import akletini.life.core.user.repository.entity.AuthProvider;
import akletini.life.core.user.repository.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PasswordNotEmptyRule implements ValidationRule<User> {
    @Override
    public Optional<BusinessException> validate(User validatable) {
        if (AuthProvider.CREDENTIALS.equals(validatable.getAuthProvider())) {
            if (StringUtils.isEmpty(validatable.getPassword())) {
                return Optional.of(new InvalidDataException(Errors.getError(Errors.USER.MISSING_PASSWORD)));
            }
        }
        return Optional.empty();
    }
}
