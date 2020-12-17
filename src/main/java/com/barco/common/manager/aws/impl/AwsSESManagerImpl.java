package com.barco.common.manager.aws.impl;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import com.barco.common.manager.aws.IAwsSESManager;
import com.barco.common.manager.aws.properties.AwsProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Nabeel Ahmed
 */
@Component
@Scope("prototype")
public class AwsSESManagerImpl implements IAwsSESManager {

    public Logger logger = LogManager.getLogger(AwsSESManagerImpl.class);

    @Autowired
    private AwsProperties awsProperties;

    private AWSCredentials credentials;
    private AmazonSimpleEmailService amazonSES;

    public AwsSESManagerImpl() {}

    @Override
    @PostConstruct
    public void initializeAmazonS3Client() throws AmazonClientException {
        this.credentials = new BasicAWSCredentials(this.awsProperties.getAccessKey(), this.awsProperties.getSecretKey());
        logger.info("+================AWS-SIMPLE-EMAIL-SERVICE-START====================+");
        this.amazonSES = AmazonSimpleEmailServiceClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(this.credentials))
            .withRegion(Regions.fromName(this.awsProperties.getRegion())).build();
        logger.info("+================AWS-SIMPLE-EMAIL-SERVICE-END====================+");
    }

    public AwsProperties getAwsProperties() {
        return awsProperties;
    }
    public void setAwsProperties(AwsProperties awsProperties) {
        this.awsProperties = awsProperties;
    }

    public void sendSESEmail(SendEmailRequest request) throws AmazonClientException {
        logger.info("Simple Email Send Start.");
        this.amazonSES.sendEmail(request);
        logger.info("Simple Email Send End.");
    }

    public void sendRawEmail(SendRawEmailRequest request) throws AmazonClientException {
        logger.info("Raw Email Send Start.");
        this.amazonSES.sendRawEmail(request);
        logger.info("Simple Email Send End.");
     }

    @Override
    public AWSCredentials credentials(AwsProperties awsProperties) throws AmazonClientException {
        logger.info("+================AWS-CREDENTIALS-UPDATE-START====================+");
        AWSCredentials credentials = new BasicAWSCredentials(awsProperties.getAccessKey(), awsProperties.getSecretKey());
        logger.info("+================AWS-CREDENTIALS-UPDATE-END====================+");
        return credentials;
    }

    @Override
    public void amazonSES(AwsProperties awsProperties) throws AmazonClientException {
        logger.info("+================AWS-SIMPLE-EMAIL-SERVICE-START====================+");
        this.amazonSES = AmazonSimpleEmailServiceClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials(awsProperties)))
            .withRegion(Regions.fromName(awsProperties.getRegion())).build();
        logger.info("+================AWS-SIMPLE-EMAIL-SERVICE-END====================+");
    }

}
