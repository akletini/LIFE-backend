package akletini.life.core.shared;

import akletini.life.core.shared.validation.EntityValidation;
import akletini.life.core.shared.validation.Errors;
import akletini.life.core.shared.validation.ValidationRule;
import akletini.life.core.shared.validation.exception.BusinessException;
import akletini.life.core.shared.validation.exception.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public abstract class EntityService<T extends BaseEntity> {

    @Autowired
    protected EntityValidation<T> entityValidation;

    @Autowired
    protected ListCrudRepository<T, Long> entityRepository;

    public List<T> storeBulk(List<T> objects) throws BusinessException {
        List<T> storedObjects = new ArrayList<>();
        for (T object : objects) {
            storedObjects.add(store(object));
        }
        return storedObjects;
    }


    public boolean validate(T object) throws BusinessException {
        List<ValidationRule<T>> validationRules = entityValidation.getValidationRules();
        for (ValidationRule<T> rule : validationRules) {
            Optional<BusinessException> error = rule.validate(object);
            if (error.isPresent()) {
                BusinessException exception = error.get();
                log.error(exception);
                throw exception;
            }
        }
        return true;
    }

    public T store(T object) throws BusinessException {
        validate(object);
        log.info("Saving entity {}", object.getClass().getSimpleName());
        return entityRepository.save(object);
    }

    public T getById(Long id) throws EntityNotFoundException {
        log.info("Searching entity with id {}", id);
        return entityRepository.findById(id).orElseThrow(() -> {
            EntityNotFoundException exception =
                    new EntityNotFoundException(Errors.getError(Errors.ENTITY_NOT_FOUND,
                            "object", id));
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
