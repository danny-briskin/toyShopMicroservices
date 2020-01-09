package ua.com.univerpulse.toyshop.model.repositories;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Danny Briskin (DBriskin@qaconsultants.com)
 * for toyshopmicros project.
 */
@FeignClient(name = "PAYMENTS-SERVICE", configuration = FooConfiguration.class)
public interface PaymentClient {
    @GetMapping("/api/customerPayments/{customerId}")
    Object getPaymentsForCustomer(@PathVariable(value = "customerId") int customerId);
}

@Configuration
class FooConfiguration {

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor("admin", "admin");
    }
}