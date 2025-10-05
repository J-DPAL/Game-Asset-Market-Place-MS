package pallares.gameassetmarketplace.apigateway.utils.exceptions;

public class InvalidTransactionStateException extends RuntimeException {
    public InvalidTransactionStateException() {}
    public InvalidTransactionStateException(String message) { super(message); }
    public InvalidTransactionStateException(Throwable cause) { super(cause); }
    public InvalidTransactionStateException(String message, Throwable cause) { super(message, cause); }
}
