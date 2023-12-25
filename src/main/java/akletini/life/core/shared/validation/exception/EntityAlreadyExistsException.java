package akletini.life.core.shared.validation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EntityAlreadyExistsException extends BusinessException {
    public EntityAlreadyExistsException(String message) {
        super(message);
    }
}
