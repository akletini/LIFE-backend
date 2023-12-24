package akletini.life.core.shared;

import akletini.life.core.chore.repository.entity.Chore;
import akletini.life.core.shared.validation.EntityValidation;
import akletini.life.core.shared.validation.Errors;
import akletini.life.core.shared.validation.ValidationRule;
import akletini.life.core.shared.validation.exception.EntityNotFoundException;
import akletini.life.core.shared.validation.exception.InvalidDataException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public abstract class EntityService<T extends BaseEntity> {

    @Autowired
    protected EntityValidation<T> entityValidation;

    @Autowired
    protected ListCrudRepository<T, Long> entityRepository;

    public boolean validate(T object) throws InvalidDataException {
        List<ValidationRule<T>> validationRules = entityValidation.getValidationRules();
        for (ValidationRule<T> rule : validationRules) {
            Optional<String> error = rule.validate(object);
            if (error.isPresent()) {
                InvalidDataException invalidDataException = new InvalidDataException(error.get());
                log.error(invalidDataException);
                throw invalidDataException;
            }
        }
        return true;
    }

    public T store(T object) throws InvalidDataException {
        validate(object);
        log.info("Saving entity {}", object.getClass().getSimpleName());
        return entityRepository.save(object);
    }

    public T getById(Long id) throws EntityNotFoundException {
        log.info("Searching entity with id {}", id);
        return entityRepository.findById(id).orElseThrow(() -> {
            EntityNotFoundException exception =
                    new EntityNotFoundException(Errors.getError(Errors.ENTITY_NOT_FOUND,
                            Chore.class.getSimpleName(), id));
            log.error(exception);
            return exception;
        });
    }

    public void delete(@NotNull T object) {
        log.info("Deleting entity {} with id {}", object.getClass().getSimpleName(),
                object.getId());
        entityRepository.delete(object);
    }

    public List<T> getAll() {
        return entityRepository.findAll();
    }
}
