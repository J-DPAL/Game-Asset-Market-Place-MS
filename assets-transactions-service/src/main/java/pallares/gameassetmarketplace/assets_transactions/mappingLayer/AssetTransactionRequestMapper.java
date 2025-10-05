package pallares.gameassetmarketplace.assets_transactions.mappingLayer;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pallares.gameassetmarketplace.assets_transactions.dataAccessLayer.*;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.assets.AssetModel;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.payments.PaymentModel;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.users.UserModel;
import pallares.gameassetmarketplace.assets_transactions.presentationLayer.AssetTransactionRequestModel;


@Mapper(componentModel = "spring")
public interface AssetTransactionRequestMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(expression = "java(status)", target = "status"),
            @Mapping(expression = "java(type)", target = "type"),
            @Mapping(expression = "java(discount)", target = "discount")
    })
    AssetTransaction requestModelToEntity(AssetTransactionRequestModel assetTransactionRequestModel,
                                          AssetTransactionIdentifier assetTransactionIdentifier,
                                          UserModel userModel,
                                          PaymentModel paymentModel,
                                          AssetModel assetModel,
                                          AssetTransactionStatus status,
                                          AssetTransactionType type,
                                          Discount discount);
}
