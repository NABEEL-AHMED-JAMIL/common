package com.barco.common.util.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.StringWriter;
import java.util.HashMap;

import static com.barco.common.util.view.TemplateType.*;

/**
 * @author Nabeel.amd
 */
@Component
@Scope("prototype")
public class VelocityManager {

    private final Logger logger = LogManager.getLogger(VelocityManager.class);

    private final String REQUEST = "request";

    @Autowired
    private TemplateFactory templateFactory;
    /*  create a context and add data */
    private VelocityContext context;
    /* now render the template into a StringWriter */
    private StringWriter writer;

    public VelocityContext getContext() { return context; }
    public void setContext(VelocityContext context) { this.context = context; }

    public String getResponseMessage(TemplateType templateType, HashMap<String, Object> metadata) throws Exception {
        String responseMessage = null;
        this.setWriter(new StringWriter());
        this.setContext(new VelocityContext());
        if(templateType.equals(EMAIL_ACCOUNT_CREATED_TEMPLATE)) {
            this.context.put(REQUEST, metadata);
            responseMessage = this.getWriterResponse(templateType).toString();
        } else if(templateType.equals(FORGET_PASSWORD_TEMPLATE)) {
            this.context.put(REQUEST, metadata);
            responseMessage = this.getWriterResponse(templateType).toString();
        } else if(templateType.equals(USER_REGISTRATIONS_TEMPLATE)) {
            this.context.put(REQUEST, metadata);
            responseMessage = this.getWriterResponse(templateType).toString();
        }
        return responseMessage;
    }

    private StringWriter getWriterResponse(TemplateType templateType) throws Exception {
        Template template = this.templateFactory.getTemplate(templateType);
        if(template != null) {
            template.merge(this.getContext(), this.getWriter());
            return this.getWriter();
        }
        throw new NullPointerException("Template Not Found");
    }

    public StringWriter getWriter() { return writer; }
    public void setWriter(StringWriter writer) { this.writer = writer; }

}
