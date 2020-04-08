package pers.wesley.common.exception;

/**
 * @Description :
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/04 21:36
 */
public class CaptchaException extends BaseException {

    public CaptchaException(ErrorCodeEnum errorCode) {
        super(errorCode);
    }

    public CaptchaException(ErrorCodeEnum errorCode, String[] args) {
        super(errorCode, args);
    }

    public CaptchaException(ErrorCodeEnum errorCode, String message) {
        super(errorCode, message);
    }

    public CaptchaException(ErrorCodeEnum errorCode, String message, String[] args) {
        super(errorCode, message, args);
    }
}
