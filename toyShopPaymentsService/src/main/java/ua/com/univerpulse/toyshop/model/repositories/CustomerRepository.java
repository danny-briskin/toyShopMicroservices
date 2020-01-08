package ua.com.univerpulse.toyshop.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ua.com.univerpulse.toyshop.model.entities.Customer;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 */

public interface CustomerRepository extends JpaRepository<Customer, Integer>
        , JpaSpecificationExecutor<Customer> {

}
