package com.barco.common.util.validation.annotation;

import com.barco.common.util.validation.constrain.ImageValidator;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Nabeel.amd
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { ImageValidator.class })
@Target({ ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD })
public @interface ValidImage {

    String message() default  "Invalid image repository";

    Class<?>[] groups() default  {};

    Class<? extends Payload>[] payload() default {};
}