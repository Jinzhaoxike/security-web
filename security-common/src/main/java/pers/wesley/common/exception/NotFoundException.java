package pers.wesley.common.exception;

/**
 * @Description : 未找到信息异常
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/03/30 16:01
 */
public class NotFoundException extends BaseException {

    public NotFoundException() {
        super(ErrorCodeEnum.NOT_FOUND_ERROR);
    }

    public NotFoundException(String message) {
        super(ErrorCodeEnum.NOT_FOUND_ERROR, message);
    }

    public NotFoundException(String[] args) {
        super(ErrorCodeEnum.NOT_FOUND_ERROR, args);
    }

    public NotFoundException(String message, String[] args) {
        super(ErrorCodeEnum.NOT_FOUND_ERROR, message, args);
    }
}
