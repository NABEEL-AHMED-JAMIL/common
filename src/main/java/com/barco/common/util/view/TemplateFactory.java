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

    public final String DAILY_FILE_STATUS_TEMPLATE_PATH = "templates/daily_file_status.vm";

    private Template template;
    private VelocityEngine engine;

    public TemplateFactory() { }

    public Template getTemplate(TemplateType templateType) {
        this.engine = this.getEngine();
        this.engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        this.engine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        this.engine.init();
        switch (templateType) {
            case DAILY_FILE_STATUS:
                this.template = this.engine.getTemplate(DAILY_FILE_STATUS_TEMPLATE_PATH);
                break;
        }
        return this.template;
    }

    private VelocityEngine getEngine() { return new VelocityEngine(); }

}
