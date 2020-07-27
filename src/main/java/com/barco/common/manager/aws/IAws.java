package com.barco.common.manager.aws;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.barco.common.manager.aws.properties.AwsProperties;


public interface IAws {

    void initializeAmazonS3Client() throws AmazonClientException;

    AWSCredentials credentials(AwsProperties awsProperties) throws AmazonClientException;

}
