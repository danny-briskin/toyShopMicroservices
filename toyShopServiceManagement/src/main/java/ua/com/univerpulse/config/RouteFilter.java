package ua.com.univerpulse.config;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * @author Danny Briskin (DBriskin@qaconsultants.com)
 * for toyshopmicros project.
 */
@Component
@Log4j2
public class RouteFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext.getCurrentContext()
                .addZuulRequestHeader("Authorization", "Bearer "
                        + JwtUsernameAndPasswordAuthenticationFilter.TOKEN);

        return null;
    }
}