package pers.wesley.common.exception;

/**
 * @Description : 已存在异常
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/03/30 16:02
 */
public class ExistsException extends BaseException {

    public ExistsException() {
        super(ErrorCodeEnum.EXISTS);
    }

    public ExistsException(String message) {
        super(ErrorCodeEnum.EXISTS, message);
    }

    public ExistsException(String[] args) {
        super(ErrorCodeEnum.EXISTS, args);
    }

    public ExistsException(String message, String[] args) {
        super(ErrorCodeEnum.EXISTS, message, args);
    }
}
