package pers.wesley.common.validation.validator;

import pers.wesley.common.validation.constraints.MinByte;

import javax.validation.ConstraintValidatorContext;

/**
 * @Description : 最小字节长度校验
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/03 10:16
 * 注：UTF-8编码格式中文占三个字节，GBK编码格式 中文占两个字节
 */
public class MinByteValidator extends AbstractByteConstraintValidator<MinByte> {

    private long min;

    private String charset;

    @Override
    public void initialize(MinByte constraintAnnotation) {
        min = constraintAnnotation.value();
        charset = constraintAnnotation.charset();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (null == value || value.length() == 0) {
            return true;
        }
        int length = getByteLength(value, charset);
        return length >= min;
    }
}
