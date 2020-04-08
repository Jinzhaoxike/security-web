package pers.wesley.common.validation.constraints;

import pers.wesley.common.validation.validator.DateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Description : 日期格式校验
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/01 14:30
 */
@Documented
@Constraint(validatedBy = DateValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface Date {

    String message() default "{security.validation.constraints.Date.message}";

    DatePattern pattern() default DatePattern.yyyyMMdd;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    enum  DatePattern {
        yyyyMMdd("yyyyMMdd"),
        yyyy_MM_dd("yyyy-MM-dd");

        private final String value;

        DatePattern(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }
}
