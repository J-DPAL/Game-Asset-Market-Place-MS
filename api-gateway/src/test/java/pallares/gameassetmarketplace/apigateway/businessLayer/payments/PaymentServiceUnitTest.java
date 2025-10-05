package pallares.gameassetmarketplace.apigateway.businessLayer.payments;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pallares.gameassetmarketplace.apigateway.domainclientLayer.payments.PaymentsServiceClient;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.payments.Currency;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.payments.PaymentType;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.payments.TransactionStatus;
import pallares.gameassetmarketplace.apigateway.presentationLayer.payments.PaymentRequestModel;
import pallares.gameassetmarketplace.apigateway.presentationLayer.payments.PaymentResponseModel;

class PaymentServiceUnitTest {

    @Mock
    private PaymentsServiceClient paymentsServiceClient;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private PaymentResponseModel sampleResponse;
    private PaymentRequestModel sampleRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleResponse = new PaymentResponseModel(
                "payment123",
                new BigDecimal("49.99"),
                Currency.US,
                PaymentType.CREDIT_CARD,
                TransactionStatus.COMPLETED
        );

        sampleRequest = new PaymentRequestModel(
                new BigDecimal("49.99"),
                Currency.US,
                PaymentType.CREDIT_CARD,
                TransactionStatus.COMPLETED
        );
    }

    @Test
    void getAllPayments_delegatesToClient() {
        when(paymentsServiceClient.getPayments()).thenReturn(List.of(sampleResponse));

        List<PaymentResponseModel> result = paymentService.getAllPayments();

        assertEquals(1, result.size());
        assertSame(sampleResponse, result.get(0));
        verify(paymentsServiceClient).getPayments();
    }

    @Test
    void getPaymentById_delegatesToClient() {
        when(paymentsServiceClient.getPaymentByPaymentId("payment123")).thenReturn(sampleResponse);

        PaymentResponseModel result = paymentService.getPaymentByPaymentId("payment123");

        assertEquals("payment123", result.getPaymentId());
        verify(paymentsServiceClient).getPaymentByPaymentId("payment123");
    }

    @Test
    void addPayment_delegatesToClient() {
        when(paymentsServiceClient.addPayment(sampleRequest)).thenReturn(sampleResponse);

        PaymentResponseModel result = paymentService.addPayment(sampleRequest);

        assertSame(sampleResponse, result);
        verify(paymentsServiceClient).addPayment(sampleRequest);
    }

    @Test
    void updatePayment_invokesUpdateThenRetrieve() {
        // for a void method, use doNothing()
        doNothing().when(paymentsServiceClient).updatePayment("payment123", sampleRequest);
        when(paymentsServiceClient.getPaymentByPaymentId("payment123")).thenReturn(sampleResponse);

        PaymentResponseModel result = paymentService.updatePayment(sampleRequest, "payment123");

        assertSame(sampleResponse, result);

        InOrder inOrder = inOrder(paymentsServiceClient);
        inOrder.verify(paymentsServiceClient).updatePayment("payment123", sampleRequest);
        inOrder.verify(paymentsServiceClient).getPaymentByPaymentId("payment123");
    }

    @Test
    void removePayment_delegatesToClient() {
        doNothing().when(paymentsServiceClient).removePayment("payment123");

        paymentService.removePayment("payment123");

        verify(paymentsServiceClient).removePayment("payment123");
    }
}
