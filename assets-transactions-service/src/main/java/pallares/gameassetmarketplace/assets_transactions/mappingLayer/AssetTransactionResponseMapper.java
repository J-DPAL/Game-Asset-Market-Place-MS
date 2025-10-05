package pallares.gameassetmarketplace.assets_transactions.mappingLayer;

import org.mapstruct.*;
import org.springframework.hateoas.Link;
import pallares.gameassetmarketplace.assets_transactions.dataAccessLayer.*;
import pallares.gameassetmarketplace.assets_transactions.presentationLayer.AssetTransactionController;
import pallares.gameassetmarketplace.assets_transactions.presentationLayer.AssetTransactionResponseModel;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring")
public interface AssetTransactionResponseMapper {

    @Mappings({
            @Mapping(expression = "java(assetTransaction.getAssetTransactionIdentifier().getAssetTransactionId())", target = "assetTransactionId"),
            @Mapping(expression = "java(assetTransaction.getUserModel().getUserId())", target = "userId"),
            @Mapping(expression = "java(assetTransaction.getUserModel().getUsername())", target = "username"),
            @Mapping(expression = "java(assetTransaction.getUserModel().getEmailAddress())", target = "emailAddress"),
            @Mapping(expression = "java(assetTransaction.getUserModel().getPhoneNumber())", target = "phoneNumber"),

            @Mapping(expression = "java(assetTransaction.getAssetModel().getAssetId())", target = "assetId"),
            @Mapping(expression = "java(assetTransaction.getAssetModel().getName())", target = "name"),
            @Mapping(expression = "java(assetTransaction.getAssetModel().getAssetType())", target = "assetType"),
            @Mapping(source="assetTransaction.assetModel.description", target="description"),
            @Mapping(source="assetTransaction.assetModel.fileUrl", target="fileUrl"),
            @Mapping(source="assetTransaction.assetModel.thumbnailUrl", target="thumbnailUrl"),
            @Mapping(expression = "java(assetTransaction.getAssetModel().getLicenseType())", target = "licenseType"),

            @Mapping(expression = "java(assetTransaction.getPaymentModel().getPaymentId())", target = "paymentId"),
            @Mapping(expression = "java(assetTransaction.getPaymentModel().getPrice())", target = "price"),
            @Mapping(expression = "java(assetTransaction.getPaymentModel().getCurrency())", target = "currency"),
            @Mapping(expression = "java(assetTransaction.getPaymentModel().getPaymentType())", target = "paymentType"),
            @Mapping(expression = "java(assetTransaction.getPaymentModel().getTransactionStatus())", target = "transactionStatus"),

            @Mapping(source = "status", target = "status"),
            @Mapping(source = "type", target = "type"),
            @Mapping(source = "discount", target = "discount")
    })
    AssetTransactionResponseModel entityToResponseModel(AssetTransaction assetTransaction);


    List<AssetTransactionResponseModel> entityListToResponseModelList(List<AssetTransaction> assetTransactions);

    @AfterMapping
    default void addLinks(@MappingTarget AssetTransactionResponseModel model, AssetTransaction entity) {
        String userId = model.getUserId();
        // Self
        model.add(linkTo(methodOn(AssetTransactionController.class)
                .getAssetTransactionByAssetTransactionId(model.getAssetTransactionId(), userId)).withSelfRel());

        // All transactions for user
        model.add(linkTo(methodOn(AssetTransactionController.class)
                .getAllAssetTransactions(userId)).withRel("asset-transactions"));

        // Link to asset (assets-service)
        model.add(Link.of(
                String.format("http://localhost:8080/api/v1/users/%s", model.getAssetId()),
                "asset"));

        // Link to payment (payments-service)
        model.add(Link.of(
                String.format("http://localhost:8080/api/v1/payments/%s", model.getPaymentId()),
                "payment"));

        // Link to user (users-service)
        model.add(Link.of(
                String.format("http://localhost:8080/api/v1/users/%s", model.getUserId()),
                "user"));
    }
}
