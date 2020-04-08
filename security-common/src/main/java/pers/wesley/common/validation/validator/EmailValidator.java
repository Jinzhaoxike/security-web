package pers.wesley.common.validation.validator;

import pers.wesley.common.validation.constraints.Email;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @Description : 邮件地址格式校验
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/02 17:01
 */
public class EmailValidator implements ConstraintValidator<Email, String> {

    private Pattern pattern = Pattern.compile("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?");

    @Override
    public void initialize(Email constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (null == value || value.length() == 0) {
            return true;
        }
        if (pattern.matcher(value).matches()) {
            return true;
        }
        return false;
    }
}
