package akletini.life.todo.exception.custom;

public class TodoNotFoundException extends RuntimeException {

    public TodoNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
