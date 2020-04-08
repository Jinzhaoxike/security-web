package pers.wesley.common.exception;

/**
 * @Description : 错误代码
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/08 14:15
 */
public enum ErrorCodeEnum {

    /**
     * 参数校验
     */
    PARAMETER_ERROR("参数错误，字段({}){}"),
    /**
     * 未找到信息
     */
    NOT_FOUND("未找到{}"),
    /**
     * 已存在
     */
    EXISTS("{}已存在"),
    /**
     * 网络连接
     */
    NETWORK_CONNECTION("请求{}失败，{}"),
    /**
     * 系统未知异常
     */
    UNKNOWN_ERROR("系统未知错误，请联系客服"),
    /**
     * 验证码生成错误
     */
    CAPTCHA_GENERATE_ERROR("验证码生成错误"),
    /**
     * 验证码错误
     */
    CAPTCHA_ERROR("验证码错误"),
    /**
     * 获取验证码太频繁
     */
    CAPTCHA_FREQUENT_ERROR("获取验证码太频繁"),
    /**
     * 加密错误
     */
    ENCRYPT("[{}]加密错误"),
    /**
     * 解密错误
     */
    DECRYPT("[{}]解密错误");

    private String message;

    ErrorCodeEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
