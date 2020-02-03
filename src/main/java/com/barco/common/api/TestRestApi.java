package com.barco.common.api;

import com.barco.common.util.validation.annotation.RequestParam;
import io.swagger.annotations.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test.json", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = { "Barco-Test := Test Data EndPoint" })
public class TestRestApi {

    public Logger logger = LogManager.getLogger(TestRestApi.class);

    private String json = "{ \"status\": \"%s\" }";

    @RequestMapping(value = "/email", method = RequestMethod.POST)
    public ResponseEntity<?> emailTest(@RequestParam Long templateId) {
        long startTime = System.currentTimeMillis();
        try {
        } catch (Exception ex) {
            json = String.format(json, "Fail");
        }
        logger.debug("schemaTest : " + json +
            " Process Time :- " + (startTime-System.currentTimeMillis()) + ".ms");
        return ResponseEntity.ok(json);
    }
}
