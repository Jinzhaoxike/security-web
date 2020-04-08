package pers.wesley.common.validation.constraints;

import pers.wesley.common.validation.validator.MinByteValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Description : 最小字节长度校验
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/03 10:13
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {MinByteValidator.class})
public @interface MinByte {

    String message() default "{security.validation.constraints.MinByte.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    /**
     * @return value the element must be higher or equal to
     */
    long value();

    String charset() default "UTF-8";
}
