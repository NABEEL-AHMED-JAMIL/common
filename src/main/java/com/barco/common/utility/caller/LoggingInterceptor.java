package com.barco.common.utility.caller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author Nabeel Ahmed
 */
public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    private Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
        throws IOException {
        if (logger.isDebugEnabled()) {
            logger.info("HTTP Request: {} {}: {}", request.getMethod(), request.getURI(), new String(body, UTF_8));
        }
        ClientHttpResponse response = execution.execute(request, body);
        if (logger.isDebugEnabled()) {
            logger.info("HTTP Response {}: {}", response.getStatusCode().value(), toString(response.getBody(), UTF_8));
        }
        return response;
    }

    private static String toString(InputStream in, Charset charset) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int length;
        while ((length = in.read(bytes)) != -1) {
            out.write(bytes, 0, length);
        }
        return out.toString(charset.name());
    }

}
