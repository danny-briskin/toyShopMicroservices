package ua.com.univerpulse.toyshop.model.services;

import ua.com.univerpulse.toyshop.exceptions.CustomerNotFoundException;
import ua.com.univerpulse.toyshop.exceptions.DateParseException;
import ua.com.univerpulse.toyshop.exceptions.PaymentNotFoundException;
import ua.com.univerpulse.toyshop.model.dto.PaymentDto;
import ua.com.univerpulse.toyshop.model.entities.Payment;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 */
public interface PaymentService {
    int deleteAllPaymentsOfCustomerId(Integer customerId) throws CustomerNotFoundException;

    Payment updatePayment(Integer paymentId, PaymentDto paymentDto)
            throws CustomerNotFoundException, PaymentNotFoundException, DateParseException;

    void deletePayment(Integer paymentId) throws PaymentNotFoundException;

    Payment makePayment(PaymentDto paymentDto) throws CustomerNotFoundException;

    Payment findById(Integer id) throws PaymentNotFoundException;
}
