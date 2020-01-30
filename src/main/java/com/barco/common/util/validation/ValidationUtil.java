package com.barco.common.util.validation;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Nabeel.amd
 */
@Component
@Scope("prototype")
public class ValidationUtil {

    public Logger logger = LogManager.getLogger(ValidationUtil.class);

    private final String emailRegex = "^[(a-zA-Z-0-9-\\_\\+\\.)]+@[(a-z-A-z)]+\\.[(a-zA-z)]{2,3}$";
    private final String mobileNumberRegex = "^\\+[0-9]{2,3}+-[0-9]{10}$";
    private final String phoneNumberRegex = "^[2-9]\\d{2}-\\d{3}-\\d{4}$";
    private Pattern regexPattern;
    private Matcher regMatcher;

    public Boolean validateEmailAddress(String emailAddress) {
        this.regexPattern = Pattern.compile(this.emailRegex);
        this.regMatcher = this.regexPattern.matcher(emailAddress);
        return this.regMatcher.matches() ? true : false;
    }

    public Boolean validateMobileNumber(String mobileNumber) {
        this.regexPattern = Pattern.compile(this.mobileNumberRegex);
        this.regMatcher = this.regexPattern.matcher(mobileNumber);
        return this.regMatcher.matches() ? true : false;
    }

    // us 800-555-5555 | 333-444-5555 | 212-666-1234
    public Boolean validatePhoneNumber(String phoneNumber) {
        this.regexPattern = Pattern.compile(this.phoneNumberRegex);
        this.regMatcher = this.regexPattern.matcher(phoneNumber);
        return this.regMatcher.matches() ? true : false;
    }

    public Boolean isVaidUrl(String url) {
        boolean isValid = false;
        if (url.toLowerCase().contains("www.")) {
            url.replaceAll("www.", "");
            String[] strUrl = url.replace(".", "=").split("=");
            if (strUrl.length > 2) {
                String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
                isValid = url.matches(regex);
            } else {
                return false;
            }
        } else {
            String[] strUrl = url.replace(".", "=").split("=");
            if (strUrl.length > 1) {
                String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
                isValid = url.matches(regex);
            } else {
                return false;
            }
        }
        return isValid;
    }

}
