package pers.wesley.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.slf4j.helpers.MessageFormatter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Description : 异常统一应答报文
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/08 14:13
 */
@Getter
@AllArgsConstructor
@ToString
public class ErrorResponse implements Serializable {

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 返回时间
     */
    private String timestamp;

    /**
     *
     * @param errorCode
     * @param message
     * @return
     */
    public static ErrorResponse of(String errorCode, String message) {
        return new ErrorResponse(errorCode, message, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    /**
     *
     * @param errorCode
     * @param message
     * @param args
     * @return
     */
    public static ErrorResponse of(String errorCode, String message, String ...args) {
        return of(errorCode, MessageFormatter.arrayFormat(message, args).getMessage());
    }

    /**
     *
     * @param e
     * @return
     */
    public static ErrorResponse of(Throwable e) {
        BaseException be;
        if (e instanceof BaseException) {
            be = (BaseException)e;
        } else if (null != e.getCause() && e.getCause() instanceof BaseException) {
            be = (BaseException)e.getCause();
        } else {
            be = new BaseException(ErrorCodeEnum.UNKNOWN_ERROR, e);
        }
        return of(be.getErrorCode().name(), be.getMessage());
    }
}
