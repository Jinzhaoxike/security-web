package pers.wesley.common.validation.validator;

import pers.wesley.common.validation.constraints.RangeByte;

import javax.validation.ConstraintValidatorContext;

/**
 * @Description : 字节长度范围校验
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/03 13:48
 */
public class RangeByteValidator extends AbstractByteConstraintValidator<RangeByte> {

    private long min;

    private long max;

    private String charset;

    @Override
    public void initialize(RangeByte constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
        charset = constraintAnnotation.charset();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (null == value || value.length() == 0) {
            return true;
        }
        int length = getByteLength(value, charset);
        return min <= length && length <=max;
    }
}
