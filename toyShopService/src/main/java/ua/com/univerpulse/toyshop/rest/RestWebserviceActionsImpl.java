package ua.com.univerpulse.toyshop.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Class to encapsulate all REST webservice related actions
 * <p>
 * apache httpclient library used
 *
 * @author Danny Briskin (DBriskin@qaconsultants.com)
 * for qactoyotaautomation project.
 */
@Component
@Lazy
@Scope("prototype")
@Log4j2
public class RestWebserviceActionsImpl implements RestWebserviceActions {
    private final BasicCookieStore cookieStore = new BasicCookieStore();
    /**
     * Main request object
     */
    private HttpRequestBase httpRequest;
    /**
     * REST request client
     */
    private CloseableHttpClient client;

    private SSLContext sslContext;
    private String restRequestType;

    @PostConstruct
    @SneakyThrows
    private void init() {
        // Trust all SSL certificates
        this.sslContext = new SSLContextBuilder()
                .loadTrustMaterial(null, (certificate, authType) -> true).build();

        this.client = HttpClients.custom()
                                 .setSSLContext(sslContext)
                                 .setSSLHostnameVerifier(new NoopHostnameVerifier())
                                 .setDefaultCookieStore(cookieStore)
                                 .build();
    }

    @Override
    public RestResponse sendRestRequestTo(RestRequest restRequest, boolean checkResponseCode)
            throws AuthenticationException, IOException {
        //setup
        this.setupRestRequest(restRequest);

        //REST execution is here
        RestResponse response = this.getRestResponse(
                this.executeRequest(restRequest.getExpectedResponseCode()
                        , 25000
                        , checkResponseCode));
        if (response == null) {
            log.debug("Empty response for " + restRequest.getEndPoint());
        }
        return response;
    }

    @SneakyThrows
    private void setupRestRequest(@NotNull RestRequest restRequest) {
        this.createRestRequest(restRequest.getRestRequestType(), restRequest.getEndPoint());
        for (Map.Entry<String, String> entry : restRequest.getHeaders().entrySet()) {
            this.setRequestHeader(entry.getKey(), entry.getValue());
        }
        //set request body here (JSON/XML)
        if (restRequest.getJsonBody() != null) {
            this.setRequestJsonBody(restRequest.getJsonBody(), "application/json");
        }

        //credentials - create authentication header
//        UsernamePasswordCredentials usernamePasswordCredentials =
//                new UsernamePasswordCredentials(restRequest.getUser(), restRequest.getPassword());
//
//        Header header = new BasicScheme(StandardCharsets.UTF_8)
//                .authenticate(usernamePasswordCredentials, this.httpRequest, null);

//        Header header = new BasicHeader(
//        "Authorization", "Bearer 003PoAxERabxZ0Caa17rC-F3b-anrka3GxLmWWd_C_");

//        this.httpRequest.addHeader(header);
    }

    /**
     * Set uri. If it contains parameters - they are encoded
     * For params like query={parent-id[2]}
     *
     * @param uri uri for set
     */
    @SneakyThrows
    private void setRequestUri(@NotNull String uri) {
        if (this.httpRequest != null) {
            if (uri.contains("?")) {
                String[] uriParts = uri.split("\\?");
                assertTrue(uriParts.length > 1
                                   && uriParts[1] != null
                                   && uriParts[1].contains("=")
                        , "Correct parameters must present");
                URIBuilder builder = new URIBuilder(uriParts[0]);
                builder.setCharset(Consts.UTF_8);
                String[] parametersPairs = uriParts[1].split("&");

                Arrays.asList(parametersPairs).forEach(parametersPair -> {
                    String[] parameters = parametersPair.split("=");
                    assertTrue(parameters.length > 1
                            , "Correct parameter must present");
                    builder.setParameter(parameters[0], parameters[1]);
                });
                this.httpRequest.setURI(builder.build());
            } else {
                this.httpRequest.setURI(new URI(uri));
            }
        }
    }

    @Override
    public void createRestRequest(String restRequestType, String webServiceEndpointUrl) {
        this.restRequestType = restRequestType;
        switch (this.restRequestType) {
            case "GET":
                this.httpRequest = new HttpGet();
                break;
            case "PUT":
                this.httpRequest = new HttpPut();
                break;
            case "DELETE":
                this.httpRequest = new HttpDelete();
                break;
            case "POST":
                this.httpRequest = new HttpPost();
                break;
            case "PATCH":
                this.httpRequest = new HttpPatch();
                break;
            case "OPTIONS":
                this.httpRequest = new HttpOptions();
                break;
            default:
                log.warn("Unknown Request Type : " + this.restRequestType);
                break;
        }
        this.setRequestUri(webServiceEndpointUrl);
    }

    private void requestClose() {
        if (httpRequest != null) {
            httpRequest.releaseConnection();
        }
    }

    @PreDestroy
    @SneakyThrows
    public void clientClose() {
        this.requestClose();
        if (this.client != null) {
            this.client.close();
        }
    }

    @Override
    public void setRequestHeader(String key, String value) {
        this.httpRequest.removeHeaders(key);
        this.httpRequest.setHeader(key, value);
    }

    @Override
    public void setRequestJsonBody(String bodyValue, String bodyType) {
        StringEntity postingString;
        postingString = new StringEntity(bodyValue, "UTF-8");
        postingString.setContentType(bodyType);
        switch (this.restRequestType) {
            case "POST":
                ((HttpPost) this.httpRequest).setEntity(postingString);
                break;
            case "PUT":
                ((HttpPut) this.httpRequest).setEntity(postingString);
                break;
            case "DELETE":
                break;
            default:
                log.fatal("Error, unsupported Http request:"
                                  + restRequestType);
                break;
        }
    }

    @Override
    @Nullable
    @SneakyThrows
    public HttpResponse executeRequest(int expectedResponseCode
            , int timeoutMilliseconds, boolean checkResponseCode) {
        HttpResponse response = null;
        if (httpRequest != null) {
            response = client.execute(this.httpRequest);
            if (checkResponseCode
                    && expectedResponseCode !=
                    response.getStatusLine().getStatusCode()) {
                //concise message
                fail("WebService returned ["
                             + response.getStatusLine().getStatusCode()
                             + "] code instead of {"
                             + expectedResponseCode + "}\n"
                             + " URI [" + this.httpRequest.getURI() + "]\n"
                             + " Detailed response is: \n\t"
                             + (response.getStatusLine().getStatusCode() < 500 ?
                        EntityUtils.toString(response.getEntity(), "UTF-8") : ""));
            }
        }
        return response;
    }

    @Override
    @SneakyThrows
    public RestResponse getRestResponse(HttpResponse response) {
        RestResponse restResponseResult = new RestResponse();
        if (response == null) {
            log.warn("The REST response is null");
        } else {
            restResponseResult.setResponseCode(response.getStatusLine().getStatusCode());
            // check that response pretend to be a JSON
            if (response.getEntity() != null && response.getEntity().getContentType() != null) {
                this.manipulateJsonResponse(response, restResponseResult);
            }
        }
        return restResponseResult;
    }

    @SneakyThrows
    private void manipulateJsonResponse(@NotNull HttpResponse response
            , RestResponse restResponseResult) {
        String resultJson = EntityUtils.toString(response.getEntity());
        // again dummy check that it is really a JSON
        if (resultJson.startsWith("{") && resultJson.endsWith("}")) {
            ObjectNode jsonNodes =
                    (ObjectNode) new ObjectMapper().readTree(resultJson);
            restResponseResult.setJson(jsonNodes);
        } else {
            log.warn("Response body is not JSON [" + resultJson + "]");
        }
    }

    @Nullable
    @Override
    @SneakyThrows
    public <T> T parseRestResponse(ObjectNode resultJson, Class<T> tClass) {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.treeToValue(resultJson, tClass);
    }

    private static final @NotNull String getBasicAuthenticationHeader(String username, String password) {
        String valueToEncode = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
//        return "Bearer 003PoAxERabxZ0Caa17rC-F3b-anrka3GxLmWWd_C_";
    }
}
