package com.barco.common.manager.aws.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;


@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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
