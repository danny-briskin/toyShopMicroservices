package ua.com.univerpulse.toyshop.model.entities.projections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import ua.com.univerpulse.toyshop.model.entities.Customer;

/**
 * @author Danny Briskin (DBriskin@qaconsultants.com)
 * for toyshop project.
 */
@Projection(name = "customerDto", types = {Customer.class})
public interface CustomerProjection {
    Integer getId();

    String getCustomerName();

    String getBillingAddress();

    Double getCustomerBalance();

    @Value("#{target.getCustomerActivated().format(T(java.time.format.DateTimeFormatter).ISO_DATE)}")
    String getCustomerActivated();

    @Value("#{target.getCustomerDeactivated() ==null? '':target.getCustomerDeactivated().format(T(java.time.format.DateTimeFormatter).ISO_DATE)}")
    String getCustomerDeactivated();

}
