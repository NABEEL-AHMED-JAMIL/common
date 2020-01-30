package com.barco.common.util.validation.annotation;

import com.barco.common.util.validation.constrain.RequestParamValidator;
import org.springframework.core.annotation.AliasFor;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Nabeel.amd
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { RequestParamValidator.class })
@Target({ ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD })
public @interface RequestParam {

    String message() default "Filed Required For Query";

    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    boolean required() default true;

    String defaultValue() default "";

    Class<?>[] groups() default  {};

    Class<? extends Payload>[] payload() default {};
}