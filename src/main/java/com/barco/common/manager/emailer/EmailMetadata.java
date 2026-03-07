package com.barco.common.manager.emailer;

import com.google.gson.Gson;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Nabeel Ahmed
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailMetadata implements Serializable {

    private String fromEmail;
    private String recipients;
    private List<String> recipientsMulti;
    private String subject;
    private String bodyPayload;
    private Map<String, String> bodyMap;

    public EmailMetadata() {}

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public List<String> getRecipientsMulti() {
        return recipientsMulti;
    }

    public void setRecipientsMulti(List<String> recipientsMulti) {
        this.recipientsMulti = recipientsMulti;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBodyPayload() {
        return bodyPayload;
    }

    public void setBodyPayload(String bodyPayload) {
        this.bodyPayload = bodyPayload;
    }

    public Map<String, String> getBodyMap() {
        return bodyMap;
    }

    public void setBodyMap(Map<String, String> bodyMap) {
        this.bodyMap = bodyMap;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}