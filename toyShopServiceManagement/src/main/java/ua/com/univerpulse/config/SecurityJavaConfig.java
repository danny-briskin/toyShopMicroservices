package ua.com.univerpulse.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 */
//@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityJavaConfig
        extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtConfig jwtConfig;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    protected void configure(@NotNull HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                // make sure we use stateless session; session won't be used to store user's state.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // handle an authorized attempts
                .exceptionHandling().authenticationEntryPoint((req, rsp, e)
                -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                // Add a filter to validate the tokens with every request
                .addFilterAfter(new JwtTokenAuthenticationFilter
                                (jwtConfig, restTemplate, passwordEncoder)
                        , UsernamePasswordAuthenticationFilter.class)
                // authorization requests config
                .authorizeRequests()
                // allow all who are accessing "auth" service
                .antMatchers(  jwtConfig.getUri()).permitAll()
                // must be an admin if trying to access admin area (authentication is also required here)
                .antMatchers("/cust/**" , "/pays/**","/auth/**")
                .permitAll()
                //.hasRole("USER")
                // Any other request must be authenticated
                .anyRequest().authenticated();
    }

    @Bean
    public JwtConfig jwtConfig() {
        return new JwtConfig();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    private static final String ADMIN = "ADMIN";
//
//
//    @Autowired
//    public void configureGlobal(@NotNull AuthenticationManagerBuilder auth)
//            throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("user").password("{noop}user").roles("USER");
//        auth.inMemoryAuthentication()
//                .withUser("admin").password("{noop}admin").roles(ADMIN);
//    }
//
//    @Override
//    protected void configure(@NotNull HttpSecurity http) throws Exception {
//        http
//                .csrf()
//                .disable()
//                .exceptionHandling()
//                .and()
//                .authorizeRequests()
//                .antMatchers("/api/explorer/**").permitAll()
//                .antMatchers("/api/allPayments", "/api/payments/**"
//                        , "/api/customerPayments/**", "/api/allCustomers", "/api/getCustomer/**"
//                        , "/api/findCustomer/**")
//                .hasAnyRole("USER", ADMIN)
//                .antMatchers("/api/paymentCreate", "/api/paymentUpdate/**"
//                        , "/api/paymentDelete/**", "/api/createCustomer", "/api/deleteCustomer/**")
//                .hasRole(ADMIN)
//                .antMatchers("/*").permitAll()
//                .and().httpBasic().realmName("Univerpulse")
//                .and()
//                .formLogin()
//                .failureHandler(new SimpleUrlAuthenticationFailureHandler())
//                .and()
//                .logout()
//                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//    }
//
//    /* To allow Pre-flight [OPTIONS] request from browser */
//    @Override
//    public void configure(@NotNull WebSecurity web) {
//        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
//    }
}