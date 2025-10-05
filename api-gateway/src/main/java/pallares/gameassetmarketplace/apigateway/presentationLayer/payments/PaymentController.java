package pallares.gameassetmarketplace.apigateway.presentationLayer.payments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pallares.gameassetmarketplace.apigateway.businessLayer.payments.PaymentService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // GET /api/v1/payments/{paymentId}
    @GetMapping(value = "/{paymentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentResponseModel> getPaymentByPaymentId(@PathVariable String paymentId) {
        log.debug("1. Request Received in API-Gateway Payments Controller: getPaymentByPaymentId");
        return ResponseEntity.ok(paymentService.getPaymentByPaymentId(paymentId));
    }

    // GET /api/v1/payments
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PaymentResponseModel>> getAllPayments() {
        log.debug("2. Request Received in API-Gateway Payments Controller: getAllPayments");
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    // POST /api/v1/payments
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentResponseModel> addPayment(@RequestBody PaymentRequestModel paymentRequestModel) {
        log.debug("3. Request Received in API-Gateway Payments Controller: addPayment");
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.addPayment(paymentRequestModel));
    }

    // PUT /api/v1/payments/{paymentId}
    @PutMapping(value = "/{paymentId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentResponseModel> updatePayment(@PathVariable String paymentId,
                                                                  @RequestBody PaymentRequestModel paymentRequestModel) {
        log.debug("4. Request Received in API-Gateway Payments Controller: updatePayment");
        return ResponseEntity.ok(paymentService.updatePayment(paymentRequestModel, paymentId));
    }

    // DELETE /api/v1/payments/{paymentId}
    @DeleteMapping(value = "/{paymentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentResponseModel> removePayment(@PathVariable String paymentId) {
        log.debug("5. Request Received in API-Gateway Payments Controller: removePayment");
        paymentService.removePayment(paymentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
