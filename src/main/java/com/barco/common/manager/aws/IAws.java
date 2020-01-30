package com.barco.common.manager.aws;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.barco.common.manager.aws.properties.AwsProperties;

/**
 * @author Nabeel.amd
 */
public interface IAws {

    public void initializeAmazonS3Client() throws AmazonClientException;
    public AWSCredentials credentials(AwsProperties awsProperties) throws AmazonClientException;

}
