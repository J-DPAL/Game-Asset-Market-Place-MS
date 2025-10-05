package pallares.gameassetmarketplace.payments.businessLayer;

import org.springframework.stereotype.Service;
import pallares.gameassetmarketplace.payments.presentationLayer.PaymentResponseModel;
import pallares.gameassetmarketplace.payments.presentationLayer.PaymentRequestModel;
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
