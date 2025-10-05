package pallares.gameassetmarketplace.payments.dataAccessLayer;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Payment findByPaymentIdentifier_PaymentId(String paymentId);
}