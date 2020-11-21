package com.barco.common.manager.aws.properties;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Nabeel Ahmed
 */
@Component
@Scope("prototype")
public class AwsProperties {

    @Value("${aws.endpointUrl}")
    private String endpointUrl;
    @Value("${aws.region}")
    private String region;
    @Value("${aws.accessKey}")
    private String accessKey;
    @Value("${aws.secretKey}")
    private String secretKey;
    private String bucketName;

    public AwsProperties() {}

    // for email
    public AwsProperties(String region, String accessKey, String secretKey) {
        if((region != null && !region.equals("")) && (accessKey != null && !accessKey.equals("")) &&
            (secretKey != null && !secretKey.equals(""))) {
            this.region = region;
            this.accessKey = accessKey;
            this.secretKey = secretKey;
         } else {
            throw new NullPointerException("Invalid Properties");
        }
    }

    // for bucket
    public AwsProperties(String endpointUrl, String region, String accessKey, String secretKey) {
        if((region != null && !region.equals("")) && (bucketName != null && !bucketName.equals("")) &&
            (accessKey != null && !accessKey.equals("")) && (secretKey != null && !secretKey.equals("")) &&
            (endpointUrl != null && !endpointUrl.equals(""))) {
            this.endpointUrl = endpointUrl;
            this.region = region;
            this.accessKey = accessKey;
            this.secretKey = secretKey;
        } else {
            throw new NullPointerException("Invalid Properties");
        }
    }

    public String getEndpointUrl() { return endpointUrl; }
    public void setEndpointUrl(String endpointUrl) { this.endpointUrl = endpointUrl; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getAccessKey() { return accessKey; }
    public void setAccessKey(String accessKey) { this.accessKey = accessKey; }

    public String getSecretKey() { return secretKey; }
    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }

    public String getBucketName() { return bucketName; }
    public void setBucketName(String bucketName) { this.bucketName = bucketName; }

    @Override
    public String toString() { return new Gson().toJson(this); }

}
