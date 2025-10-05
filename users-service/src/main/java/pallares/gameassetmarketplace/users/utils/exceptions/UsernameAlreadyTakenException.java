package pallares.gameassetmarketplace.users.utils.exceptions;

public class UsernameAlreadyTakenException extends RuntimeException {

    public UsernameAlreadyTakenException() {}

    public UsernameAlreadyTakenException(String message) {
        super(message);
    }

    public UsernameAlreadyTakenException(Throwable cause) {
        super(cause);
    }

    public UsernameAlreadyTakenException(String message, Throwable cause) {
        super(message, cause);
    }
}
