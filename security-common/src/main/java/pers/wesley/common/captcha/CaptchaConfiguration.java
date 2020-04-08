package pers.wesley.common.captcha;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @Description : 验证码配置
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/08 13:45
 */
@Configuration
@EnableConfigurationProperties(value = {CaptchaProperties.class})
public class CaptchaConfiguration {

    private CaptchaProperties captchaProperties;

    public CaptchaConfiguration(CaptchaProperties captchaProperties) {
        this.captchaProperties = captchaProperties;
    }

    @Bean
    public Producer kaptchaProducer() {
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        captchaProperties.getKaptchaConfig().forEach((k, v) -> properties.setProperty(k, v));
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }
}
