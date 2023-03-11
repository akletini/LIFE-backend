package akletini.life.chore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ChoreNotFoundException extends RuntimeException {
    public ChoreNotFoundException(String message) {
        super(message);
    }
}
