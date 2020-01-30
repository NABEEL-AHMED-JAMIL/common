package com.barco.common.emailer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Nabeel.amd
 */
@Component
@Scope("prototype")
public class EmailContentCreateFactory {

    public Logger logger = LogManager.getLogger(EmailContentCreateFactory.class);
}
