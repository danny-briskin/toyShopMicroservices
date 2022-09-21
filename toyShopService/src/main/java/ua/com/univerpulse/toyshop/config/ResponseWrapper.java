package ua.com.univerpulse.toyshop.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.env.Environment;

/**
 * @author Danny Briskin (DBriskin@qaconsultants.com)
 * for toyShopPaymentsService project.
 */
public class ResponseWrapper<T> {
    private final Integer port;
    private final T data;

    public ResponseWrapper(@NotNull final Environment environment, final T data) {
        String serverPort = environment.getProperty("server.port");
        this.port = serverPort != null ? Integer.parseInt(serverPort) : null;
        this.data = data;
    }

    public Integer getPort() {
        return port;
    }

    public T getData() {
        return data;
    }
}
