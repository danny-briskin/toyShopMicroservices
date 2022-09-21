package ua.com.univerpulse.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ua.com.univerpulse.model.CustomerData;

/**
 * @author Danny Briskin (DBriskin@qaconsultants.com)
 * for toyshopmicroservices project.
 */
@Controller
@Log4j2
public class AppController {
    @Autowired
    private CustomerAdditionalInfoClient customerAdditionalInfoClient;

    @GetMapping(value = "/customer/{id}"
            , headers = {"Accept=application/json"})
    public ResponseEntity<CustomerData> findAdditionalCustomerInfo(@PathVariable Integer id) {
        CustomerData customerData = customerAdditionalInfoClient.getCustomerAdditionalInfo(id);
        log.warn("Customer " + id + " " + customerData);
        return ResponseEntity.status(HttpStatus.OK).body(customerData);
    }
}
