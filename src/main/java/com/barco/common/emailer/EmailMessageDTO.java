package com.barco.common.emailer;

import com.google.gson.Gson;
import java.io.Serializable;
import java.util.Map;

/**
 * @author Nabeel.amd
 */
public class EmailMessageDTO implements Serializable {

    private String userName;
    private String fromEmail;
    private String fromName;
    private String recipients;
    private String cc;
    private String subject;
    private Map<String, String> bodyMap;
    private String emailTemplateName;
    private String category;

    public EmailMessageDTO() { }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getFromEmail() { return fromEmail; }
    public void setFromEmail(String fromEmail) { this.fromEmail = fromEmail; }

    public String getFromName() { return fromName; }
    public void setFromName(String fromName) { this.fromName = fromName; }

    public String getRecipients() { return recipients; }
    public void setRecipients(String recipients) { this.recipients = recipients; }

    public String getCc() { return cc; }
    public void setCc(String cc) { this.cc = cc; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public Map<String, String> getBodyMap() { return bodyMap; }
    public void setBodyMap(Map<String, String> bodyMap) { this.bodyMap = bodyMap; }

    public String getEmailTemplateName() { return emailTemplateName; }
    public void setEmailTemplateName(String emailTemplateName) { this.emailTemplateName = emailTemplateName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    @Override
    public String toString() { return new Gson().toJson(this); }

}
