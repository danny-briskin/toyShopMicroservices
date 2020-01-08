package ua.com.univerpulse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
@EnableOAuth2Sso
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
 public class ToyShopServiceManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToyShopServiceManagementApplication.class, args);
	}

}
