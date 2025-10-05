package pallares.gameassetmarketplace.assets_transactions.businessLayer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.web.client.HttpClientErrorException;
import pallares.gameassetmarketplace.assets_transactions.dataAccessLayer.*;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.assets.AssetModel;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.assets.AssetsServiceClient;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.payments.PaymentModel;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.payments.PaymentsServiceClient;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.users.UserModel;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.users.UsersServiceClient;
import pallares.gameassetmarketplace.assets_transactions.mappingLayer.AssetTransactionRequestMapper;
import pallares.gameassetmarketplace.assets_transactions.mappingLayer.AssetTransactionResponseMapper;
import pallares.gameassetmarketplace.assets_transactions.presentationLayer.AssetTransactionRequestModel;
import pallares.gameassetmarketplace.assets_transactions.presentationLayer.AssetTransactionResponseModel;
import pallares.gameassetmarketplace.assets_transactions.utils.exceptions.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class AssetTransactionServiceImpl implements AssetTransactionService {

    private final AssetTransactionRepository assetTransactionRepository;
    private final AssetTransactionResponseMapper assetTransactionResponseMapper;
    private final AssetTransactionRequestMapper assetTransactionRequestMapper;
    private final UsersServiceClient userServiceClient;
    private final PaymentsServiceClient paymentServiceClient;
    private final AssetsServiceClient assetServiceClient;

    public AssetTransactionServiceImpl(AssetTransactionRepository assetTransactionRepository,
                                       AssetTransactionResponseMapper assetTransactionResponseMapper,
                                       AssetTransactionRequestMapper assetTransactionRequestMapper, UsersServiceClient userServiceClient, PaymentsServiceClient paymentServiceClient, AssetsServiceClient assetServiceClient) {
        this.assetTransactionRepository = assetTransactionRepository;
        this.assetTransactionResponseMapper = assetTransactionResponseMapper;
        this.assetTransactionRequestMapper = assetTransactionRequestMapper;
        this.userServiceClient = userServiceClient;
        this.paymentServiceClient = paymentServiceClient;
        this.assetServiceClient = assetServiceClient;
    }

    @Override
    public List<AssetTransactionResponseModel> getAllAssetTransactions(String userId) {
        try {
            UserModel user = userServiceClient.getUserByUserId(userId);
            if (user == null) {
                throw new NotFoundException("Unknown userId: " + userId);
            }
        } catch (HttpClientErrorException.NotFound ex) {
            throw new NotFoundException("User not found: " + userId);
        }
        List<AssetTransaction> assetTransactions = assetTransactionRepository.findAllByUserModel_UserId(userId);
        List<AssetTransactionResponseModel> assetTransactionResponseModelList = assetTransactionResponseMapper.entityListToResponseModelList(assetTransactions);
        return assetTransactionResponseModelList;
    }

    @Override
    public AssetTransactionResponseModel getAssetTransactionByAssetTransactionId(String assetTransactionId, String userId) {
        AssetTransaction foundAssetTransaction = assetTransactionRepository.findByAssetTransactionIdentifier_AssetTransactionIdAndUserModel_UserId(assetTransactionId, userId);
        if (foundAssetTransaction == null) {
            throw new NotFoundException("No asset transaction found for id: " + assetTransactionId + " and userId: " + userId);
        }
        return assetTransactionResponseMapper.entityToResponseModel(foundAssetTransaction);
    }

    @Override
    public AssetTransactionResponseModel addAssetTransaction(AssetTransactionRequestModel assetTransactionRequestModel, String userId) {
        AssetModel assetModel;
        try {
            assetModel = assetServiceClient.getAssetByAssetId(assetTransactionRequestModel.getAssetId());
            if (assetModel == null) {
                throw new NotFoundException("Asset not found for assetId: " + assetTransactionRequestModel.getAssetId());
            }
        } catch (HttpClientErrorException.NotFound ex) {
            throw new NotFoundException("Asset not found for assetId: " + assetTransactionRequestModel.getAssetId());
        }

        PaymentModel foundPayment;
        try {
            foundPayment = paymentServiceClient.getPaymentByPaymentId(assetTransactionRequestModel.getPaymentId());
        } catch (HttpClientErrorException.BadRequest ex) {
            // Translate Spring HTTP exception into domain-specific exception
            throw new InvalidInputException("Invalid paymentId provided: " + assetTransactionRequestModel.getPaymentId(), ex);
        }

        if (foundPayment == null) {
            throw new NotFoundException("Unknown paymentId provided: " + assetTransactionRequestModel.getPaymentId());
        }

        if (foundPayment.getItemPrice() == null) {
            throw new IllegalStateException("Payment retrieved has null itemPrice for paymentId: " + assetTransactionRequestModel.getPaymentId());
        }

        if (foundPayment.getPrice() == null) {
            throw new IllegalStateException("Payment retrieved has null price for paymentId: " + assetTransactionRequestModel.getPaymentId());
        }

        UserModel userModel;
        try {
            userModel = userServiceClient.getUserByUserId(userId);
            if (userModel == null) {
                throw new NotFoundException("User not found for userId: " + userId);
            }
        } catch (HttpClientErrorException.NotFound ex) {
            throw new NotFoundException("User not found for userId: " + userId);
        }

        Discount discount = assetTransactionRequestModel.getDiscount();
        if (discount.getPercentage().compareTo(BigDecimal.ZERO) < 0 ||
                discount.getPercentage().compareTo(new BigDecimal("100")) > 0) {
            throw new InvalidDiscountException("Discount percentage must be between 0 and 100");
        }

        if (discount.getAmountOff().signum() < 0) {
            throw new InvalidDiscountException("Discount amountOff must be non-negative");
        }

        boolean purchased = assetTransactionRepository.existsByUserModel_UserIdAndAssetModel_AssetId(
                userId, assetTransactionRequestModel.getAssetId()
        );
        if (purchased) {
            throw new AssetAlreadyPurchasedException(
                    "User with userId " + userId + " already purchased this asset " + assetTransactionRequestModel.getAssetId()
            );
        }

        AssetTransaction assetTransaction = assetTransactionRequestMapper.requestModelToEntity(
                assetTransactionRequestModel,
                new AssetTransactionIdentifier(),
                userModel,
                foundPayment,
                assetModel,
                assetTransactionRequestModel.getStatus(),
                assetTransactionRequestModel.getType(),
                new Discount(
                        assetTransactionRequestModel.getDiscount().getPercentage(),
                        assetTransactionRequestModel.getDiscount().getAmountOff(),
                        assetTransactionRequestModel.getDiscount().getReason()
                )
        );

        AssetTransaction savedAssetTransaction = assetTransactionRepository.save(assetTransaction);
        return assetTransactionResponseMapper.entityToResponseModel(savedAssetTransaction);
    }


    @Override
    public AssetTransactionResponseModel updateAssetTransaction(AssetTransactionRequestModel assetTransactionRequestModel, String assetTransactionId, String userId) {
        AssetTransaction existingAssetTransaction = assetTransactionRepository.findByAssetTransactionIdentifier_AssetTransactionIdAndUserModel_UserId(assetTransactionId, userId);
        if (existingAssetTransaction == null) {
            throw new NotFoundException("No asset transaction found for id: " + assetTransactionId + " and userId: " + userId);
        }

        AssetModel asset = assetServiceClient.getAssetByAssetId(assetTransactionRequestModel.getAssetId());
        if(asset == null) {
            throw new NotFoundException("Unknown assetId provided: " + assetTransactionRequestModel.getAssetId());
        }

        PaymentModel payment = paymentServiceClient.getPaymentByPaymentId(assetTransactionRequestModel.getPaymentId());
        if (payment == null) {
            throw new NotFoundException("Unknown paymentId provided: " + assetTransactionRequestModel.getPaymentId());
        }
        if (payment.getItemPrice() == null) {
            throw new IllegalStateException("Payment retrieved has null itemPrice for paymentId: " + assetTransactionRequestModel.getPaymentId());
        }
        if (payment.getPrice() == null) {
            throw new IllegalStateException("Payment retrieved has null price for paymentId: " + assetTransactionRequestModel.getPaymentId());
        }

        UserModel user = userServiceClient.getUserByUserId(userId);
        if (user == null) {
            throw new NotFoundException("Unknown userId provided: " + userId);
        }

        Discount discount = assetTransactionRequestModel.getDiscount();
        if (discount.getPercentage().compareTo(BigDecimal.ZERO) < 0 ||
                discount.getPercentage().compareTo(new BigDecimal("100")) > 0) {
            throw new InvalidDiscountException("Discount percentage must be between 0 and 100");
        }

        if (discount.getAmountOff().signum() < 0) {
            throw new InvalidDiscountException("Discount amountOff must be non-negative");
        }

        AssetTransactionStatus oldStatus = existingAssetTransaction.getStatus();
        AssetTransactionStatus newStatus = assetTransactionRequestModel.getStatus();
        if (oldStatus == AssetTransactionStatus.PAID && newStatus != AssetTransactionStatus.PAID) {
            throw new InvalidTransactionStateException("Cannot change status from PAID to " + newStatus);
        }

        existingAssetTransaction.setAssetModel(asset);
        existingAssetTransaction.setPaymentModel(payment);
        existingAssetTransaction.setUserModel(user);
        existingAssetTransaction.setStatus(assetTransactionRequestModel.getStatus());
        existingAssetTransaction.setType(assetTransactionRequestModel.getType());
        existingAssetTransaction.setDiscount(new Discount(assetTransactionRequestModel.getDiscount().getPercentage(), assetTransactionRequestModel.getDiscount().getAmountOff(), assetTransactionRequestModel.getDiscount().getReason()));

        AssetTransaction savedPAssetTransaction = assetTransactionRepository.save(existingAssetTransaction);
        return assetTransactionResponseMapper.entityToResponseModel(savedPAssetTransaction);

    }

    @Override
    public void removeAssetTransaction(String assetTransactionId, String userId) {
        AssetTransaction existingAssetTransaction = assetTransactionRepository.findByAssetTransactionIdentifier_AssetTransactionIdAndUserModel_UserId(assetTransactionId, userId);

        if (existingAssetTransaction == null) {
            throw new NotFoundException("No asset transaction found for id: " + assetTransactionId + " and userId: " + userId);
        }

        assetTransactionRepository.delete(existingAssetTransaction);
    }
}