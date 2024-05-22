package com.barco.common.emailer;

import com.barco.common.utility.BarcoUtil;
import com.barco.common.utility.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Nabeel Ahmed
 */
@Component
public class EmailMessagesFactory {

    private Logger logger = LoggerFactory.getLogger(EmailMessagesFactory.class);

    private final String UTF8 = "utf-8";

    @Autowired
    private JavaMailSender javaMailSender;

    private ExecutorService executorService;

    public EmailMessagesFactory() {
        this.executorService = Executors.newCachedThreadPool();
    }

    /**
     * Method use to send the simple email
     * @param emailContent
     * */
    public String sendSimpleMailAsync(EmailMessageRequest emailContent) {
        try {
            Future<?> future = this.executorService.submit(() -> sendSimpleMail(emailContent));
            if (future.isDone()) {}
            // Return immediately after submitting the task
            return "Mail sending task submitted successfully...";
        } catch (Exception ex) {
            // Handle any exceptions occurred while submitting the task
            ex.printStackTrace();
            return "Error while submitting mail sending task";
        }
    }

    /**
     * Method use to send the simple email
     * @param emailContent
     * */
    private void sendSimpleMail(EmailMessageRequest emailContent) {
        try {
            MimeMessage mailMessage = this.javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, UTF8);
            helper.setFrom(emailContent.getFromEmail());
            if (!BarcoUtil.isNull(emailContent.getRecipients())) {
                helper.setTo(emailContent.getRecipients());
                if (emailContent.getRecipientsMulti() != null && emailContent.getRecipientsMulti().size() > 0) {
                    // * * * * * * * * *Send cc's* * * * * * * * *
                    String ccSendTo = emailContent.getRecipientsMulti().toString();
                    ccSendTo = ccSendTo.substring(1, ccSendTo.length()-1);
                    helper.setCc(ccSendTo);
                }
                helper.setSubject(emailContent.getSubject());
                helper.setText(this.getResponseMessage(
                        emailContent.getBodyPayload(), emailContent.getBodyMap()), true);
                this.javaMailSender.send(mailMessage);
                logger.info(String.format("Email Send Successfully Content %s.", emailContent.getBodyMap().toString()));
            } else {
                throw new Exception("Recipient Not Found");
            }
        } catch (Exception ex) {
            logger.error("Exception :- " + ExceptionUtil.getRootCauseMessage(ex));
        }
    }

    /**
     * Method use to get the response message
     * @param bodyPayload
     * @param bodyMap
     * @return String
     * */
    public String getResponseMessage(String bodyPayload, Map<String, Object> bodyMap) {
        for (Map.Entry<String, Object> objectEntry: bodyMap.entrySet()) {
            bodyPayload = bodyPayload.replace(String.format("${%s}",
                objectEntry.getKey()), String.valueOf(objectEntry.getValue()));
        }
        return bodyPayload;
    }

}
