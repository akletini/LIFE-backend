package akletini.life.core.user.service.impl;

import akletini.life.core.shared.validation.EntityValidation;
import akletini.life.core.shared.validation.Errors;
import akletini.life.core.shared.validation.ValidationRule;
import akletini.life.core.shared.validation.exception.EntityNotFoundException;
import akletini.life.core.shared.validation.exception.InvalidDataException;
import akletini.life.core.user.repository.api.UserRepository;
import akletini.life.core.user.repository.entity.User;
import akletini.life.core.user.service.api.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static akletini.life.core.shared.validation.Errors.ENTITY_NOT_FOUND;

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
                InvalidDataException invalidDataException = new InvalidDataException(error.get());
                log.error(invalidDataException);
                throw invalidDataException;
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
            EntityNotFoundException exception =
                    new EntityNotFoundException(Errors.getError(ENTITY_NOT_FOUND,
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
