package com.barco.common.manager.aws;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.barco.common.manager.aws.dto.AwsBucketObjectDetail;
import com.barco.common.manager.aws.properties.AwsProperties;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

/**
 * @author Nabeel.amd
 */
public interface IAwsBucketManager extends IAws {

    public String createBucket(final String bucketName) throws AmazonServiceException;

    public Boolean isBucketExist(final String bucketName) throws AmazonClientException;

    public Set<String> listBuckets() throws AmazonClientException;

    public Boolean deleteBucket(String bucketName) throws AmazonClientException;

    public Boolean isObjKeyExist(String bucketName, String objectKey) throws AmazonClientException;

    public AwsBucketObjectDetail uploadToBucket(String bucketName, String objKey, InputStream inputStream) throws AmazonClientException, IOException;

    public Boolean deleteBucketObject(final String objKey, final String bucketName) throws AmazonClientException;

    public Map<String, Object> getObjectMetadata(final String objKey, final String bucketName) throws AmazonClientException;

    public void amazonS3(AwsProperties awsProperties) throws AmazonClientException;

    public void updateAmazonBucket(AwsProperties awsProperties);

}
