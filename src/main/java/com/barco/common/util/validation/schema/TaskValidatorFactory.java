package com.barco.common.util.validation.schema;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Nabeel.amd
 */
public class TaskValidatorFactory {

    public static Logger logger = LogManager.getLogger(TaskValidatorFactory.class);

    private static final ITaskValidator SegmentProcessTaskValidator =
            new GenericTaskValidator("/schemas/segment_process_task_schema.json");

    private static final ITaskValidator WebScrapingProcessTaskValidator =
            new GenericTaskValidator("/schemas/web_scraping_process_task_schema.json");


    public static ITaskValidator getValidator(TaskInputType type) {
        logger.info("Schema Type :- " + type.name());
        ITaskValidator validator = null;
        if (type != null) {
            switch (type) {
                case SegmentProcessTask:
                    validator = SegmentProcessTaskValidator;
                    break;
                case WebScrapingProcessTask:
                    validator = WebScrapingProcessTaskValidator;
                    break;
            }
        }
        return validator;
    }

}
