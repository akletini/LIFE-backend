package akletini.life.chore.exception;

public class ChoreNotFoundException extends RuntimeException {
    public ChoreNotFoundException(String message) {
        super(message);
    }
}
