package akletini.life.todo.exception.custom;

public class TagNotFoundException extends RuntimeException {

    public TagNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
