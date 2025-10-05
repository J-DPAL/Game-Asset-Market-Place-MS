package pallares.gameassetmarketplace.apigateway.presentationLayer.payments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import pallares.gameassetmarketplace.apigateway.businessLayer.payments.PaymentService;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.payments.Currency;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.payments.PaymentType;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.payments.TransactionStatus;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

@WebFluxTest(controllers = PaymentController.class)
class PaymentControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private PaymentService paymentService;

    private PaymentResponseModel mockPayment;

    private static final String BASE_URL = "/api/v1/payments";

    @BeforeEach
    void setUp() {
        mockPayment = new PaymentResponseModel(
                "payment123",
                new BigDecimal("49.99"),
                Currency.US,
                PaymentType.CREDIT_CARD,
                TransactionStatus.COMPLETED
        );
    }

    @Test
    void getAllPayments_ReturnsListOfPayments() {
        when(paymentService.getAllPayments()).thenReturn(List.of(mockPayment));

        webTestClient.get()
                .uri(BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PaymentResponseModel.class).hasSize(1);

        verify(paymentService, times(1)).getAllPayments();
    }

    @Test
    void getPaymentById_ReturnsPayment() {
        when(paymentService.getPaymentByPaymentId("payment123")).thenReturn(mockPayment);

        webTestClient.get()
                .uri(BASE_URL + "/payment123")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.paymentId").isEqualTo("payment123");

        verify(paymentService, times(1)).getPaymentByPaymentId("payment123");
    }

    @Test
    void createPayment_ReturnsCreatedPayment() {
        PaymentRequestModel request = new PaymentRequestModel(
                new BigDecimal("49.99"),
                Currency.US,
                PaymentType.CREDIT_CARD,
                TransactionStatus.COMPLETED
        );

        when(paymentService.addPayment(request)).thenReturn(mockPayment);

        webTestClient.post()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.paymentId").isEqualTo("payment123");

        verify(paymentService, times(1)).addPayment(request);
    }

    @Test
    void updatePayment_ReturnsUpdatedPayment() {
        PaymentRequestModel updateRequest = new PaymentRequestModel(
                new BigDecimal("59.99"),
                Currency.EUR,
                PaymentType.PAYPAL,
                TransactionStatus.COMPLETED
        );

        when(paymentService.updatePayment(updateRequest, "payment123")).thenReturn(mockPayment);

        webTestClient.put()
                .uri(BASE_URL + "/payment123")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.paymentId").isEqualTo("payment123");

        verify(paymentService).updatePayment(updateRequest, "payment123");
    }

    @Test
    void deletePayment_ReturnsNoContent() {
        doNothing().when(paymentService).removePayment("payment123");

        webTestClient.delete()
                .uri(BASE_URL + "/payment123")
                .exchange()
                .expectStatus().isNoContent();

        verify(paymentService).removePayment("payment123");
    }
}
