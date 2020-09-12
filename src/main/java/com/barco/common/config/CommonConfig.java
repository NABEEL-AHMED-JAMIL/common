package com.barco.common.config;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import com.barco.common.manager.async.executor.AsyncDALTaskExecutor;
import com.barco.common.manager.async.properties.AsyncTaskProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;


@Configuration
public class CommonConfig {

    public Logger logger = LogManager.getLogger(CommonConfig.class);

    @Autowired
    public AsyncTaskProperties asyncTaskProperties;

    @Bean
    public RestTemplate restTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        logger.info("+================RestTemplate-Start====================+");
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
            .loadTrustMaterial(null, acceptingTrustStrategy).build();
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        logger.info("+================RestTemplate-End====================+");
        return new RestTemplate(requestFactory);
    }

    @Bean
    @Scope("singleton")
    public AsyncDALTaskExecutor asyncDALTaskExecutor() throws Exception {
        logger.debug("===============Application-DAO-INIT===============");
        AsyncDALTaskExecutor taskExecutor = new AsyncDALTaskExecutor(this.asyncTaskProperties.getMinThreads(),
              this.asyncTaskProperties.getMaxThreads(), this.asyncTaskProperties.getIdleThreadLife());
        logger.debug("===============Application-DAO-END===============");
        return taskExecutor;
    }

}
