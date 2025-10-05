package pallares.gameassetmarketplace.apigateway.businessLayer.payments;

import pallares.gameassetmarketplace.apigateway.domainclientLayer.payments.PaymentsServiceClient;
import pallares.gameassetmarketplace.apigateway.presentationLayer.payments.PaymentRequestModel;
import pallares.gameassetmarketplace.apigateway.presentationLayer.payments.PaymentResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentsServiceClient paymentsServiceClient;

    @Autowired
    public PaymentServiceImpl(PaymentsServiceClient paymentsServiceClient) {
        this.paymentsServiceClient = paymentsServiceClient;
    }

    @Override
    public PaymentResponseModel getPaymentByPaymentId(String paymentId) {
        log.debug("Fetching payment with ID: {}", paymentId);
        return paymentsServiceClient.getPaymentByPaymentId(paymentId);
    }

    @Override
    public List<PaymentResponseModel> getAllPayments() {
        log.debug("Fetching all payments");
        return paymentsServiceClient.getPayments();
    }

    @Override
    public PaymentResponseModel addPayment(PaymentRequestModel paymentRequestModel) {
        log.debug("Creating new payment: {}", paymentRequestModel);
        return paymentsServiceClient.addPayment(paymentRequestModel);
    }

    @Override
    public PaymentResponseModel updatePayment(PaymentRequestModel paymentRequestModel, String paymentId) {
        log.debug("Updating payment with ID: {}", paymentId);
        paymentsServiceClient.updatePayment(paymentId, paymentRequestModel);
        return paymentsServiceClient.getPaymentByPaymentId(paymentId);
    }

    @Override
    public void removePayment(String paymentId) {
        log.debug("Deleting payment with ID: {}", paymentId);
        paymentsServiceClient.removePayment(paymentId);
    }
}
