package pers.wesley.common.validation.constraints;

import pers.wesley.common.validation.validator.TimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Description : 时间格式校验
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/02 16:27
 */
@Documented
@Constraint(validatedBy = TimeValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface Time {

    String message() default "{security.validation.constraints.Time.message}";

    TimePattern pattern() default TimePattern.HHmmss;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    enum  TimePattern {
        HHmmss("HHmmss"),
        HH_mm_ss("HH:mm:ss");
        private final String value;

        TimePattern(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }
}
