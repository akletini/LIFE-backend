package akletini.life.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TagNotFoundException extends RuntimeException {

    public TagNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
