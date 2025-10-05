package pallares.gameassetmarketplace.payments.utils.exceptions;

public class UnsupportedPaymentTypeException extends RuntimeException {

    public UnsupportedPaymentTypeException() {}

    public UnsupportedPaymentTypeException(String message) {
        super(message);
    }

    public UnsupportedPaymentTypeException(Throwable cause) {
        super(cause);
    }

    public UnsupportedPaymentTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
