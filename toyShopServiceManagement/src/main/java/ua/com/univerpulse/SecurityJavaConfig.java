package ua.com.univerpulse;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityJavaConfig extends WebSecurityConfigurerAdapter {
    private static final String ADMIN = "ADMIN";


    @Autowired
    public void configureGlobal(@NotNull AuthenticationManagerBuilder auth)
            throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("{noop}user").roles("USER");
        auth.inMemoryAuthentication()
                .withUser("admin").password("{noop}admin").roles(ADMIN);
    }

    @Override
    protected void configure(@NotNull HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .exceptionHandling()
                .and()
                .authorizeRequests()
                .antMatchers("/api/explorer/**").permitAll()
                .antMatchers("/api/allPayments", "/api/payments/**"
                        , "/api/customerPayments/**", "/api/allCustomers", "/api/getCustomer/**"
                        , "/api/findCustomer/**")
                .hasAnyRole("USER", ADMIN)
                .antMatchers("/api/paymentCreate", "/api/paymentUpdate/**"
                        , "/api/paymentDelete/**", "/api/createCustomer", "/api/deleteCustomer/**")
                .hasRole(ADMIN)
                .antMatchers("/*").permitAll()
                .and().httpBasic().realmName("Univerpulse")
                .and()
                .formLogin()
                .failureHandler(new SimpleUrlAuthenticationFailureHandler())
                .and()
                .logout()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    /* To allow Pre-flight [OPTIONS] request from browser */
    @Override
    public void configure(@NotNull WebSecurity web) {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }
}