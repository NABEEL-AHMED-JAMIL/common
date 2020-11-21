package com.barco.common.utility.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.StringWriter;
import java.util.Map;

import static com.barco.common.utility.view.TemplateType.EMAIL_ACCOUNT_CREATED_TEMPLATE;
import static com.barco.common.utility.view.TemplateType.FORGOT_PASSWORD_TEMPLATE;

/**
 * @author Nabeel Ahmed
 */
@Component
@Scope("prototype")
public class VelocityManager {

    private final Logger logger = LogManager.getLogger(VelocityManager.class);

    @Autowired
    private TemplateFactory templateFactory;
    /*  create a context and add data */
    private VelocityContext context;
    /* now render the template into a StringWriter */
    private StringWriter writer;

    public VelocityContext getContext() { return context; }
    public void setContext(VelocityContext context) { this.context = context; }

    public String getResponseMessage(TemplateType templateType, Map<String, Object> object) throws Exception {
        String responseMessage = null;
        this.setWriter(new StringWriter());
        this.setContext(new VelocityContext());
        if(templateType.equals(EMAIL_ACCOUNT_CREATED_TEMPLATE)) {
            //logger.info("Request Content :- " + object);
            this.context.put("request", object);
            responseMessage = this.getWriterResponse(templateType).toString();
        } else if(templateType.equals(FORGOT_PASSWORD_TEMPLATE)) {
            //logger.info("Request Content :- " + object);
            this.context.put("request", object);
            responseMessage = this.getWriterResponse(templateType).toString();
        }
        return responseMessage;
    }

    private StringWriter getWriterResponse(TemplateType templateType) throws Exception {
        Template template = this.templateFactory.getTemplate(templateType);
        if(template != null) {
            template.merge(this.getContext(), this.getWriter());
            //logger.info("Response Content :- " + this.getWriter().toString().replaceAll("\\s+",""));
            return this.getWriter();
        }
        throw new NullPointerException("Template Not Found");
    }

    public StringWriter getWriter() { return writer; }
    public void setWriter(StringWriter writer) { this.writer = writer; }

}
