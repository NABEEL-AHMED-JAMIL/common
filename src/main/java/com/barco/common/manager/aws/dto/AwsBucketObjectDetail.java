package com.barco.common.manager.aws.dto;


import com.google.gson.Gson;

/**
 * @author Nabeel.amd
 */
public class AwsBucketObjectDetail {

    private String bucketName;
    private String objKey;

    public AwsBucketObjectDetail() { }

    public String getBucketName() { return bucketName; }
    public void setBucketName(String bucketName) { this.bucketName = bucketName; }

    public String getObjKey() { return objKey; }
    public void setObjKey(String objKey) { this.objKey = objKey; }

    @Override
    public String toString() { return new Gson().toJson(this); }
}
