package com.barco.common.utility.caller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;

/**
 * @author Nabeel Ahmed
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MultipartContentResponse {

    private String type;
    private byte[] content;
    private String fileName;

    public MultipartContentResponse() { }

    public String getType() {
        return type;
    }
    public MultipartContentResponse setType(String type) {
        this.type = type;
        return this;
    }

    public byte[] getContent() {
        return content;
    }
    public MultipartContentResponse setContent(byte[] content) {
        this.content = content;
        return this;
    }

    public String getFileName() {
        return fileName;
    }
    public MultipartContentResponse setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
