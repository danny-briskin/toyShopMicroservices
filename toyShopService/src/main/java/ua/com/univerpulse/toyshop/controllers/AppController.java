package ua.com.univerpulse.toyshop.controllers;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.com.univerpulse.toyshop.config.ResponseWrapper;
import ua.com.univerpulse.toyshop.exceptions.CustomerNotFoundException;
import ua.com.univerpulse.toyshop.exceptions.DateParseException;
import ua.com.univerpulse.toyshop.exceptions.PaymentNotFoundException;
import ua.com.univerpulse.toyshop.model.dto.CustomerCompleteDto;
import ua.com.univerpulse.toyshop.model.dto.CustomerData;
import ua.com.univerpulse.toyshop.model.dto.CustomerDto;
import ua.com.univerpulse.toyshop.model.dto.PaymentDto;
import ua.com.univerpulse.toyshop.model.entities.Customer;
import ua.com.univerpulse.toyshop.model.entities.Payment;
import ua.com.univerpulse.toyshop.model.services.CustomerService;
import ua.com.univerpulse.toyshop.model.services.PaymentService;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 */
@RestController
@Log4j2
public class AppController {
    private final CustomerService customerService;
    private final PaymentService paymentService;
    private final Environment environment;

    private final CustomerAdditionalInfoClient customerAdditionalInfoClient;

    private static final String NOT_FOUND = "] was not found";
    private static final String CUSTOMER = "Customer with ID [";
    private static final String DELETED = "] was deleted";

    @Contract(pure = true)
    public AppController(@Autowired CustomerService customerService
            , @Autowired PaymentService paymentService
            , @Autowired Environment environment
            , CustomerAdditionalInfoClient customerAdditionalInfoClient) {
        this.customerService = customerService;
        this.paymentService = paymentService;
        this.environment = environment;
        this.customerAdditionalInfoClient = customerAdditionalInfoClient;
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

    @SneakyThrows
    @GetMapping(value = "/api/findCustomerComplete/{id}"
            , headers = {"Accept=application/json"})
    public ResponseEntity<CustomerCompleteDto> findCustomerComplete(@PathVariable Integer id, Principal principal) {
        OAuth2Authentication authentication = (OAuth2Authentication) principal;
        Customer customer = customerService.findById(id);
        CustomerData custData =customerAdditionalInfoClient.getCustomerAdditionalInfo(id);
        log.warn(custData);
        CustomerCompleteDto customerDto = new CustomerCompleteDto(customer, custData);
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

    /**
     * 2.1 Create payment
     *
     * @param paymentDto json like  {
     *                   "customerId":68
     *                   ,"paymentAmount":23.45
     *                   ,"channel" : "PostMan 68"
     *                   }
     * @return ResponseEntity PaymentDto
     */
    @PostMapping(value = "/api/paymentCreate", headers = {"Accept=application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<PaymentDto> createPayment(@RequestBody PaymentDto paymentDto) {
        try {
            Payment payment = paymentService.makePayment(paymentDto);
            return new ResponseEntity<>(new PaymentDto(payment), HttpStatus.CREATED);
        } catch (CustomerNotFoundException customerNotFoundException) {
            log.catching(customerNotFoundException);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(paymentDto);
        }
    }

    /**
     * 3.1 Update payment by id
     *
     * @param id         payment ID
     * @param paymentDto data to update. json like  {
     *                   "customerId":2
     *                   ,"paymentAmount":45.67
     *                   ,"channel" : "PostMan A"
     *                   }
     * @return ResponseEntity
     */
    @PutMapping(value = "/api/paymentUpdate/{id}", headers = {"Accept=application/json"})
    public ResponseEntity<PaymentDto> updatePayment(@PathVariable("id") Integer id
            , @RequestBody PaymentDto paymentDto) {
        try {
            Payment newPayment = paymentService.updatePayment(id, paymentDto);
            return ResponseEntity.status(HttpStatus.OK).body(new PaymentDto(newPayment));
        } catch (CustomerNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Customer of payment ID [" + id + NOT_FOUND, e);
        } catch (PaymentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Payment ID [" + id + NOT_FOUND, e);
        } catch (DateParseException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_IMPLEMENTED, "Payment Date [" + paymentDto.getPaymentDate()
                    + " format is not supported", e);
        }
    }

    /**
     * 4.1 Delete payment by id
     *
     * @param id id of payment
     * @return "Payment " + id + " deleted"
     */
    @DeleteMapping(value = "/api/paymentDelete/{id}")
    public ResponseEntity<String> deletePaymentById(@PathVariable("id") Integer id) {
        try {
            paymentService.deletePayment(id);
            return new ResponseEntity<>("{\"paymentId\": " + id
                    + ",\"status\":\"deleted\"}", HttpStatus.OK);
        } catch (PaymentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Payment ID [" + id + NOT_FOUND, e);
        }
    }

    @GetMapping(value = "/api/findPayment/{id}"
            , headers = {"Accept=application/json"})
    public ResponseWrapper<PaymentDto> findPayment(@PathVariable Integer id) {
        try {
            Payment payment = paymentService.findById(id);
            return new ResponseWrapper<>(
                    environment, new PaymentDto(payment));
//            return ResponseEntity.status(HttpStatus.OK)
//                    .body(new PaymentDto(payment));
        } catch (PaymentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Payment ID [" + id + NOT_FOUND, e);
        }
    }
}