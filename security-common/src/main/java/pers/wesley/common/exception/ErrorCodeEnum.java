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
    NOT_FOUND_ERROR("未找到{}"),
    /**
     * 已存在
     */
    EXISTS_ERROR("{}已存在"),
    /**
     * 网络连接
     */
    NETWORK_CONNECTION_ERROR("请求{}失败，{}"),
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
    ENCRYPT_ERROR("[{}]加密错误"),
    /**
     * 解密错误
     */
    DECRYPT_ERROR("[{}]解密错误"),
    /**
     * json转换错误
     */
    JSON_ERROR("json转换错误"),
    /**
     * 身份认证错误
     */
    AUTHENTICATION_ERROR("身份认证认证错误，{}"),
    /**
     * 权限认证错误
     */
    AUTHORIZATION_ERROR("权限认证错误，{}");

    private String message;

    ErrorCodeEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
