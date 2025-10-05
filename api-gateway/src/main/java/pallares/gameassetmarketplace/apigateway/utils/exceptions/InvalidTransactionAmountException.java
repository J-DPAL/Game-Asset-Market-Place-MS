package pallares.gameassetmarketplace.apigateway.utils.exceptions;

public class InvalidTransactionAmountException extends RuntimeException {

    public InvalidTransactionAmountException() {}

    public InvalidTransactionAmountException(String message) {
        super(message);
    }

    public InvalidTransactionAmountException(Throwable cause) {
        super(cause);
    }

    public InvalidTransactionAmountException(String message, Throwable cause) {
        super(message, cause);
    }
}
