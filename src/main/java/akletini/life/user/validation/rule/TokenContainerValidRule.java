package akletini.life.user.validation.rule;

import akletini.life.shared.validation.Errors;
import akletini.life.shared.validation.ValidationRule;
import akletini.life.user.repository.entity.AuthProvider;
import akletini.life.user.repository.entity.TokenContainer;
import akletini.life.user.repository.entity.User;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static akletini.life.shared.validation.Errors.USER.INVALID_TOKEN_CONTAINER;

@Component
public class TokenContainerValidRule implements ValidationRule<User> {
    @Override
    public Optional<String> validate(User validatable) {
        if (AuthProvider.GOOGLE.equals(validatable.getAuthProvider())) {
            TokenContainer tokenContainer = validatable.getTokenContainer();
            boolean allNonNull = false;
            if (tokenContainer != null) {
                allNonNull = Stream.of(tokenContainer.getAccessToken(),
                                tokenContainer.getRefreshToken(),
                                tokenContainer.getAccessTokenCreation())
                        .allMatch(Objects::nonNull);
            }
            if (!allNonNull) {
                return Optional.of(Errors.getError(INVALID_TOKEN_CONTAINER,
                        validatable.getEmail()));
            }
        }
        return Optional.empty();
    }
}
