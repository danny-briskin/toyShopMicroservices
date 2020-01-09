package ua.com.univerpulse.toyshop.model.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import ua.com.univerpulse.toyshop.model.entities.Customer;
import ua.com.univerpulse.toyshop.model.entities.Payment;
import ua.com.univerpulse.toyshop.model.entities.projections.PaymentProjection;

import java.util.List;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 */
@RepositoryRestResource(collectionResourceRel = "payments", path = "payments"
        , excerptProjection = PaymentProjection.class)
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public interface PaymentRepository extends PagingAndSortingRepository<Payment, Integer> {
    int deleteAllByCustomer(Customer customer);

    List<Payment> findAllByCustomerId(Integer id);
}



