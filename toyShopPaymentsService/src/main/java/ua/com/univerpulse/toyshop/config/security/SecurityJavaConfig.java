package ua.com.univerpulse.toyshop.config.security;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityJavaConfig extends WebSecurityConfigurerAdapter {
    private static final String ADMIN = "ADMIN";

    private RestAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private JwtRequestFilter jwtRequestFilter;

    public SecurityJavaConfig(
            @Autowired RestAuthenticationEntryPoint jwtAuthenticationEntryPoint
            , @Autowired JwtRequestFilter jwtRequestFilter
    ) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Override
    protected void configure(@NotNull HttpSecurity http) throws Exception {
        http
                .csrf()
                //.and().cors()
                .disable()
                .authorizeRequests()
                .antMatchers("/api/explorer/**")
                .permitAll()
                .antMatchers("/api/allPayments", "/api/payments/**"
                        , "/api/customerPayments/**", "/api/allCustomers", "/api/getCustomer/**"
                        , "/api/findCustomer/**")
                .hasAnyRole("USER", ADMIN)
                .antMatchers("/api/paymentCreate", "/api/paymentUpdate/**"
                        , "/api/paymentDelete/**", "/api/createCustomer", "/api/deleteCustomer/**")
                .hasRole(ADMIN)
                .anyRequest().authenticated().and().exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        ;
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}