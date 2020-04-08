package pers.wesley.common.exception;

/**
 * @Description : 未找到信息异常
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/03/30 16:01
 */
public class NotFoundException extends BaseException {

    public NotFoundException() {
        super(ErrorCodeEnum.NOT_FOUND);
    }

    public NotFoundException(String message) {
        super(ErrorCodeEnum.NOT_FOUND, message);
    }

    public NotFoundException(String[] args) {
        super(ErrorCodeEnum.NOT_FOUND, args);
    }

    public NotFoundException(String message, String[] args) {
        super(ErrorCodeEnum.NOT_FOUND, message, args);
    }
}
