package com.barco.common.manager.aws;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import com.barco.common.manager.aws.properties.AwsProperties;


public interface IAwsSESManager extends IAws {

    void sendSESEmail(SendEmailRequest request) throws AmazonClientException;

    void sendRawEmail(SendRawEmailRequest request) throws AmazonClientException;

    void amazonSES(AwsProperties awsProperties) throws AmazonClientException;

}
