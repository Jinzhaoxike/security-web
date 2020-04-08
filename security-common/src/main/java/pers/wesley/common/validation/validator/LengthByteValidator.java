package pers.wesley.common.validation.validator;

import pers.wesley.common.validation.constraints.LengthByte;

import javax.validation.ConstraintValidatorContext;

/**
 * @Description : 定长字节长度校验
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/03 11:00
 */
public class LengthByteValidator extends AbstractByteConstraintValidator<LengthByte> {

    private long fixedLength;
    private String charset;

    @Override
    public void initialize(LengthByte constraintAnnotation) {
        fixedLength = constraintAnnotation.value();
        charset = constraintAnnotation.charset();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (null == value || value.length() == 0) {
            return true;
        }
        int length = getByteLength(value, charset);
        return length == fixedLength;
    }
}
