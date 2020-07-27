package com.barco.common.manager.async.task;

import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import com.barco.common.manager.aws.impl.AwsSESManagerImpl;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class AsyncSESTask implements Runnable {

    public static Logger logger = LogManager.getLogger(AsyncSESTask.class);

    @Autowired
    private AwsSESManagerImpl awsSESManager;

    private Boolean isSimple = false;
    private SendEmailRequest sendEmailRequest;
    private SendRawEmailRequest sendRawEmailRequest;

    public AsyncSESTask() { }

    // give free hand to the request and email give to the separate thread
    @Override
    public void run() {
        // false then send attache email
        if(this.getSimple()){
            logger.debug("===>>>>>>Send-Simple-Email<<<<<<=======");
            this.awsSESManager.sendSESEmail(this.getSendEmailRequest());
        }else{
            logger.debug("===>>>>>>Send-Raw-Email<<<<<<=======");
            this.awsSESManager.sendRawEmail(this.getSendRawEmailRequest());
        }
    }

    public Boolean getSimple() { return isSimple; }
    public void setSimple(Boolean simple) { isSimple = simple; }

    public SendEmailRequest getSendEmailRequest() { return sendEmailRequest; }
    public void setSendEmailRequest(SendEmailRequest sendEmailRequest) { this.sendEmailRequest = sendEmailRequest; }

    public SendRawEmailRequest getSendRawEmailRequest() { return sendRawEmailRequest; }
    public void setSendRawEmailRequest(SendRawEmailRequest sendRawEmailRequest) { this.sendRawEmailRequest = sendRawEmailRequest; }

    @Override
    public String toString() { return new Gson().toJson(this); }

}
