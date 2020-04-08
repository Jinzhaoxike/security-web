package pers.wesley.common.validation.constraints;

import pers.wesley.common.validation.validator.InValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Description : 类型校验
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/01 11:28
 */
@Documented
@Constraint(validatedBy = InValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface In {

    String message() default "{security.validation.constraints.In.message}";

    String[] value() default {};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
