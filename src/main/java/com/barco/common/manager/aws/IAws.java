package com.barco.common.manager.aws;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.barco.common.manager.aws.properties.AwsProperties;

/**
 * @author Nabeel Ahmed
 */
public interface IAws {

    AWSCredentials credentials(AwsProperties awsProperties) throws AmazonClientException;

}
