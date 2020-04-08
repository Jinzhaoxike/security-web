package pers.wesley.common.validation.validator;

import pers.wesley.common.validation.constraints.DateTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Description : 日期时间格式校验
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/02 16:35
 */
public class DateTimeValidator implements ConstraintValidator<DateTime, String> {

    private String patten;

    @Override
    public void initialize(DateTime constraintAnnotation) {
        patten = constraintAnnotation.pattern().getValue();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (null == value || value.length() == 0) {
            return true;
        }
        try {
            LocalDateTime.parse(value, DateTimeFormatter.ofPattern(patten));
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
