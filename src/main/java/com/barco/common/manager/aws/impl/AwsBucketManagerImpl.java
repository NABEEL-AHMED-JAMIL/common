package com.barco.common.manager.aws.impl;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.barco.common.manager.aws.IAwsBucketManager;
import com.barco.common.manager.aws.dto.AwsBucketObjectDetail;
import com.barco.common.manager.aws.properties.AwsProperties;
import com.barco.common.utility.BarcoUtil;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author Nabeel Ahmed
 */
public class AwsBucketManagerImpl implements IAwsBucketManager {

    public Logger logger = LogManager.getLogger(AwsBucketManagerImpl.class);

    private AmazonS3 amazonS3;

    public AwsBucketManagerImpl() {}

    /**
     * Method use to check bucket exist or not
     * @param bucketName
     * @return Boolean
     * */
    @Override
    public Boolean isBucketExist(String bucketName) throws AmazonClientException {
        if (bucketName != null && !bucketName.equals("")) {
            return this.amazonS3.doesBucketExist(bucketName);
        }
        throw new NullPointerException("Invalid bucket name");
    }

    /**
     * Method use to check is object exist or not
     * @param bucketName
     * @param objectKey
     * @return Boolean
     * */
    @Override
    public Boolean isObjKeyExist(String bucketName, String objectKey) throws AmazonClientException {
        if (isBucketExist(bucketName) && (objectKey != null && !objectKey.equals(""))) {
            return this.amazonS3.doesObjectExist(bucketName,objectKey);
        }
        throw new NullPointerException("Invalid objectKey name");
    }

    /**
     * Method use to upload object to bucket
     * @param bucketName
     * @param objKey
     * @param inputStream
     * @return AwsBucketObjectDetail
     * */
    @Override
    public AwsBucketObjectDetail uploadToBucket(String bucketName, String objKey, InputStream inputStream)
        throws AmazonClientException, IOException {
        logger.debug("Uploading a new object to S3 from a file > " + objKey);
        this.amazonS3.putObject(new PutObjectRequest(bucketName, objKey, inputStream, new ObjectMetadata()));
        if (!BarcoUtil.isNull(inputStream)) {
            inputStream.close();
        }
        AwsBucketObjectDetail awsBucketObjectDetail = new AwsBucketObjectDetail();
        awsBucketObjectDetail.setBucketName(bucketName);
        awsBucketObjectDetail.setObjKey(objKey);
        logger.info("Upload File Detail Aws :- {}", awsBucketObjectDetail);
        return awsBucketObjectDetail;
    }

    /**
     * Method use to delete the bucket
     * @param objKey
     * @param bucketName
     * @return Boolean
     * */
    @Override
    public Boolean deleteBucketObject(String objKey, String bucketName) throws AmazonClientException {
        logger.warn("Deleting an object");
        this.amazonS3.deleteObject(bucketName, objKey);
        return true;
    }

    /**
     * Method use to get object meta data
     * @param objKey
     * @param bucketName
     * @return Map<String, Object>
     * */
    @Override
    public Map<String, Object> getObjectMetadata(String objKey, String bucketName) throws AmazonClientException {
        return this.amazonS3.getObject(new GetObjectRequest(bucketName, objKey)).getObjectMetadata().getRawMetadata();
    }

    /**
     * Method use to add credentials
     * @param awsProperties
     * @return AWSCredentials
     * */
    @Override
    public AWSCredentials credentials(AwsProperties awsProperties) throws AmazonClientException {
        return new BasicAWSCredentials(awsProperties.getAccessKey(), awsProperties.getSecretKey());
    }

    /**
     * Method use to add amazonS3
     * @param awsProperties
     * */
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

}
