package akletini.life.core.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TodoStoreException extends RuntimeException{

    public TodoStoreException(String errorMessage) {
        super(errorMessage);
    }
}
