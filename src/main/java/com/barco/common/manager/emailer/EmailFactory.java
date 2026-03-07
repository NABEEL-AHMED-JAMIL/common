package com.barco.common.manager.emailer;

import com.barco.common.utility.BarcoUtil;
import com.barco.common.utility.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Objects;

/**
 * @author Nabeel Ahmed
 */
@Component
public class EmailFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailFactory.class);

    private static final String UTF8 = "utf-8";
    private final JavaMailSender javaMailSender;

    public EmailFactory(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Asynchronous method to send simple email
     * @param emailMetadata the email request containing all necessary information to send the email
     */
    @Async("emailTaskExecutor")
    public void sendSimpleMailAsync(EmailMetadata emailMetadata) {
        this.sendSimpleMail(emailMetadata);
    }

    /**
     * Method to send simple email with support for placeholders in the body
     * @param emailMetadata the email request containing all necessary information to send the email
     * @return true if email sent successfully, false otherwise
     */
    public boolean sendSimpleMail(EmailMetadata emailMetadata) {
        try {
            this.validate(emailMetadata);
            MimeMessage mailMessage = this.javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, UTF8);
            helper.setFrom(emailMetadata.getFromEmail());
            helper.setTo(emailMetadata.getRecipients());
            if (!BarcoUtil.isNull(emailMetadata.getRecipientsMulti()) && !emailMetadata.getRecipientsMulti().isEmpty()) {
                helper.setCc(emailMetadata.getRecipientsMulti().toArray(new String[0]));
            }
            helper.setSubject(emailMetadata.getSubject());
            helper.setText(getResponseMessage(emailMetadata.getBodyPayload(), emailMetadata.getBodyMap()),true);
            this.javaMailSender.send(mailMessage);
            LOGGER.info("Email sent successfully. Payload={}", emailMetadata.getBodyMap());
            return true;
        } catch (Exception ex) {
            LOGGER.error("Email sending failed. Payload={}", emailMetadata.getBodyMap());
            LOGGER.error("Exception: {}", ExceptionUtil.getRootCauseMessage(ex));
            return false;
        }
    }

    /**
     * Method to replace placeholders in the email body with actual values from the bodyMap
     * @param bodyPayload the email body containing placeholders in the format ${key}
     * @param bodyMap a map of key-value pairs to replace in the bodyPayload
     * @return the email body with placeholders replaced by actual values
     */
    public String getResponseMessage(String bodyPayload, Map<String, String> bodyMap) {
        if (BarcoUtil.isNull(bodyPayload) || BarcoUtil.isNull(bodyMap)) {
            return bodyPayload;
        }
        for (Map.Entry<String, String> entry : bodyMap.entrySet()) {
            bodyPayload = bodyPayload.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        return bodyPayload;
    }

    /**
     * Validate the email request object
     * @param emailMetadata
     */
    private void validate(EmailMetadata emailMetadata) {
        Objects.requireNonNull(emailMetadata, "Email metadata must not be null");
        Objects.requireNonNull(emailMetadata.getFromEmail(), "From email must not be null");
        Objects.requireNonNull(emailMetadata.getRecipients(), "Recipients must not be null");
        Objects.requireNonNull(emailMetadata.getSubject(), "Subject must not be null");
        Objects.requireNonNull(emailMetadata.getBodyPayload(), "Email body must not be null");
    }

}
