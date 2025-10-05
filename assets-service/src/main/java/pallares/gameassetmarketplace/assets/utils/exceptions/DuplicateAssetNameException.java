package pallares.gameassetmarketplace.assets.utils.exceptions;

public class DuplicateAssetNameException extends RuntimeException {

    public DuplicateAssetNameException() {}

    public DuplicateAssetNameException(String message) {
        super(message);
    }

    public DuplicateAssetNameException(Throwable cause) {
        super(cause);
    }

    public DuplicateAssetNameException(String message, Throwable cause) {
        super(message, cause);
    }
}


