package ua.com.univerpulse.toyshop.controllers;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.RandomStringUtils;
import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
    private final Environment environment;

    @Contract(pure = true)
    public AppController(@Autowired Environment environment) {
        this.environment = environment;
    }

    @GetMapping(value = "/api/customer/info/{id}"
            , headers = {"Accept=application/json"})
    public ResponseEntity<CustomerData> findAditionalCustomerInfo(@PathVariable Integer id) {


        CustomerData customerData = CustomerData.builder()
                .addressLine1(RandomStringUtils.randomNumeric(2) + " "
                        + Utilities.createName(7) + " str.")
                .addressLine2("unit " + RandomStringUtils.randomNumeric(2))
                .legalName(Utilities.createName(5) + " " + Utilities.createName(7))
                .build();
        log.warn("Customer " + id + " " + customerData);
        return ResponseEntity.status(HttpStatus.OK)
                .body(customerData);
    }
}