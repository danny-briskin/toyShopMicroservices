package ua.com.univerpulse.toyshop.model.services;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.univerpulse.toyshop.exceptions.CustomerNotFoundException;
import ua.com.univerpulse.toyshop.exceptions.DateParseException;
import ua.com.univerpulse.toyshop.exceptions.PaymentNotFoundException;
import ua.com.univerpulse.toyshop.model.dto.PaymentDto;
import ua.com.univerpulse.toyshop.model.entities.Customer;
import ua.com.univerpulse.toyshop.model.entities.Payment;
import ua.com.univerpulse.toyshop.model.repositories.CustomerRepository;
import ua.com.univerpulse.toyshop.model.repositories.PaymentRepository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 */
@Service(value = "paymentService")
public class PaymentServiceImpl extends AbstractService implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;

    private static final String CUSTOMER_ID = "Customer with id ";
    private static final String NOT_FOUND = " not found";

    @Contract(pure = true)
    public PaymentServiceImpl(@Autowired PaymentRepository paymentRepository
            , @Autowired CustomerRepository customerRepository) {
        this.paymentRepository = paymentRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public int deleteAllPaymentsOfCustomerId(Integer customerId) throws CustomerNotFoundException {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isPresent()) {
            return paymentRepository.deleteAllByCustomer(customer.get());
        } else {
            throw new CustomerNotFoundException(CUSTOMER_ID + customerId + NOT_FOUND);
        }

    }

    @Override
    @Transactional
    public Payment updatePayment(Integer paymentId, @NotNull PaymentDto paymentDto)
            throws CustomerNotFoundException, PaymentNotFoundException, DateParseException {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("" + paymentId));
        if (paymentDto.getCustomerId() != null) {
            Customer customer = customerRepository.findById(paymentDto.getCustomerId())
                    .orElseThrow(() -> new CustomerNotFoundException(""
                            + paymentDto.getCustomerId()));
            payment.setCustomer(customer);
        }
        if (paymentDto.getPaymentAmount() != null) {
            payment.setPaymentAmount(paymentDto.getPaymentAmount());
        }
        if (paymentDto.getPaymentDate() != null) {
            payment.setPaymentDate(super.tryToParseStringDate(
                    paymentDto.getPaymentDate()));
        }
        if (paymentDto.getChannel() != null) {
            payment.setChannel(paymentDto.getChannel());
        }
        paymentRepository.save(payment);
        return payment;
    }

    @Override
    @Transactional
    public void deletePayment(Integer paymentId) throws PaymentNotFoundException {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("" + paymentId));
        paymentRepository.deleteById(payment.getPaymentId());
    }

    @Override
    @Transactional
    public Payment makePayment(@NotNull PaymentDto paymentDto) throws CustomerNotFoundException {
        if (paymentDto.getCustomerId() == null) {
            throw new CustomerNotFoundException(CUSTOMER_ID + paymentDto.getCustomerId()
                    + NOT_FOUND);
        }
        Optional<Customer> customer = customerRepository.findById(paymentDto.getCustomerId());
        if (customer.isPresent()) {
            Payment payment = new Payment();
            payment.setPaymentAmount(paymentDto.getPaymentAmount());
            payment.setPaymentDate(LocalDateTime.now());
            payment.setChannel(paymentDto.getChannel());
            payment.setCustomer(customer.get());
            paymentRepository.save(payment);
            return payment;
        } else {
            throw new CustomerNotFoundException(CUSTOMER_ID + paymentDto.getCustomerId()
                    + NOT_FOUND);
        }
    }

    @Override
    public Payment findById(Integer id) throws PaymentNotFoundException {
        Optional<Payment> paym = this.paymentRepository.findById(id);
        if (paym.isPresent()) {
            return paym.get();
        } else {
            throw new PaymentNotFoundException("id " + id);
        }
    }
}
