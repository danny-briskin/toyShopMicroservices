package ua.com.univerpulse.toyshop.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author Danny Briskin (DBriskin@qaconsultants.com)
 * for toyshopmicroservices project.
 */
@Data
@Builder
public class CustomerData {
    private String addressLine1;
    private String addressLine2;
    private String legalName;
}
