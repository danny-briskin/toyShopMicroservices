package ua.com.univerpulse.toyshop.controllers;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ua.com.univerpulse.toyshop.model.CustomerData;
import ua.com.univerpulse.toyshop.util.Utilities;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 */
@RestController
@Log4j2
public class AppController {

    @GetMapping(value = "/api/customer/info/{id}"
            , headers = {"Accept=application/json"})
    public ResponseEntity<CustomerData> findAditionalCustomerInfo(@PathVariable Integer id) {
        CustomerData customerData = CustomerData.builder()
                .addressLine1(RandomStringUtils.randomNumeric(2).toUpperCase() + " "
                        + Utilities.createName(7).toUpperCase() + " STR.")
                .addressLine2("UNIT " + RandomStringUtils.randomNumeric(2).toUpperCase())
                .legalName(Utilities.createName(5).toUpperCase()
                        + " " + Utilities.createName(7).toUpperCase())
                .build();
        log.warn("Customer " + id + " " + customerData);
        return ResponseEntity.status(HttpStatus.OK).body(customerData);
    }
}