package akletini.life.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TagStoreException extends RuntimeException {
    public TagStoreException(String errorMessage) {
        super(errorMessage);
    }
}
