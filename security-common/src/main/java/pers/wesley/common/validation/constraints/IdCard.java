package pers.wesley.common.validation.constraints;

import pers.wesley.common.validation.validator.IdCardValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Description : 身份证格式校验
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/02 17:12
 */
@Documented
@Constraint(validatedBy = IdCardValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface IdCard {

    String message() default "{security.validation.constraints.IdCard.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
