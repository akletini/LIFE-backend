package akletini.life.user.service.impl;

import akletini.life.shared.validation.EntityValidation;
import akletini.life.shared.validation.Errors;
import akletini.life.shared.validation.ValidationRule;
import akletini.life.user.exception.UserNotFoundException;
import akletini.life.user.exception.UserStoreException;
import akletini.life.user.repository.api.UserRepository;
import akletini.life.user.repository.entity.User;
import akletini.life.user.service.api.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static akletini.life.shared.validation.Errors.ENTITY_NOT_FOUND;

@Service
@AllArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private EntityValidation<User> validation;

    @Override
    public boolean validate(User user) {
        List<ValidationRule<User>> validationRules = validation.getValidationRules();
        validationRules.forEach(rule -> {
            Optional<String> error = rule.validate(user);
            if (error.isPresent()) {
                UserStoreException userStoreException = new UserStoreException(error.get());
                log.error(userStoreException);
                throw userStoreException;
            }
        });
        return true;
    }

    @Override
    public User store(@NonNull User user) {
        validate(user);
        return userRepository.save(user);
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> {
            UserNotFoundException exception =
                    new UserNotFoundException(Errors.getError(ENTITY_NOT_FOUND,
                            User.class.getSimpleName(), id));
            log.error(exception);
            return exception;
        });
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public User getByEmail(String email) {
        Optional<User> userByEmail = userRepository.findByEmail(email);
        if (userByEmail.isPresent()) {
            return userByEmail.get();
        }
        throw new RuntimeException("User not found");
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

}
