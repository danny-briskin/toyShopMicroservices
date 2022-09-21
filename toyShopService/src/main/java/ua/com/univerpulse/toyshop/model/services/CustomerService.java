package ua.com.univerpulse.toyshop.model.services;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import ua.com.univerpulse.toyshop.exceptions.CustomerNotFoundException;
import ua.com.univerpulse.toyshop.exceptions.DateParseException;
import ua.com.univerpulse.toyshop.model.dto.CustomerData;
import ua.com.univerpulse.toyshop.model.dto.CustomerDto;
import ua.com.univerpulse.toyshop.model.entities.Customer;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 */
public interface CustomerService {
    Customer createCustomer(@NotNull CustomerDto customerDto) throws DateParseException;

    int deleteCustomer(@NotNull Integer customerId) throws CustomerNotFoundException;

    Customer findById(Integer id);

    @SneakyThrows
    CustomerData getAdditionalCustomerInfo(Integer id);
}
