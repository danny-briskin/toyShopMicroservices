package ua.com.univerpulse;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * @author Danny Briskin (DBriskin@qaconsultants.com)
 * for demo project.
 */
@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(@NotNull AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("discUser")
                .password("{noop}discPassword").roles("SYSTEM");
    }

    @Override
    protected void configure(@NotNull HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and().requestMatchers().antMatchers("/eureka/**")
                .and().authorizeRequests().antMatchers("/eureka/**")
                .hasRole("SYSTEM").anyRequest().denyAll().and()
                .httpBasic().and().csrf().disable();
    }
}
