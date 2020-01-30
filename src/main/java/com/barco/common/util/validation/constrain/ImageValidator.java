package com.barco.common.util.validation.constrain;


import com.barco.common.util.validation.annotation.ValidImage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Nabeel.amd
 */
public class ImageValidator implements ConstraintValidator<ValidImage, MultipartFile> {

    public Logger logger = LogManager.getLogger(ImageValidator.class);

    private String fileType[] = { "image/png", "image/jpg", "image/jpeg" };

    @Override
    public void initialize(ValidImage constraintAnnotation) { }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
        Boolean result = true;
        if(!ObjectUtils.isEmpty(multipartFile)) {
            if(!isSupportedContentType(multipartFile.getContentType())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Only PNG or JPG image are allowed.")
                .addConstraintViolation();
                result = false;
            }
        }else {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File object null pls enter PNG or JPG image.")
            .addConstraintViolation();
            result = false;
        }
        return result;
    }

    public Boolean isSupportedContentType(String contentType) {
        return contentType.equals(fileType[0]) || contentType.equals(fileType[1]) || contentType.equals(fileType[2]);
    }
}
