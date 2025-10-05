package pallares.gameassetmarketplace.apigateway.presentationLayer.assetsTransactions;

import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import pallares.gameassetmarketplace.apigateway.presentationLayer.assetsTransactions.AssetTransactionController;
import pallares.gameassetmarketplace.apigateway.presentationLayer.assetsTransactions.AssetTransactionResponseModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class AssetTransactionModelAssembler implements RepresentationModelAssembler<AssetTransactionResponseModel, AssetTransactionResponseModel> {

    @Override
    public AssetTransactionResponseModel toModel(AssetTransactionResponseModel model) {
//        String userId = model.getUserId();
//        String txId = model.getAssetTransactionId();
//
//        model.add(linkTo(methodOn(AssetTransactionController.class)
//                .getById(txId, userId)).withSelfRel());
//
//        model.add(linkTo(methodOn(AssetTransactionController.class)
//                .getAll(userId)).withRel("all-transactions"));
//
//        model.add(linkTo(methodOn(AssetTransactionController.class)
//                .delete(txId, userId)).withRel("delete"));
//
//        model.add(linkTo(methodOn(AssetTransactionController.class)
//                .update(txId, userId, null)).withRel("update"));

        return model;
    }
}
