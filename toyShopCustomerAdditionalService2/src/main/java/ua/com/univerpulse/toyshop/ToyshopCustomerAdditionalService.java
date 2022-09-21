package ua.com.univerpulse.toyshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ToyshopCustomerAdditionalService {
    public static void main(String[] args) {
        SpringApplication.run(ToyshopCustomerAdditionalService.class, args);
    }
}
