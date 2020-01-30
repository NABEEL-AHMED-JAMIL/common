package com.barco.common.manager.aws;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import com.barco.common.manager.aws.properties.AwsProperties;

/**
 * @author Nabeel.amd
 */
public interface IAwsSESManager extends IAws {

    public void sendSESEmail(SendEmailRequest request) throws AmazonClientException;

    public void sendRawEmail(SendRawEmailRequest request) throws AmazonClientException;

    public void amazonSES(AwsProperties awsProperties) throws AmazonClientException;

    public void updateAmazonSES(AwsProperties awsProperties);
}
