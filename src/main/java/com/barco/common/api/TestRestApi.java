package com.barco.common.api;

import com.barco.common.util.validation.annotation.RequestParam;
import com.barco.common.util.validation.schema.ITaskValidator;
import com.barco.common.util.validation.schema.TaskInputType;
import com.barco.common.util.validation.schema.TaskValidatorFactory;
import io.swagger.annotations.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test.json", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = { "Barco-Test := Test Data EndPoint" })
public class TestRestApi {

    public Logger logger = LogManager.getLogger(TestRestApi.class);

    private ITaskValidator taskValidator;

    private String json = "{ \"status\": \"%s\" }";

    // for change password0
    @RequestMapping(value = "/schema/web/scraping", method = RequestMethod.POST)
    public ResponseEntity<?> schemaWebScraping(@RequestBody String jsonRequest) {
        long startTime = System.currentTimeMillis();
        String tempJson = json;
        try {
            this.taskValidator = TaskValidatorFactory.getValidator(TaskInputType.WebScrapingProcessTask);
            tempJson = String.format(tempJson, taskValidator.isValid(jsonRequest) ? "Pass" : "Fail");
        } catch (Exception ex) {
            tempJson = String.format(tempJson, "Fail");
        }
        logger.debug("schemaTest : " + tempJson + " Process Time :- " + (startTime-System.currentTimeMillis()) + ".ms");
        return ResponseEntity.ok(tempJson);
    }

    @RequestMapping(value = "/schema/segment", method = RequestMethod.POST)
    public ResponseEntity<?> schemaTest(@RequestBody String jsonRequest) {
        long startTime = System.currentTimeMillis();
        try {
            this.taskValidator = TaskValidatorFactory.getValidator(TaskInputType.SegmentProcessTask);
            json = String.format(json, taskValidator.isValid(jsonRequest) ? "Pass" : "Fail");
        } catch (Exception ex) {
            json = String.format(json, "Fail");
        }
        logger.debug("schemaTest : " + json + " Process Time :- " + (startTime-System.currentTimeMillis()) + ".ms");
        return ResponseEntity.ok(json);
    }

    @RequestMapping(value = "/email", method = RequestMethod.POST)
    public ResponseEntity<?> emailTest(@RequestParam Long templateId) {
        long startTime = System.currentTimeMillis();
        try {
        } catch (Exception ex) {
            json = String.format(json, "Fail");
        }
        logger.debug("schemaTest : " + json + " Process Time :- " + (startTime-System.currentTimeMillis()) + ".ms");
        return ResponseEntity.ok(json);
    }
}
