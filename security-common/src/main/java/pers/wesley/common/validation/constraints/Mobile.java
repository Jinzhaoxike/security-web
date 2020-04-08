package pers.wesley.common.validation.constraints;

import pers.wesley.common.validation.validator.MobileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Description :
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/02 16:46
 */
@Documented
@Constraint(validatedBy = MobileValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface Mobile {

    String message() default "{security.validation.constraints.Mobile.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
