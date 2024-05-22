package com.barco.common.config;

import com.barco.common.manager.async.executor.AsyncDALTaskExecutor;
import com.barco.common.manager.async.properties.AsyncTaskProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.apache.http.impl.client.HttpClients;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * @author Nabeel Ahmed
 */
@Configuration
public class CommonConfig {

    public Logger logger = LogManager.getLogger(CommonConfig.class);

    @Autowired
    public AsyncTaskProperties asyncTaskProperties;

    /**
     * Rest Template Bean With Trust Manager
     * @return RestTemplate
     * */
    @Bean
    public RestTemplate restTemplate() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[] { new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) {}
            public void checkServerTrusted(X509Certificate[] chain, String authType) {}
            public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[]{}; }
        }}, null);
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
            HttpClients.custom().setSSLContext(sslContext).build());
        return new RestTemplate(requestFactory);
    }

    /**
     * Method use to async dal task executor
     * @return AsyncDALTaskExecutor
     * */
    @Bean
    public AsyncDALTaskExecutor asyncDALTaskExecutor() throws Exception {
        logger.debug("===============Application-DAO-INIT===============");
        AsyncDALTaskExecutor taskExecutor = new AsyncDALTaskExecutor(this.asyncTaskProperties.getMinThreads(),
            this.asyncTaskProperties.getMaxThreads(), this.asyncTaskProperties.getIdleThreadLife());
        logger.debug("===============Application-DAO-END===============");
        return taskExecutor;
    }

}