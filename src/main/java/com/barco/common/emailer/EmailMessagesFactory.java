package com.barco.common.emailer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * @author Nabeel Ahmed
 */
@Component
public class EmailMessagesFactory {

    public Logger logger = LogManager.getLogger(EmailMessagesFactory.class);

    public EmailMessagesFactory() {}


}
