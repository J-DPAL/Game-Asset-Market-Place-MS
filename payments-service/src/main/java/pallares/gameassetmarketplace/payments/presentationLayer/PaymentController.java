package pallares.gameassetmarketplace.payments.presentationLayer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pallares.gameassetmarketplace.payments.businessLayer.PaymentService;
import pallares.gameassetmarketplace.payments.utils.exceptions.InvalidInputException;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private static final int UUID_LENGTH = 36;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping()
    public ResponseEntity<List<PaymentResponseModel>> getAllPayments() {
        return ResponseEntity.ok().body(paymentService.getAllPayments());
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponseModel> getPaymentByPaymentId(@PathVariable String paymentId) {
        if (paymentId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid paymentIdentifier provided: " + paymentId);
        }
        return ResponseEntity.ok().body(paymentService.getPaymentByPaymentId(paymentId));
    }

    @PostMapping()
    public ResponseEntity<PaymentResponseModel> addUser(@RequestBody PaymentRequestModel paymentRequestModel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.addPayment(paymentRequestModel));
    }

    @PutMapping("/{paymentId}")
    public ResponseEntity<PaymentResponseModel> updateUser(@RequestBody PaymentRequestModel paymentRequestModel, @PathVariable String paymentId) {
        if (paymentId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid paymentIdentifier provided: " + paymentId);
        }
        return ResponseEntity.ok().body(paymentService.updatePayment(paymentRequestModel, paymentId));
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String paymentId) {
        if (paymentId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid paymentIdentifier provided: " + paymentId);
        }
       paymentService.removePayment(paymentId);
       return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
