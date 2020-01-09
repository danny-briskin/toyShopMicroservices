package ua.com.univerpulse.toyshop.controllers;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.com.univerpulse.toyshop.config.ResponseWrapper;
import ua.com.univerpulse.toyshop.exceptions.CustomerNotFoundException;
import ua.com.univerpulse.toyshop.exceptions.DateParseException;
import ua.com.univerpulse.toyshop.exceptions.PaymentNotFoundException;
import ua.com.univerpulse.toyshop.model.dto.PaymentDto;
import ua.com.univerpulse.toyshop.model.entities.Payment;
import ua.com.univerpulse.toyshop.model.services.PaymentService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 */
@RestController
@Log4j2
public class AppController {
    private final PaymentService paymentService;
    private final Environment environment;

    private static final String NOT_FOUND = "] was not found";

    @Contract(pure = true)
    public AppController(@Autowired PaymentService paymentService
            , @Autowired Environment environment) {
        this.paymentService = paymentService;
        this.environment = environment;
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
        } catch (PaymentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Payment ID [" + id + NOT_FOUND, e);
        }
    }

    @GetMapping(value = "/api/customerPayments/{id}"
            , headers = {"Accept=application/json"})
    public ResponseWrapper<List<PaymentDto>> findCustomerPayment(@PathVariable Integer id) {
        try {
            List<PaymentDto> payments = paymentService.findByCustomerId(id).stream()
                    .map(PaymentDto::new)
                    .collect(Collectors.toList());
            return new ResponseWrapper<>(environment, payments);
        } catch (PaymentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Payment of customer ID [" + id + NOT_FOUND, e);
        }
    }
}