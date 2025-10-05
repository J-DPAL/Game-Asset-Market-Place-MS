package pallares.gameassetmarketplace.payments.mappingLayer;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;
import pallares.gameassetmarketplace.payments.dataAccessLayer.Payment;
import pallares.gameassetmarketplace.payments.presentationLayer.PaymentController;
import pallares.gameassetmarketplace.payments.presentationLayer.PaymentResponseModel;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring")
public interface PaymentResponseMapper {

    @Mapping(expression = "java(payment.getPaymentIdentifier().getPaymentId())", target = "paymentId")
    @Mapping(expression = "java(payment.getItemPrice().getPrice())", target = "price")
    @Mapping(expression = "java(payment.getItemPrice().getCurrency())", target = "currency")
    @Mapping(source = "payment.paymentType", target = "paymentType")
    @Mapping(source = "payment.transactionStatus", target = "transactionStatus")
    PaymentResponseModel entityToResponseModel(Payment payment);

    List<PaymentResponseModel> entityListToResponseModelList(List<Payment> payments);

    @AfterMapping
    default void addLinks(@MappingTarget PaymentResponseModel paymentResponseModel, Payment payment) {
        Link selfLink = linkTo(methodOn(PaymentController.class)
                .getPaymentByPaymentId(paymentResponseModel.getPaymentId()))
                .withSelfRel();
        paymentResponseModel.add(selfLink);

        Link allPaymentsLink = linkTo(methodOn(PaymentController.class)
                .getAllPayments())
                .withRel("payments");
        paymentResponseModel.add(allPaymentsLink);
    }
}
