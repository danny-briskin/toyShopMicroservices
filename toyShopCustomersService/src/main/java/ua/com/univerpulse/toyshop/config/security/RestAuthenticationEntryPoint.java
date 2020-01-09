package ua.com.univerpulse.toyshop.config.security;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 */
@Component
@Log4j2
public class RestAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, @NotNull HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "!Unauthorized!");
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName("Univerpulse");
        super.afterPropertiesSet();
    }
}
