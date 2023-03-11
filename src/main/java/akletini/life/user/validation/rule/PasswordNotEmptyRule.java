package akletini.life.user.validation.rule;

import akletini.life.shared.validation.Errors;
import akletini.life.shared.validation.ValidationRule;
import akletini.life.user.repository.entity.AuthProvider;
import akletini.life.user.repository.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static akletini.life.shared.validation.Errors.USER.MISSING_PASSWORD;

@Component
public class PasswordNotEmptyRule implements ValidationRule<User> {
    @Override
    public Optional<String> validate(User validatable) {
        if (AuthProvider.CREDENTIALS.equals(validatable.getAuthProvider())) {
            if (StringUtils.isEmpty(validatable.getPassword())) {
                return Optional.of(Errors.getError(MISSING_PASSWORD));
            }
        }
        return Optional.empty();
    }
}
