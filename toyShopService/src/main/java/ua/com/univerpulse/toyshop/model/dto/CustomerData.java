package ua.com.univerpulse.toyshop.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Danny Briskin (DBriskin@qaconsultants.com)
 * for toyshopmicroservices project.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "addressLine1",
        "addressLine2",
        "legalName"
})
public class CustomerData {
    @JsonProperty(value = "addressLine1")
    private String addressLine1;
    @JsonProperty(value = "addressLine2")
    private String addressLine2;
    @JsonProperty(value = "legalName")
    private String legalName;
}
