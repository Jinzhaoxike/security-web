package pers.wesley.common.validation.validator;

import pers.wesley.common.validation.constraints.MaxByte;

import javax.validation.ConstraintValidatorContext;

/**
 * @Description : 最大字节长度校验
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/03 10:45
 */
public class MaxByteValidator extends AbstractByteConstraintValidator<MaxByte> {
    private long max;
    private String charset;

    @Override
    public void initialize(MaxByte constraintAnnotation) {
        max = constraintAnnotation.value();
        charset = constraintAnnotation.charset();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (null == value || value.length() == 0) {
            return true;
        }
        int length = getByteLength(value, charset);
        return length <= max;
    }
}
