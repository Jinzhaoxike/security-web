package pers.wesley.common.validation.validator;

import pers.wesley.common.validation.constraints.In;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * @Description : 类型校验
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/01 11:33
 */
public class InValidator implements ConstraintValidator<In, String> {

    private final Set<String> values = new HashSet<>(8);

    @Override
    public void initialize(In constraintAnnotation) {
        for (String value : constraintAnnotation.value()) {
            values.add(value);
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (null == value || value.length() == 0) {
            return true;
        }
        return values.contains(value);
    }
}
