package pers.wesley.common.validation.validator;

import pers.wesley.common.validation.constraints.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @Description : 日期格式校验
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/01 14:30
 */
public class DateValidator implements ConstraintValidator<Date, String> {

    private String patten;

    @Override
    public void initialize(Date constraintAnnotation) {
        patten = constraintAnnotation.pattern().getValue();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (null == value || value.length() == 0) {
            return true;
        }
        try {
            LocalDate.parse(value, DateTimeFormatter.ofPattern(patten));
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
