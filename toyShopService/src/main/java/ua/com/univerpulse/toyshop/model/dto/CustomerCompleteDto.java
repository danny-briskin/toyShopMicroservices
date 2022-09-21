package ua.com.univerpulse.toyshop.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import ua.com.univerpulse.toyshop.model.entities.Customer;
import ua.com.univerpulse.toyshop.model.entities.projections.CustomerProjection;

import java.time.format.DateTimeFormatter;

/**
 * @author Danny Briskin (DBriskin@qaconsultants.com)
 * for toyshop project.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "customerName",
        "billingAddress",
        "customerBalance",
        "customerId",
        "customerActivated",
        "customerDeactivated",
        "addressLine1",
        "addressLine2",
        "legalName"
})
public class CustomerCompleteDto implements CustomerProjection {
    @JsonProperty(value = "customerName", required = true)
    private String customerName;
    @JsonProperty(value = "billingAddress", required = true)
    private String billingAddress;
    @JsonProperty(value = "customerBalance", required = true)
    private Double customerBalance;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("customerActivated")
    private String customerActivated;
    @JsonProperty("customerDeactivated")
    private String customerDeactivated;
    @JsonProperty("addressLine1")
    private String addressLine1;
    @JsonProperty("addressLine2")
    private String addressLine2;
    @JsonProperty("legalName")
    private String legalName;

    public CustomerCompleteDto(@NotNull Customer customer,@NotNull CustomerData customerData) {
        this.id = customer.getId();
        this.billingAddress = customer.getBillingAddress();
        this.customerBalance = customer.getCustomerBalance();
        this.customerName = customer.getCustomerName();
        this.customerActivated = customer.getCustomerActivated()
                .format(DateTimeFormatter.ISO_DATE);
        this.customerDeactivated = customer.getCustomerDeactivated() == null
                ? ""
                : customer.getCustomerDeactivated().format(DateTimeFormatter.ISO_LOCAL_DATE);
        this.addressLine1=customerData.getAddressLine1();
        this.addressLine2=customerData.getAddressLine2();
        this.legalName=customerData.getLegalName();
    }
}
