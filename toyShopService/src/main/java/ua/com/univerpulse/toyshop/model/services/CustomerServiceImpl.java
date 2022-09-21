package ua.com.univerpulse.toyshop.model.services;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.univerpulse.toyshop.exceptions.CustomerNotFoundException;
import ua.com.univerpulse.toyshop.exceptions.DateParseException;
import ua.com.univerpulse.toyshop.model.dto.CustomerData;
import ua.com.univerpulse.toyshop.model.dto.CustomerDto;
import ua.com.univerpulse.toyshop.model.entities.Customer;
import ua.com.univerpulse.toyshop.model.repositories.CustomerRepository;
import ua.com.univerpulse.toyshop.model.repositories.PaymentRepository;
import ua.com.univerpulse.toyshop.rest.RestRequest;
import ua.com.univerpulse.toyshop.rest.RestResponse;
import ua.com.univerpulse.toyshop.rest.RestWebserviceActions;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 */
@Service(value = "customerService")
@Log4j2
public class CustomerServiceImpl extends AbstractService implements CustomerService {
    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;

    private final RestWebserviceActions restWebserviceActions;

    @Contract(pure = true)
    public CustomerServiceImpl(@Autowired CustomerRepository customerRepository
            , @Autowired PaymentRepository paymentRepository
            , RestWebserviceActions restWebserviceActions) {
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
        this.restWebserviceActions = restWebserviceActions;
    }

    @Override
    @Transactional
    public Customer createCustomer(@NotNull CustomerDto customerDto) throws DateParseException {
        Customer customer = new Customer();
        customer.setCustomerName(customerDto.getCustomerName());
        customer.setBillingAddress(customerDto.getBillingAddress());
        customer.setCustomerBalance(customerDto.getCustomerBalance());
        LocalDateTime customerActivated;
        LocalDateTime customerDeactivated;
        if (customerDto.getCustomerActivated() == null) {
            customerActivated = LocalDateTime.now();
        } else {
            customerActivated = super.tryToParseStringDate(customerDto.
                    getCustomerActivated());
        }
        if (customerDto.getCustomerDeactivated() == null) {
            customerDeactivated = null;
        } else {
            customerDeactivated = super.tryToParseStringDate(customerDto.
                    getCustomerDeactivated());
        }
        customer.setCustomerActivated(customerActivated);
        customer.setCustomerDeactivated(customerDeactivated);

        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public int deleteCustomer(@NotNull Integer customerId) throws CustomerNotFoundException {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isPresent()) {
            log.debug("Deleted " +
                    paymentRepository.deleteAllByCustomer(customer.get())
                    + " payments");
            customerRepository.delete(customer.get());
            log.debug("Deleted " + customerId + " customerID");
            return customerId;
        } else {
            throw new CustomerNotFoundException("Customer with id " + customerId + " not found");
        }
    }

    @Override
    @Nullable
    public Customer findById(Integer id) {
        return this.customerRepository.findById(id).orElse(null);
    }

    @SneakyThrows
    @Override
    public CustomerData getAdditionalCustomerInfo(Integer id) {
        RestRequest restRequest = new RestRequest("http://localhost:7080/api/customer/info/1"
                , "GET", 200, id + "");


//        restRequest.addHeader(HttpHeaders.AUTHORIZATION,"token");
        @Nullable RestResponse response =
                restWebserviceActions.sendRestRequestTo(restRequest, true);
        return restWebserviceActions.parseRestResponse(response.getJson(), CustomerData.class);
    }
}
