package com.barco.common.emailer;

import com.amazonaws.services.simpleemail.model.*;
import com.barco.common.manager.async.executor.AsyncDALTaskExecutor;
import com.barco.common.manager.async.task.AsyncSESTask;
import com.barco.common.manager.aws.impl.AwsBucketManagerImpl;
import com.barco.common.utility.BarcoUtil;
import com.barco.common.utility.ExceptionUtil;
import com.barco.common.utility.view.TemplateType;
import com.barco.common.utility.view.VelocityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@Scope("prototype")
public class EmailMessagesFactory {

    public Logger logger = LogManager.getLogger(EmailMessagesFactory.class);

    @Value("${base.url}")
    private String baseUrl;
    private String FROM = "support@quorum.works";
    private String ICON = "icon";
    private String EMAIL = "email";
    private String TOKEN = "token";
    private String FULLNAME = "fullName";
    private String REMINDER_URL = "reminderUrl";
    private final String UTF8 ="UTF-8";
    private final String BODY = "body";
    private final String PDF_FORMAT = "application/pdf";
    private final String XLS_FORMAT = "application/vnd.ms-excel";

    @Autowired
    private AsyncSESTask asyncSESTask;
    @Autowired
    private VelocityManager velocityManager;
    @Autowired
    private AwsBucketManagerImpl awsBucketManager;
    @Autowired
    private AsyncDALTaskExecutor asyncDALTaskExecutor;

    private SendEmailRequest sendEmailRequest;

    public EmailMessagesFactory() {}

    public void emailAccountCreated(Map<String, Object> emailDetail) {
        try {
            EmailMessageDTO emailMessageDTO = new EmailMessageDTO();
            emailMessageDTO.setFromEmail(FROM);
            emailMessageDTO.setRecipients((String) emailDetail.get(EMAIL));
            emailMessageDTO.setSubject("New Account Verification Email");
            emailMessageDTO.setEmailTemplateName(TemplateType.EMAIL_ACCOUNT_CREATED_TEMPLATE);
            emailDetail.put(REMINDER_URL, baseUrl+"/signup-success?token="+emailDetail.get(TOKEN)
                    +"&username="+emailDetail.get(FULLNAME));
            emailDetail.put(ICON, BarcoUtil.icon);
            emailMessageDTO.setBodyMap(emailDetail);
            this.setSimpleMessage(emailMessageDTO);
        } catch (Exception ex) {
            logger.error("Failed to submit emailAccountCreated  " + ExceptionUtil.getRootCauseMessage(ex));
        }
    }

    public void forgetPassword(Map<String, Object> emailDetail) {
        try {
            EmailMessageDTO emailMessageDTO = new EmailMessageDTO();
            emailMessageDTO.setFromEmail(FROM);
            emailMessageDTO.setRecipients((String) emailDetail.get(EMAIL));
            emailMessageDTO.setSubject("Forgot Password Detail");
            emailMessageDTO.setEmailTemplateName(TemplateType.FORGOT_PASSWORD_TEMPLATE);
            emailDetail.put(REMINDER_URL, baseUrl+"/reset_password?token="+emailDetail.get(TOKEN)
                    +"&username="+emailDetail.get(EMAIL));
            emailDetail.put(ICON, BarcoUtil.icon);
            emailMessageDTO.setBodyMap(emailDetail);
            this.setSimpleMessage(emailMessageDTO);
        } catch (Exception ex) {
            logger.error("Failed to submit forgetPassword " + ExceptionUtil.getRootCauseMessage(ex));
        }
    }

    private void setSimpleMessage(EmailMessageDTO emailMessageDTO) throws Exception {
        this.sendEmailRequest = new SendEmailRequest();
        this.sendEmailRequest.withSource(emailMessageDTO.getFromEmail());
        logger.info("From Email " + emailMessageDTO.getFromEmail());
        if(emailMessageDTO.getRecipientsMulti() != null && emailMessageDTO.getRecipientsMulti().size() > 0) {
            this.sendEmailRequest.withDestination(new Destination().withToAddresses(emailMessageDTO.getRecipientsMulti()));
            logger.info("To Email " + emailMessageDTO.getRecipientsMulti());
        } else {
            this.sendEmailRequest.withDestination(new Destination().withToAddresses(emailMessageDTO.getRecipients()));
            logger.info("To Email " + emailMessageDTO.getRecipients());
        }
        Message message = new Message();
        message.withSubject(new Content().withCharset(UTF8).withData(emailMessageDTO.getSubject()));
        // body detail
        Body body = new Body();
        body.withHtml(new Content().withCharset(UTF8).withData(this.velocityManager.
                getResponseMessage(emailMessageDTO.getEmailTemplateName(), emailMessageDTO.getBodyMap())));
        message.withBody(body);
        // message
        this.sendEmailRequest.withMessage(message);
        this.asyncSESTask.setSimple(true);
        this.asyncSESTask.setSendEmailRequest(sendEmailRequest);
        this.asyncDALTaskExecutor.addTask(asyncSESTask);
    }

}
