package akletini.life.todo.exception.custom;

public class TodoStoreException extends RuntimeException{

    public TodoStoreException(String errorMessage) {
        super(errorMessage);
    }
}
