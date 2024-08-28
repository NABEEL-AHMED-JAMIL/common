package com.barco.common.emailer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Nabeel Ahmed
 */
public class EmailUtil {

    private Logger logger = LoggerFactory.getLogger(EmailUtil.class);

    // title
    public static String USER_REGISTERED = "ETL User Registered";
    public static String ORG_ACCOUNT_REGISTERED = "ETL Org Account Registered";
    public static String FORGOT_PASSWORD = "ETL Forgot Password";
    public static String PASSWORD_UPDATED = "ETL Password Updated";
    public static String YOUR_ACCOUNT_IS_NOW_ACTIVE = "ETL Your Account is Now Active";
    public static String YOUR_ACCOUNT_HAS_BEEN_BLOCKED = "ETL Your Account Has Been Blocked";

    // filed
    public static String USERNAME = "username";
    public static String FULL_NAME = "fullName";
    public static String ROLE = "role";
    public static String PROFILE = "profile";
    public static String ORG_NAME = "orgName";
    public static String ORG_ADDRESS = "orgAddress";
    public static String FORGOT_PASSWORD_URL = "forgotPasswordPageUrl";

}
