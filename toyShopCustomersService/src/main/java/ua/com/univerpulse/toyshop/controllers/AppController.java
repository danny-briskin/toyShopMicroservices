package ua.com.univerpulse.toyshop.controllers;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.com.univerpulse.toyshop.exceptions.CustomerNotFoundException;
import ua.com.univerpulse.toyshop.exceptions.DateParseException;
import ua.com.univerpulse.toyshop.model.dto.CustomerDto;
import ua.com.univerpulse.toyshop.model.entities.Customer;
import ua.com.univerpulse.toyshop.model.services.CustomerService;

import java.security.Principal;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 */
@RestController
@Log4j2
public class AppController {
    private final CustomerService customerService;

    private static final String NOT_FOUND = "] was not found";
    private static final String CUSTOMER = "Customer with ID [";
    private static final String DELETED = "] was deleted";

    @Contract(pure = true)
    public AppController(@Autowired CustomerService customerService) {
        this.customerService = customerService;
    }


    /**
     * 5.1 Create customer
     *
     * @param customerDto json like {
     *                    "customerName": "A1A",
     *                    "billingAddress": "billingAddress 1312",
     *                    "customerBalance": 123
     *                    }
     *                    Optional fields: :
     *                    "customerActivated":"2010-01-02" -
     *                    otherwise current date
     *                    "customerDeactivated":"2010-01-02"
     *                    - otherwise null
     * @return customer
     * @see CustomerDto
     */
    @PostMapping(value = "/api/createCustomer"
            , headers = {"Accept=application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto) {
        assertNotNull(customerDto, "Customer data must not be null");
        try {
            Customer customer = customerService.createCustomer(customerDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new CustomerDto(customer));
        } catch (DateParseException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Customer dates [" + customerDto.getCustomerActivated()
                    + " or " + customerDto.getCustomerDeactivated()
                    + " formats are not supported", e);
        }
    }

    @GetMapping(value = "/api/findCustomer/{id}"
            , headers = {"Accept=application/json"})
    public ResponseEntity<CustomerDto> findCustomer(@PathVariable Integer id, Principal principal) {
        OAuth2Authentication authentication = (OAuth2Authentication) principal;
        Customer customer = customerService.findById(id);
        CustomerDto customerDto = new CustomerDto(customer);
        customerDto.setBillingAddress(authentication.getName());
        return ResponseEntity.status(HttpStatus.OK)
                .body(customerDto);
    }

    /**
     * 5.2 Delete customer
     *
     * @param customerId customerId
     * @return customerId
     */
    @DeleteMapping(value = "/api/deleteCustomer/{customerId}")
    @ResponseBody
    public ResponseEntity<String> deleteCustomer
    (@PathVariable("customerId") Integer customerId) {
        try {
            log.debug(CUSTOMER + customerService.deleteCustomer(customerId) + DELETED);
            return ResponseEntity.status(HttpStatus.OK).body(CUSTOMER
                    + customerId + DELETED);
        } catch (CustomerNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, CUSTOMER + customerId + NOT_FOUND, e);
        }
    }
}