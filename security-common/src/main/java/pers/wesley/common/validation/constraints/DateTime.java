package pers.wesley.common.validation.constraints;

import pers.wesley.common.validation.validator.DateTimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Description : 日期时间格式校验
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/02 16:33
 */
@Documented
@Constraint(validatedBy = DateTimeValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface DateTime {

    String message() default "{security.validation.constraints.DateTime.message}";

    DateTimePattern pattern() default DateTimePattern.yyyyMMddHHmmss;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    enum  DateTimePattern {
        yyyyMMddHHmmss("yyyyMMddHHmmss"),
        yyyy_MM_dd_HH_mm_ss("yyyy-MM-dd HH:mm:ss");

        private final String value;

        DateTimePattern(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }
}
