package pallares.gameassetmarketplace.apigateway.utils.exceptions;

public class AssetFileMissingException extends RuntimeException {

    public AssetFileMissingException() {}

    public AssetFileMissingException(String message) {
        super(message);
    }

    public AssetFileMissingException(Throwable cause) {
        super(cause);
    }

    public AssetFileMissingException(String message, Throwable cause) {
        super(message, cause);
    }
}
