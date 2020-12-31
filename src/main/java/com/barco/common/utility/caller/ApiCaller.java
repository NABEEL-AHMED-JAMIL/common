package com.barco.common.utility.caller;

import com.amazonaws.util.IOUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;


/**
 * @author Nabeel Ahmed
 */
@Component
@Scope("prototype")
public class ApiCaller {

    public Logger logger = LogManager.getLogger(ApiCaller.class);

    private String strResponse;
    private ResponseEntity<?> response;
    private ArrayList<MultipartContentResponse> multipartContentResponses;

    @Autowired
    private RestTemplate restTemplate;

    public ApiCaller() { }

    public ResponseEntity apiCaller(Object body, HttpMethod httpMethod, String url, Map<String, String> headerMap) throws Exception {
        logger.debug(">>>>>>> ApiCaller := apiCaller Start");
        String log = "Request := End-Point " + url + " Request := Body " + body + " Request := Header  " + headerMap + " Request := Time " + new Date();
        logger.info(log);
        this.response = this.restTemplate.exchange(url, httpMethod, new HttpEntity<>(!httpMethod.equals(HttpMethod.GET) ? body: null,
            this.fillHeader(headerMap)), String.class);
        log = "Response := ActiveStatus " + this.response.getStatusCode() + " Response := Body " + this.response.getBody() + " Response := Time " + new Date();
        logger.info(log);
        logger.debug(">>>>>>> ApiCaller := apiCaller End");
        return this.response;
    }

    public ResponseEntity downloadFile(HttpMethod httpMethod, String url, Map<String, String> headerMap) throws Exception {
        logger.debug(">>>>>>> ApiCaller := downloadFile Start");
        String log = "Request := End-Point " + url + " Request := Header " + headerMap + " Request := Time " + new Date();
        logger.info(log);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
        this.fillHeader(headers, headerMap);
        this.restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        this.response = this.restTemplate.exchange(url, httpMethod, entity, byte[].class);
        log = "Response := ActiveStatus " + this.response.getStatusCode() + " Response := Body " + this.response.getBody() + " Response := Time " + new Date();
        logger.info(log);
        logger.debug(">>>>>>> ApiCaller := downloadFile End");
        //Files.write(Paths.get("E:\\demo1.csv"), response.getBody());
        return response;
    }

    public String uploadFile(String url, File fileSource, Map<String, String> headerMap) throws Exception {
        logger.debug(">>>>>>> ApiCaller := uploadFile Start");
        String log = "Request := End-Point " + url + " Request := Header " + headerMap + " Request := Time " + new Date();
        logger.info(log);
        final InputStream fis = new FileInputStream(fileSource);
        final RequestCallback requestCallback = new RequestCallback() {
            @Override
            public void doWithRequest(final ClientHttpRequest request) throws IOException {
                request.getHeaders().add("Content-type", "application/octet-stream");
                // using for-each loop for iteration over Map.entrySet()
                for (Map.Entry<String,String> entry : headerMap.entrySet()) {
                    request.getHeaders().add(entry.getKey(), entry.getValue());
                }
                IOUtils.copy(fis, request.getBody());
            }
        };
        this.strResponse = this.callUploadFile(url, requestCallback);
        logger.debug(">>>>>>> ApiCaller := apiCaller End");
        return this.strResponse;
    }

    private String uploadFile(String url, InputStream fis, Map<String, String> headerMap) throws Exception {
        logger.debug(">>>>>>> ApiCaller := uploadFile Start");
        String log = "Request := End-Point " + url + " Request := Header " + headerMap + " Request := Time " + new Date();
        logger.info(log);
        final RequestCallback requestCallback = new RequestCallback() {
            @Override
            public void doWithRequest(final ClientHttpRequest request) throws IOException {
                request.getHeaders().add("Content-type", "application/octet-stream");
                // using for-each loop for iteration over Map.entrySet()
                for (Map.Entry<String,String> entry : headerMap.entrySet()) {
                    request.getHeaders().add(entry.getKey(), entry.getValue());
                }
                IOUtils.copy(fis, request.getBody());
            }
        };
        // close the stream
        if(fis != null) {
            fis.close();
        }
        this.strResponse = this.callUploadFile(url, requestCallback);
        logger.debug(">>>>>>> ApiCaller := apiCaller End");
        return this.strResponse;
    }

    public List<MultipartContentResponse> getMultipartContent(String finalUrl, String json, Map<String, String> headerMap) throws Exception {
        this.multipartContentResponses = new ArrayList<>();
        try {
            HttpPost httpPost = new HttpPost(finalUrl);
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-type", "application/json");
            // using for-each loop for iteration over Map.entrySet()
            for (Map.Entry<String,String> entry : headerMap.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(httpPost);
            if (response.getEntity() != null) {
                ByteArrayDataSource datasource = new ByteArrayDataSource(response.getEntity().getContent(), "multipart/mixed");
                MimeMultipart multipart = new MimeMultipart(datasource);
                int count = multipart.getCount();
                logger.info("count " + count);
                for (int i = 0; i < count; i++) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    MultipartContentResponse contentResponse = new MultipartContentResponse();
                    if (bodyPart.isMimeType("text/plain") ) {
                        contentResponse.setType("text").setContent(getOutputByte(bodyPart));
                        this.multipartContentResponses.add(contentResponse);
                    } else if (bodyPart.isMimeType("text/html")) {
                        contentResponse.setType("html").setFileName(bodyPart.getFileName()).setContent(getOutputByte(bodyPart));
                        this.multipartContentResponses.add(contentResponse);
                    } else if (bodyPart.isMimeType("text/csv")) {
                        contentResponse.setType("csv").setFileName(bodyPart.getFileName()).setContent(getOutputByte(bodyPart));
                        this.multipartContentResponses.add(contentResponse);
                    } else if (bodyPart.isMimeType("image/jpeg") || bodyPart.isMimeType("image/png") ||
                            bodyPart.isMimeType("image/svg+xml")) {
                        contentResponse.setType("img").setFileName(bodyPart.getFileName()).setContent(getOutputByte(bodyPart));
                        this.multipartContentResponses.add(contentResponse);
                    } else if (bodyPart.isMimeType("application/octet-stream")) {
                        contentResponse.setType("stream").setFileName(bodyPart.getFileName()).setContent(getOutputByte(bodyPart));
                        this.multipartContentResponses.add(contentResponse);
                    } else if (bodyPart.isMimeType("application/zip")) {
                        contentResponse.setType("zip").setFileName(bodyPart.getFileName()).setContent(getOutputByte(bodyPart));
                        this.multipartContentResponses.add(contentResponse);
                    } else if (bodyPart.isMimeType("application/pdf")) {
                        contentResponse.setType("pdf").setFileName(bodyPart.getFileName()).setContent(getOutputByte(bodyPart));
                        this.multipartContentResponses.add(contentResponse);
                    } else if (bodyPart.isMimeType("application/json")) {
                        contentResponse.setType("json").setContent(getOutputByte(bodyPart));
                        this.multipartContentResponses.add(contentResponse);
                    } else {
                        // if any other data then here
                        logger.info("default " + bodyPart.getContentType());
                    }
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        }
        return this.multipartContentResponses;
    }
    
    private byte[] getOutputByte(BodyPart bodyPart) throws IOException, MessagingException {
        byte[] targetArray = new byte[bodyPart.getInputStream().available()];
        bodyPart.getInputStream().read(targetArray);
        if (bodyPart.getInputStream() != null) {
            bodyPart.getInputStream().close();
        }
        return targetArray;
    }

    private String callUploadFile(String url, RequestCallback requestCallback) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setBufferRequestBody(false);
        this.restTemplate.setRequestFactory(requestFactory);
        final HttpMessageConverterExtractor<String> responseExtractor = new HttpMessageConverterExtractor<String>(String.class,
             this.restTemplate.getMessageConverters());
        return this.restTemplate.execute(String.valueOf(url), HttpMethod.POST, requestCallback, responseExtractor);
    }

    private HttpHeaders fillHeader(Map<String, String> headerMap) {
        HttpHeaders headers = new HttpHeaders();
        if (headerMap != null && headerMap.size() > 0) {
            this.fillHeaderLoop(headers, headerMap);
        } else {
            // in case of auth
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        return headers;
    }

    private void fillHeader(HttpHeaders headers, Map<String, String> headerMap) {
        if (headerMap != null && headerMap.size() > 0) {
            this.fillHeaderLoop(headers, headerMap);
        }
    }

    private void fillHeaderLoop(HttpHeaders headers, Map<String, String> headerMap) {
        Iterator<? extends Entry<String, String>> it = headerMap.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> entry = it.next();
            headers.add(entry.getKey(), entry.getValue());
        }
    }

}
