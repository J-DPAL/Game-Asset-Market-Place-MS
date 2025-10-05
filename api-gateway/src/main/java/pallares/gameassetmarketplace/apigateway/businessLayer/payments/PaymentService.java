package pallares.gameassetmarketplace.apigateway.businessLayer.payments;

import org.springframework.stereotype.Service;
import pallares.gameassetmarketplace.apigateway.presentationLayer.payments.PaymentRequestModel;
import pallares.gameassetmarketplace.apigateway.presentationLayer.payments.PaymentResponseModel;

import java.util.List;
import java.util.Map;

@Service
public interface PaymentService {

    List<PaymentResponseModel> getAllPayments();
    PaymentResponseModel getPaymentByPaymentId(String paymentId);
    PaymentResponseModel addPayment(PaymentRequestModel PaymentRequestModel);
    PaymentResponseModel updatePayment(PaymentRequestModel paymentRequestModel, String paymentId);
    void removePayment(String paymentId);
}
