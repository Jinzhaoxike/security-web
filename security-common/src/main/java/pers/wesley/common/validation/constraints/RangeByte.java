package pers.wesley.common.validation.constraints;

import pers.wesley.common.validation.validator.RangeByteValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Description : 字节长度范围校验
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/03 13:46
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {RangeByteValidator.class})
public @interface RangeByte {

    String message() default "{security.validation.constraints.RangeByte.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    long min();

    long max();

    String charset() default "UTF-8";
}
