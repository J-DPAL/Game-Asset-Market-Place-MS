package pallares.gameassetmarketplace.users.utils.exceptions;

public class InvalidEmailException extends RuntimeException{

    public InvalidEmailException() {}

    public InvalidEmailException(String message) { super(message); }

    public InvalidEmailException(Throwable cause) { super(cause); }

    public InvalidEmailException(String message, Throwable cause) { super(message, cause); }
}
