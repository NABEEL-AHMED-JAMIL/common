package com.barco.common.manager.aws.properties;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

/**
 * @author Nabeel Ahmed
 */
@Component
public class AwsProperties {

    private String region;
    private String accessKey;
    private String secretKey;
    private String bucketName;

    public AwsProperties() {}

    public AwsProperties(String region, String accessKey, String secretKey) {
        if((region != null && !region.equals("")) &&
            (accessKey != null && !accessKey.equals("")) &&
            (secretKey != null && !secretKey.equals(""))) {
            this.region = region;
            this.accessKey = accessKey;
            this.secretKey = secretKey;
         } else {
            throw new NullPointerException("Invalid Properties");
        }
    }

    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }

    public String getAccessKey() {
        return accessKey;
    }
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucketName() {
        return bucketName;
    }
    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
