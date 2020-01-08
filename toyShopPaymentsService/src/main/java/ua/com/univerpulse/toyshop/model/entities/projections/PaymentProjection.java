package ua.com.univerpulse.toyshop.model.entities.projections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import ua.com.univerpulse.toyshop.model.entities.Payment;

/**
 * @author Danny Briskin (DBriskin@qaconsultants.com)
 * for toyshop project.
 */
@Projection(name = "paymentDto", types = {Payment.class})
public interface PaymentProjection {
    @Value("#{target.getCustomer().getId()}")
    Integer getCustomerId();

    Double getPaymentAmount();

    String getChannel();

    @Value("#{target.getPaymentDate().format(T(java.time.format.DateTimeFormatter).ISO_DATE)}")
    String getPaymentDate();

    Integer getPaymentId();
}
