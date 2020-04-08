package pers.wesley.common.validation.validator;

import pers.wesley.common.validation.constraints.Mobile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @Description : 手机号码格式校验
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/02 16:47
 */
public class MobileValidator implements ConstraintValidator<Mobile, String> {

    private Pattern pattern = Pattern.compile("^((10[0-9])|(11[0-9])|(12[0-9])|(13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$");

    @Override
    public void initialize(Mobile constraintAnnotation) {
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
