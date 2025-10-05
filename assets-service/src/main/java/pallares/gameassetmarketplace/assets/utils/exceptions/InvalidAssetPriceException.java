package pallares.gameassetmarketplace.assets.utils.exceptions;

public class InvalidAssetPriceException extends RuntimeException {

    public InvalidAssetPriceException() {}

    public InvalidAssetPriceException(String message) {
        super(message);
    }

    public InvalidAssetPriceException(Throwable cause) {
        super(cause);
    }

    public InvalidAssetPriceException(String message, Throwable cause) {
        super(message, cause);
    }
}

