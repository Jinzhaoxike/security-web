package pers.wesley.common.exception;

/**
 * @Description : 网络请求异常
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/03/30 16:08
 */
public class NetworkConnectionException extends BaseException {

    public NetworkConnectionException() {
        super(ErrorCodeEnum.NETWORK_CONNECTION);
    }

    public NetworkConnectionException(String message) {
        super(ErrorCodeEnum.NETWORK_CONNECTION, message);
    }

    public NetworkConnectionException(String[] args) {
        super(ErrorCodeEnum.NETWORK_CONNECTION, args);
    }

    public NetworkConnectionException(String message, String[] args) {
        super(ErrorCodeEnum.NETWORK_CONNECTION, message, args);
    }
}
