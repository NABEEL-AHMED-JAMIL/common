package com.barco.common.manager.aws;

import com.amazonaws.AmazonClientException;
import com.barco.common.manager.aws.dto.AwsBucketObjectDetail;
import com.barco.common.manager.aws.properties.AwsProperties;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author Nabeel Ahmed
 */
public interface IAwsBucketManager extends IAws {

    Boolean isBucketExist(final String bucketName) throws AmazonClientException;

    Boolean isObjKeyExist(String bucketName, String objectKey) throws AmazonClientException;

    AwsBucketObjectDetail uploadToBucket(String bucketName, String objKey, InputStream inputStream) throws AmazonClientException, IOException;

    Boolean deleteBucketObject(final String objKey, final String bucketName) throws AmazonClientException;

    Map<String, Object> getObjectMetadata(final String objKey, final String bucketName) throws AmazonClientException;

    void amazonS3(AwsProperties awsProperties) throws AmazonClientException;

}
