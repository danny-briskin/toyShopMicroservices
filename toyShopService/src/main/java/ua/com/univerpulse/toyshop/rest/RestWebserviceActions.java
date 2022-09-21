package ua.com.univerpulse.toyshop.rest;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthenticationException;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * @author Danny Briskin (DBriskin@qaconsultants.com)
 * for testToyShop project.
 */
public interface RestWebserviceActions {
    /**
     * Sends REST request to WebService. Asserts that response is given
     *
     * @param restRequest       request request encapsulation
     * @param checkResponseCode whether to check response code against expected
     * @return pair of HttpResponse and
     * JSON ObjectNode with response, null if there is no response
     * @see RestRequest
     * @see RestResponse
     */
    @Nullable
    RestResponse sendRestRequestTo(RestRequest restRequest, boolean checkResponseCode)
            throws AuthenticationException, IOException;

    void createRestRequest(String restRequestType, String webServiceEndpointUrl);

    /**
     * Sets request header
     *
     * @param key   header key
     * @param value header value
     */
    void setRequestHeader(String key, String value);

    void setRequestJsonBody(String bodyValue, String bodyType);

    /**
     * Executes request against endpoint. Optionally validates the response code
     *
     * @param expectedResponseCode expectedResponseCode
     * @param timeoutMilliseconds  timeout Milliseconds
     * @param checkResponseCode    whether to check response code
     * @return HttpResponse
     * @see HttpResponse
     */
    @Nullable
    HttpResponse executeRequest(int expectedResponseCode, int timeoutMilliseconds
            , boolean checkResponseCode);

    /**
     * Extracts response code and response body from HttpResponse object and
     * stores it in RestResponse object
     *
     * @param response response
     * @return RestResponse
     * @throws IOException in case of invalid response
     * @see RestResponse
     * @see HttpResponse
     */
    RestResponse getRestResponse(HttpResponse response) throws IOException;

    /**
     * Parses JSON response into a valid recipient class object
     *
     * @param resultJson JSON to parse
     * @param tClass     class recipient
     * @param <T>        type of recipient class
     * @return T object
     */
    @Nullable <T> T parseRestResponse(ObjectNode resultJson, Class<T> tClass);
}
