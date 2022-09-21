package ua.com.univerpulse.toyshop.rest;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Danny Briskin (DBriskin@qaconsultants.com)
 * for testToyShop project.
 */
@Getter
@Setter
public class RestRequest {
    /**
     * URI of Request, aka endpoint
     */
    private String endPoint;
    /**
     * GET/POST/PUT/DELETE
     */
    private String restRequestType;
    /**
     * Expected response code
     */
    private int expectedResponseCode;
    /**
     * Username for basic authentication
     */
    private String user;
    /**
     * Password for basic authentication
     */
    private String password;
    /**
     * Additional headers to add to request
     */
    private Map<String, String> headers;
    /**
     * JSON body of request
     */
    private String jsonBody;
    /**
     * Optional URI path parameters
     * <pre>{@code
     * http://some.url/value1/value2/value3
     */
    private List<String> urlPathParameters;
    /**
     * Optional GET URI parameters
     * <pre>{@code
     * http://some.url?paramOne=value1&param2=value2
     * }</pre>
     */
    private Map<String, String> httpGetParameters;

    public RestRequest(@NotNull String endPoint
            , String restRequestType
            , int expectedResponseCode
            , String... urlPathParameters) {
        this.restRequestType = restRequestType;
        this.expectedResponseCode = expectedResponseCode;
        this.urlPathParameters = Arrays.asList(urlPathParameters);
        this.endPoint = endPoint;
        this.addUrlParameters(urlPathParameters);
        this.headers = new HashMap<>();
        this.headers.put("Accept", "application/json");
    }

    /**
     * Adds optional URL path parameters to current URL
     *
     * @param urlPathParameters parameters
     * @see #urlPathParameters
     */
    public void addUrlParameters(@NotNull String... urlPathParameters) {
        if (urlPathParameters.length > 0) {
            this.endPoint += String.join("/", urlPathParameters);
        }
    }

    public void addHeader(String header, String value) {
        this.headers.put(header, value);
    }

    /**
     * URL parameters are reset to new
     *
     * @param httpGetParameters map of parameters
     * @see #httpGetParameters
     */
    public void setHttpGetParameters(@NotNull Map<String, String> httpGetParameters) {
        this.httpGetParameters = httpGetParameters;
        if (!httpGetParameters.isEmpty()) {
            StringJoiner stringJoiner = new StringJoiner("&"
                    , this.endPoint.split("\\?")[0] + "?", "");
            httpGetParameters.forEach((key, value) -> stringJoiner.add(key + "=" + value));
            this.endPoint = stringJoiner.toString();
        }
    }
}
