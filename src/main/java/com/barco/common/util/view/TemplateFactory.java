package com.barco.common.util.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Nabeel.amd
 */
@Component
@Scope("prototype")
public class TemplateFactory {

    private Logger logger = LogManager.getLogger(TemplateFactory.class);

    public final String EMAIL_ACCOUNT_CREATED_TEMPLATE_PATH = "templates/email_account_created.vm";
    public final String FORGET_PASSWORD_TEMPLATE_PATH = "templates/forget_password.vm";
    public final String USER_REGISTRATIONS_TEMPLATE_PATH = "templates/user_registrations.vm";

    private Template template;
    private VelocityEngine engine;

    public TemplateFactory() { }

    public Template getTemplate(TemplateType templateType) {
        this.engine = this.getEngine();
        this.engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        this.engine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        this.engine.init();
        switch (templateType) {
            case EMAIL_ACCOUNT_CREATED_TEMPLATE:
                this.template = this.engine.getTemplate(EMAIL_ACCOUNT_CREATED_TEMPLATE_PATH);
                break;
            case FORGET_PASSWORD_TEMPLATE:
                this.template = this.engine.getTemplate(FORGET_PASSWORD_TEMPLATE_PATH);
                break;
            case USER_REGISTRATIONS_TEMPLATE:
                this.template = this.engine.getTemplate(USER_REGISTRATIONS_TEMPLATE_PATH);
                break;
        }
        return this.template;
    }

    private VelocityEngine getEngine() { return new VelocityEngine(); }

}
