package pallares.gameassetmarketplace.apigateway.presentationLayer.payments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pallares.gameassetmarketplace.apigateway.businessLayer.payments.PaymentService;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.payments.Currency;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.payments.PaymentType;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.payments.TransactionStatus;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentControllerUnitTest {

    private PaymentService paymentService;
    private PaymentController paymentController;

    private PaymentResponseModel sampleResponse;

    @BeforeEach
    void setup() {
        paymentService = mock(PaymentService.class);
        paymentController = new PaymentController(paymentService);

        sampleResponse = new PaymentResponseModel(
                "pay123",
                new BigDecimal("49.99"),
                Currency.US,
                PaymentType.CREDIT_CARD,
                TransactionStatus.COMPLETED
        );
    }

    @Test
    void getPaymentByPaymentId_shouldReturnPayment() {
        when(paymentService.getPaymentByPaymentId("pay123")).thenReturn(sampleResponse);

        ResponseEntity<PaymentResponseModel> response = paymentController.getPaymentByPaymentId("pay123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleResponse, response.getBody());
        verify(paymentService, times(1)).getPaymentByPaymentId("pay123");
    }

    @Test
    void getAllPayments_shouldReturnListOfPayments() {
        when(paymentService.getAllPayments()).thenReturn(List.of(sampleResponse));

        ResponseEntity<List<PaymentResponseModel>> response = paymentController.getAllPayments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(sampleResponse, response.getBody().get(0));
        verify(paymentService, times(1)).getAllPayments();
    }

    @Test
    void addPayment_shouldReturnCreatedPayment() {
        PaymentRequestModel requestModel = new PaymentRequestModel(
                new BigDecimal("49.99"),
                Currency.US,
                PaymentType.CREDIT_CARD,
                TransactionStatus.COMPLETED
        );

        when(paymentService.addPayment(requestModel)).thenReturn(sampleResponse);

        ResponseEntity<PaymentResponseModel> response = paymentController.addPayment(requestModel);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleResponse, response.getBody());
        verify(paymentService, times(1)).addPayment(requestModel);
    }

    @Test
    void updatePayment_shouldReturnUpdatedPayment() {
        PaymentRequestModel updateModel = new PaymentRequestModel(
                new BigDecimal("59.99"),
                Currency.US,
                PaymentType.PAYPAL,
                TransactionStatus.PENDING
        );

        PaymentResponseModel updatedResponse = new PaymentResponseModel(
                "pay123",
                new BigDecimal("59.99"),
                Currency.US,
                PaymentType.PAYPAL,
                TransactionStatus.PENDING
        );

        when(paymentService.updatePayment(updateModel, "pay123")).thenReturn(updatedResponse);

        ResponseEntity<PaymentResponseModel> response = paymentController.updatePayment("pay123", updateModel);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedResponse, response.getBody());
        verify(paymentService, times(1)).updatePayment(updateModel, "pay123");
    }

    @Test
    void removePayment_shouldReturnNoContent() {
        doNothing().when(paymentService).removePayment("pay123");

        ResponseEntity<PaymentResponseModel> response = paymentController.removePayment("pay123");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(paymentService, times(1)).removePayment("pay123");
    }
}
