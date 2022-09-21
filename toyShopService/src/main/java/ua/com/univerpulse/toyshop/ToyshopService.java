package ua.com.univerpulse.toyshop;

import feign.RequestInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import ua.com.univerpulse.toyshop.config.UserFeignClientInterceptor;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableOAuth2Sso
public class ToyshopService {

    public static void main(String[] args) {
        SpringApplication.run(ToyshopService.class, args);
    }
    @Bean
    public RequestInterceptor getUserFeignClientInterceptor() {
        return new UserFeignClientInterceptor();
    }
}
