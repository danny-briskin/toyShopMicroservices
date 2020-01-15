package ua.com.univerpulse.config;

import lombok.extern.log4j.Log4j2;

/**
 * @author Danny Briskin (DBriskin@qaconsultants.com)
 * for toyshopmicros project.
 */
@Log4j2
public class LogRequestFilter
//        extends ZuulFilter
{
//
//    @Override
//    public String filterType() {
//        return "pre";
//    }
//
//    @Override
//    public int filterOrder() {
//        return 2;
//    }
//
//    @Override
//    public boolean shouldFilter() {
//        return true;
//    }
//
//    @Override
//    public Object run() {
//        RequestContext ctx = RequestContext.getCurrentContext();
//        InputStream responseDataStream = ctx.getResponseDataStream();
//
//        HttpServletRequest request = new HttpServletRequestWrapper(ctx.getRequest());
//        String requestData = "";
//        String responseData = "";
//        try {
//            if (request.getContentLength() > 0) {
//                requestData+= CharStreams.toString(request.getReader());
//            }
//               responseData +=
//                    CharStreams.toString(new InputStreamReader(responseDataStream,
//                            "UTF-8"));
//        } catch (Exception e) {
//            log.error("Error parsing request", e);
//
//        }
//
//        log.warn(requestData);
//        log.warn(responseData);
//
//        return null;
//    }
}
