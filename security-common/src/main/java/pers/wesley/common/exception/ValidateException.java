package pers.wesley.common.exception;

/**
 * @Description : 参数校验异常
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/03/30 15:02
 */
public class ValidateException extends BaseException {

    public ValidateException() {
        super(ErrorCodeEnum.PARAMETER_ERROR);
    }

    public ValidateException(String message) {
        super(ErrorCodeEnum.PARAMETER_ERROR, message);
    }

    public ValidateException(String[] args) {
        super(ErrorCodeEnum.PARAMETER_ERROR, args);
    }

    public ValidateException(String message, String[] args) {
        super(ErrorCodeEnum.PARAMETER_ERROR, message, args);
    }
}
