package pallares.gameassetmarketplace.apigateway.utils.exceptions;

public class InvalidDiscountException extends RuntimeException {
    public InvalidDiscountException() {}
    public InvalidDiscountException(String message) { super(message); }
    public InvalidDiscountException(Throwable cause) { super(cause); }
    public InvalidDiscountException(String message, Throwable cause) { super(message, cause); }
}
