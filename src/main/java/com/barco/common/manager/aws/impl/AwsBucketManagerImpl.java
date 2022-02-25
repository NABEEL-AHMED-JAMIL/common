package com.barco.common.manager.aws.impl;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.apigateway.model.NotFoundException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.barco.common.manager.aws.IAwsBucketManager;
import com.barco.common.manager.aws.dto.AwsBucketObjectDetail;
import com.barco.common.manager.aws.properties.AwsProperties;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Nabeel Ahmed
 */
@Component
public class AwsBucketManagerImpl implements IAwsBucketManager {

    public Logger logger = LogManager.getLogger(AwsBucketManagerImpl.class);

    @Autowired
    private AwsProperties awsProperties;

    private AmazonS3 amazonS3;
    private AWSCredentials credentials;

    public AwsBucketManagerImpl() {}

    @Override
    @PostConstruct
    public void initializeAmazonS3Client() throws AmazonClientException {
        this.credentials = new BasicAWSCredentials(this.awsProperties.getAccessKey(), this.awsProperties.getSecretKey());
        logger.info("+================AWS-S3-START====================+");
        this.amazonS3 = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(this.credentials))
            .withRegion(Regions.fromName(this.awsProperties.getRegion())).build();
        logger.info("+================AWS-S3-END====================+");
    }

    @Override
    public String createBucket(String bucketName) throws AmazonClientException {
        String bucketLocation = null;
        if (this.isBucketExist(bucketName)) {
            this.amazonS3.createBucket(new CreateBucketRequest(bucketName));
            bucketLocation = this.amazonS3.getBucketLocation(new GetBucketLocationRequest(bucketName));
            logger.info("New Bucket location:- " + bucketLocation);
        }
        return bucketLocation;
    }

    @Override
    public Boolean isBucketExist(String bucketName) throws AmazonClientException {
        if (bucketName != null && !bucketName.equals("")) {
            return this.amazonS3.doesBucketExist(bucketName);
        }
        throw new NullPointerException("Invalid bucket name");
    }

    @Override
    public Set<String> listBuckets() throws AmazonClientException {
        Set<String> bucketsDetail = new HashSet<>();
        for (Bucket bucket : this.amazonS3.listBuckets()) {
            logger.info(" -----> " + bucket.getName());
            bucketsDetail.add(bucket.getName());
        }
        return bucketsDetail;
    }

    @Override
    public Boolean deleteBucket(String bucketName) throws AmazonClientException {
        if (this.amazonS3.doesBucketExist(bucketName)) {
            this.amazonS3.deleteBucket(bucketName);
            return true;
        }
        throw new NotFoundException("Bucket Not Found Exception");
    }

    @Override
    public Boolean isObjKeyExist(String bucketName, String objectKey) throws AmazonClientException {
        if (isBucketExist(bucketName) && (objectKey != null && !objectKey.equals(""))) {
            return this.amazonS3.doesObjectExist(bucketName,objectKey);
        }
        throw new NullPointerException("Invalid objectKey name");
    }

    @Override
    public AwsBucketObjectDetail uploadToBucket(String bucketName, String objKey, InputStream inputStream)
            throws AmazonClientException, IOException {
        logger.debug("Uploading a new object to S3 from a file > " + objKey);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objKey,
                inputStream, new ObjectMetadata());
        this.amazonS3.putObject(putObjectRequest);
        if (inputStream != null) {
            inputStream.close();
        }
        AwsBucketObjectDetail awsBucketObjectDetail = new AwsBucketObjectDetail();
        awsBucketObjectDetail.setBucketName(bucketName);
        awsBucketObjectDetail.setObjKey(objKey);
        logger.info("Upload File Detail Aws :- " + awsBucketObjectDetail);
        return awsBucketObjectDetail;
    }

    @Override
    public Boolean deleteBucketObject(String objKey, String bucketName) throws AmazonClientException {
        logger.warn("Deleting an object");
        this.amazonS3.deleteObject(bucketName, objKey);
        return true;
    }

    @Override
    public Map<String, Object> getObjectMetadata(String objKey, String bucketName) throws AmazonClientException {
        return this.amazonS3.getObject(new GetObjectRequest(bucketName, objKey))
            .getObjectMetadata().getRawMetadata();
    }

    @Override
    public AWSCredentials credentials(AwsProperties awsProperties) throws AmazonClientException {
        return new BasicAWSCredentials(awsProperties.getAccessKey(), awsProperties.getSecretKey());
    }

    @Override
    public void amazonS3(AwsProperties awsProperties) throws AmazonClientException {
        logger.debug("+================AWS--START====================+");
        this.amazonS3 = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(this.credentials(awsProperties)))
            .withRegion(Regions.fromName(awsProperties.getRegion())).build();
        logger.debug("+================AWS-S3-END====================+");
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static void main(String[] args) {
        AwsBucketManagerImpl awsBucketManager = new AwsBucketManagerImpl();
        AwsProperties awsProperties = new AwsProperties("us-east-1", "AKIAZ6NING3INUTIKOWN",
            "jsu/jsUHQN0ElbRZQqf9ehXaOGXwcSq7MbBagG7i");
        awsBucketManager.amazonS3(awsProperties);
        System.out.println(awsBucketManager.getObjectMetadata("Spring-RoadMap.png", "barco-user-bucket"));
        System.out.println(awsBucketManager.isBucketExist("barco-user-bucket"));
    }

}
