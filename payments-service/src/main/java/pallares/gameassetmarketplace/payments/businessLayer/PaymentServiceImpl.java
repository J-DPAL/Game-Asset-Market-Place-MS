package pallares.gameassetmarketplace.payments.businessLayer;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pallares.gameassetmarketplace.payments.presentationLayer.PaymentRequestModel;
import pallares.gameassetmarketplace.payments.presentationLayer.PaymentResponseModel;
import pallares.gameassetmarketplace.payments.dataAccessLayer.*;
import pallares.gameassetmarketplace.payments.utils.exceptions.InvalidTransactionAmountException;
import pallares.gameassetmarketplace.payments.utils.exceptions.NotFoundException;
import pallares.gameassetmarketplace.payments.mappingLayer.PaymentRequestMapper;
import pallares.gameassetmarketplace.payments.mappingLayer.PaymentResponseMapper;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentResponseMapper paymentResponseMapper;
    private final PaymentRequestMapper paymentRequestMapper;


    public PaymentServiceImpl(PaymentRepository paymentRepository, PaymentResponseMapper paymentResponseMapper, PaymentRequestMapper paymentRequestMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentResponseMapper = paymentResponseMapper;
        this.paymentRequestMapper = paymentRequestMapper;
    }

    @Override
    public List<PaymentResponseModel> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return paymentResponseMapper.entityListToResponseModelList(payments);
    }

    @Override
    public PaymentResponseModel addPayment(PaymentRequestModel paymentRequestModel) {

        Payment payment = paymentRequestMapper.requestModelToEntity(paymentRequestModel, new PaymentIdentifier());
        if (payment.getItemPrice().getPrice() == null ||
                payment.getItemPrice().getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidTransactionAmountException("Provided price is invalid: " + payment.getItemPrice().getPrice());
        }
        Payment savedPayment = paymentRepository.save(payment);

        return paymentResponseMapper.entityToResponseModel(savedPayment);
    }

    @Override
    public PaymentResponseModel updatePayment(PaymentRequestModel paymentRequestModel, String paymentId) {

        Payment existingPayment = paymentRepository.findByPaymentIdentifier_PaymentId(paymentId);

        if (existingPayment == null) {
            throw new NotFoundException("Provided paymentIdentifier not found: " + paymentId);
        }

        if (existingPayment.getItemPrice().getPrice() == null ||
                existingPayment.getItemPrice().getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidTransactionAmountException("Provided price is invalid: " + existingPayment.getItemPrice().getPrice());
        }


        Payment updatedPayment = paymentRequestMapper.requestModelToEntity(paymentRequestModel,
            existingPayment.getPaymentIdentifier());
        updatedPayment.setId(existingPayment.getId());

        Payment savedPayment = paymentRepository.save(updatedPayment);

        return paymentResponseMapper.entityToResponseModel(savedPayment);
    }

    @Override
    public void removePayment(String paymentId) {
        Payment existingPayment = paymentRepository.findByPaymentIdentifier_PaymentId(paymentId);

        if (existingPayment == null) {
            throw new NotFoundException("Provided paymentIdentifier not found: " + paymentId);
        }

        paymentRepository.delete(existingPayment);
    }

    @Override
    public PaymentResponseModel getPaymentByPaymentId(String paymentId) {
        Payment payment = paymentRepository.findByPaymentIdentifier_PaymentId(paymentId);

        if (payment == null) {
            throw new NotFoundException("Provided paymentIdentifier not found: " + paymentId);
        }
        return paymentResponseMapper.entityToResponseModel(payment);
    }
}
