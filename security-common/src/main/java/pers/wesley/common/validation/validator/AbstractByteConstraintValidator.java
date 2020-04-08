package pers.wesley.common.validation.validator;

import javax.validation.ConstraintValidator;
import java.lang.annotation.Annotation;
import java.nio.charset.Charset;

/**
 * @Description : 字符串字节长度
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/03 10:18
 */
public abstract class AbstractByteConstraintValidator<A extends Annotation> implements ConstraintValidator<A, String> {

    protected int getByteLength(String value, String charset) {
        return value.getBytes(Charset.forName(charset)).length;
    }
}
