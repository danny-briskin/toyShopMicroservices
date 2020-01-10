package ua.com.univerpulse.toyshop.config.security;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityJavaConfig extends WebSecurityConfigurerAdapter {
    private static final String ADMIN = "ADMIN";
    private UserDetailsService userDetailsService;

    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private JwtRequestFilter jwtRequestFilter;

//    private TokenProvider tokenProvider;

//    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    public SecurityJavaConfig(
//            @Autowired RestAuthenticationEntryPoint restAuthenticationEntryPoint,
//            @Autowired TokenProvider tokenProvider
            @Autowired JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint
            , @Autowired UserDetailsService userDetailsService
            , @Autowired JwtRequestFilter jwtRequestFilter
    ) {
//        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
//        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Autowired
    public void configureGlobal(@NotNull AuthenticationManagerBuilder auth) throws Exception {
// configure AuthenticationManager so that it knows from where to load
// user for matching credentials
// Use BCryptPasswordEncoder
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder())
        ;
    }
//    @Autowired
//    public void configureGlobal(@NotNull AuthenticationManagerBuilder auth)
//            throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("user").password("{noop}user").roles("USER");
//        auth.inMemoryAuthentication()
//                .withUser("admin").password("{noop}admin").roles(ADMIN);
//    }

    @Override
    protected void configure(@NotNull HttpSecurity http) throws Exception {
        http
                .csrf()
                //.and().cors()
                .disable()
//                .exceptionHandling()
//                .authenticationEntryPoint(restAuthenticationEntryPoint)
//                .and()
//                .httpBasic() // optional, if you want to access
//                .and()
                .authorizeRequests()
                .antMatchers("/api/explorer/**", "/authenticate/**"
                        , "/h2-console/**")
                .permitAll()
                .antMatchers("/api/allPayments", "/api/payments/**"
                        , "/api/customerPayments/**", "/api/allCustomers", "/api/getCustomer/**"
                        , "/api/findCustomer/**")
                .hasAnyRole("USER", ADMIN)
                .antMatchers("/api/paymentCreate", "/api/paymentUpdate/**"
                        , "/api/paymentDelete/**", "/api/createCustomer", "/api/deleteCustomer/**")
                .hasRole(ADMIN)
//                .antMatchers("/*").permitAll()
                //.and().httpBasic().realmName("Univerpulse")
//                .and()
//                .formLogin()
//                .failureHandler(new SimpleUrlAuthenticationFailureHandler())
//                .and()
//                .logout()
                .anyRequest().authenticated().and().exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        ;
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        http.headers().frameOptions().sameOrigin();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
//
//
//    /* To allow Pre-flight [OPTIONS] request from browser */
//    @Override
//    public void configure(@NotNull WebSecurity web) {
//        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
//    }
}