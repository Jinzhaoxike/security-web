package pers.wesley.common.exception;

import org.slf4j.helpers.MessageFormatter;

/**
 * @Description :
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/08 14:14
 */
public class BaseException extends RuntimeException {
    private ErrorCodeEnum errorCode;

    private String message;

    private String[] args;

    public BaseException(ErrorCodeEnum errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public BaseException(ErrorCodeEnum errorCode, String[] args) {
        super();
        this.errorCode = errorCode;
        this.args = args;
    }

    public BaseException(ErrorCodeEnum errorCode, String message) {
        super();
        this.errorCode = errorCode;
        this.message = message;
    }

    public BaseException(ErrorCodeEnum errorCode, String message, String[] args) {
        super();
        this.errorCode = errorCode;
        this.message = message;
        this.args = args;
    }

    public BaseException(ErrorCodeEnum errorCode, Throwable throwable) {
        this(errorCode, null, null, throwable);
    }

    public BaseException(ErrorCodeEnum errorCode, String[] args, Throwable throwable) {
        this(errorCode, null, args, throwable);
    }

    public BaseException(ErrorCodeEnum errorCode, String message, Throwable throwable) {
        this(errorCode, message, null, throwable);
    }

    public BaseException(ErrorCodeEnum errorCode, String message, String[] args, Throwable throwable) {
        super(throwable);
        this.errorCode = errorCode;
        this.message = message;
        this.args = args;
    }

    @Override
    public String getMessage() {
        String formatMessage = message;
        if (null == formatMessage || formatMessage.length() == 0) {
            formatMessage = getErrorCode().getMessage();
        }
        if (null == getArgs() || getArgs().length == 0) {
            return formatMessage;
        }
        return MessageFormatter.arrayFormat(formatMessage, getArgs()).getMessage();
    }

    public ErrorCodeEnum getErrorCode() {
        return errorCode;
    }

    public String[] getArgs() {
        return args;
    }
}
