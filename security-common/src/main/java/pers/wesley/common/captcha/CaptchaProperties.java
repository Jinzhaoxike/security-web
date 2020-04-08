package pers.wesley.common.captcha;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description : 验证码配置
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/08 13:40
 */
@ConfigurationProperties(prefix = "pers.security.captcha")
@Setter
@Getter
public class CaptchaProperties {
    /**
     * kaptcha配置
     */
    private Map<String, String> kaptchaConfig = new HashMap<>(4);

    /**
     * 图片验证码配置
     */
    private ImageCaptcha image = new ImageCaptcha();

    /**
     * 短信验证码配置
     */
    private SmsCaptcha sms = new SmsCaptcha();

    @Setter
    @Getter
    public static class ImageCaptcha {

        /**
         * 验证码有效时间，单位秒
         */
        private Long expireTime = 120L;
    }

    @Setter
    @Getter
    public static class SmsCaptcha {
        /**
         * 验证码有效时间，单位秒
         */
        private Long expireTime = 120L;
    }
}
