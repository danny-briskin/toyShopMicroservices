package ua.com.univerpulse.toyshop.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import ua.com.univerpulse.toyshop.model.entities.Payment;
import ua.com.univerpulse.toyshop.model.entities.projections.PaymentProjection;

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
        "customerId",
        "paymentAmount",
        "channel",
        "paymentDate",
        "paymentId"
})
public class PaymentDto implements PaymentProjection {
    @JsonProperty("customerId")
    private Integer customerId;
    @JsonProperty("paymentAmount")
    private Double paymentAmount;
    @JsonProperty("channel")
    private String channel;
    @JsonProperty(value = "paymentDate")
    private String paymentDate;
    @JsonProperty("paymentId")
    private Integer paymentId;

    public PaymentDto(@NotNull Payment payment) {
        this.customerId = payment.getCustomer().getId();
        this.channel = payment.getChannel();
        this.paymentAmount = payment.getPaymentAmount();
        this.paymentDate = payment.getPaymentDate().format(DateTimeFormatter.ISO_DATE);
        this.paymentId = payment.getPaymentId();
    }
}
