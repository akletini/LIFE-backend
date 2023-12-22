package akletini.life.core.user.validation;

import akletini.life.core.shared.validation.EntityValidation;
import akletini.life.core.shared.validation.ValidationRule;
import akletini.life.core.user.repository.entity.User;
import akletini.life.core.user.validation.rule.PasswordNotEmptyRule;
import akletini.life.core.user.validation.rule.TokenContainerValidRule;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class UserValidation implements EntityValidation<User> {

    private TokenContainerValidRule tokenContainerValidRule;

    private PasswordNotEmptyRule passwordNotEmptyRule;

    @Override
    public List<ValidationRule<User>> getValidationRules() {
        ArrayList<ValidationRule<User>> rules = new ArrayList<>();
        rules.add(tokenContainerValidRule);
        rules.add(passwordNotEmptyRule);
        return rules;
    }
}
