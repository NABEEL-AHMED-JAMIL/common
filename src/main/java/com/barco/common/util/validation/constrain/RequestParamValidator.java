package com.barco.common.util.validation.constrain;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Nabeel.amd
 */
public class RequestParamValidator implements ConstraintValidator<RequestParam, String> {

    public Logger logger = LogManager.getLogger(RequestParamValidator.class);

    @Override
    public void initialize(RequestParam constraintAnnotation) { }

    @Override
    public boolean isValid(String filed, ConstraintValidatorContext constraintValidatorContext) {
        return StringUtils.isEmpty(filed);
    }
}
