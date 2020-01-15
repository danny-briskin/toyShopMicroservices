package ua.com.univerpulse.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.client.RestTemplate;

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
//                .csrf().disable()
//                // make sure we use stateless session; session won't be used to store user's state.
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                // handle an authorized attempts
////                .exceptionHandling().authenticationEntryPoint((req, rsp, e)
////                -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
////                .and()
//                // Add a filter to validate the tokens with every request
////                .addFilterAfter(new JwtUsernameAndPasswordAuthenticationFilter
////                                (authenticationManager, jwtConfig, restTemplate)
////                        , UsernamePasswordAuthenticationFilter.class)
//                // authorization requests config
//                .authorizeRequests()
//                // allow all who are accessing "auth" service
////                .antMatchers(jwtConfig.getUri()).permitAll()
//                // must be an admin if trying to access admin area (authentication is also required here)
//                .antMatchers("/cust/**", "/pays/**", "/auth/**")
//                .permitAll()
//                //.hasRole("USER")
//                // Any other request must be authenticated
//                .anyRequest().authenticated()
//                .and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
//                .and().httpBasic()
//                .and().formLogin().loginPage("/login")
//        ;
                .authorizeRequests()
                .antMatchers("/cust/**", "/pays/**", "/auth/**")
//                .permitAll()
                .hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .and().addFilter(new JwtUsernameAndPasswordAuthenticationFilter
                        (super.authenticationManager(), jwtConfig, restTemplate)
                )
        ;
    }

    @Bean
    public JwtConfig jwtConfig() {
        return new JwtConfig();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}