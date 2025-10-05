package pallares.gameassetmarketplace.users.utils.exceptions;

public class DuplicateEmailAddressException extends RuntimeException {

    public DuplicateEmailAddressException() {}

    public DuplicateEmailAddressException(String message) {
        super(message);
    }

    public DuplicateEmailAddressException(Throwable cause) {
        super(cause);
    }

    public DuplicateEmailAddressException(String message, Throwable cause) {
        super(message, cause);
    }
}

