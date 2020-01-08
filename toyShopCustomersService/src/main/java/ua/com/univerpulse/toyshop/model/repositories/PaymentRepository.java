package ua.com.univerpulse.toyshop.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.univerpulse.toyshop.model.entities.Customer;
import ua.com.univerpulse.toyshop.model.entities.Payment;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 */
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    int deleteAllByCustomer(Customer customer);
}



