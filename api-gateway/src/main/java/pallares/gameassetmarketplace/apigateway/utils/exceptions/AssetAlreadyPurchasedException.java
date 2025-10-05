package pallares.gameassetmarketplace.apigateway.utils.exceptions;

public class AssetAlreadyPurchasedException extends RuntimeException {
    public AssetAlreadyPurchasedException() {}
    public AssetAlreadyPurchasedException(String message) { super(message); }
    public AssetAlreadyPurchasedException(Throwable cause) { super(cause); }
    public AssetAlreadyPurchasedException(String message, Throwable cause) { super(message, cause); }
}
