package pallares.gameassetmarketplace.users.utils.exceptions;

public class InvalidPhoneNumberException extends RuntimeException {

    public InvalidPhoneNumberException() {}

    public InvalidPhoneNumberException(String message) {
        super(message);
    }

    public InvalidPhoneNumberException(Throwable cause) {
        super(cause);
    }

    public InvalidPhoneNumberException(String message, Throwable cause) {
        super(message, cause);
    }
}
