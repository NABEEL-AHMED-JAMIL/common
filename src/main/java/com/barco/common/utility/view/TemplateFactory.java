package com.barco.common.utility.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Nabeel Ahmed
 */
@Component
@Scope("prototype")
public class TemplateFactory {

    private Logger logger = LogManager.getLogger(TemplateFactory.class);

    public final String EMAIL_ACCOUNT_CREATED_TEMPLATE_PATH = "templates/email_account_created.vm";
    public final String FORGOT_PASSWORD_TEMPLATE_PATH = "templates/forget_password.vm";

    private Template template;
    private VelocityEngine engine;
    private static volatile boolean isRDInitialized = false;

    @PostConstruct
    public void init() {
        if (!isRDInitialized) {
            logger.info("+================Velocity-Start====================+");
            this.engine = getEngine();
            this.engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
            this.engine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
            this.engine.init();
            logger.info("+================Velocity-End====================+");
            isRDInitialized = true;
        }
    }

    public TemplateFactory() { }

    public Template getTemplate(TemplateType templateType) {
        switch (templateType) {
            case EMAIL_ACCOUNT_CREATED_TEMPLATE:
                //logger.debug("Error-Template Path :- " + this.getERROR_TEMPLATE_PATH());
                this.template = this.engine.getTemplate(EMAIL_ACCOUNT_CREATED_TEMPLATE_PATH);
                break;
            case FORGOT_PASSWORD_TEMPLATE:
                //logger.debug("Error-Template Path :- " + this.getERROR_TEMPLATE_PATH());
                this.template = this.engine.getTemplate(FORGOT_PASSWORD_TEMPLATE_PATH);
                break;
        }
        return template;
    }

    private VelocityEngine getEngine() { return new VelocityEngine(); }

}
