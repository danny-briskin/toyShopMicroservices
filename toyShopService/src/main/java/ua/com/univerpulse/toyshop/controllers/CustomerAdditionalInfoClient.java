package ua.com.univerpulse.toyshop.controllers;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ua.com.univerpulse.toyshop.model.dto.CustomerData;

/**
 * @author Danny Briskin (DBriskin@qaconsultants.com)
 * for toyshopmicroservices project.
 */
@FeignClient("CUSTOMER-ADDITIONAL-SERVICE")
public interface CustomerAdditionalInfoClient {
    @GetMapping("/api/customer/info/{id}")
    CustomerData getCustomerAdditionalInfo(@PathVariable Integer id);
}
