package ua.com.univerpulse;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
//@EnableDiscoveryClient
//@EnableCircuitBreaker
//@EnableOAuth2Sso
public class ToyShopServiceManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(ToyShopServiceManagementApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(@NotNull RestTemplateBuilder builder) {
        return builder.build();
    }
}
