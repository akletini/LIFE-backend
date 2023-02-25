package akletini.life.chore.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ChoreStoreException extends RuntimeException {
    public ChoreStoreException(String message) {
        super(message);
    }
}
