package pers.wesley.common.validation.validator;

import pers.wesley.common.validation.constraints.Time;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @Description : 时间格式校验
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/02 16:29
 */
public class TimeValidator implements ConstraintValidator<Time, String> {

    private String patten;
    @Override
    public void initialize(Time constraintAnnotation) {
        patten = constraintAnnotation.pattern().getValue();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (null == value || value.length() == 0) {
            return true;
        }
        try {
            LocalTime.parse(value, DateTimeFormatter.ofPattern(patten));
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
