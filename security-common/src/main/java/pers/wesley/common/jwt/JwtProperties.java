package pers.wesley.common.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description : jwt配置参数
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/08 16:56
 */
@ConfigurationProperties(prefix = "pers.security.jwt")
@Setter
@Getter
public class JwtProperties {

    /**
     * token有效时间，单位秒
     */
    private Long expireTime = 120L;
    /**
     * token签名密钥
     */
    private String signKey = "E10ADC3949BA59ABBE56E057F20F884E";
    /**
     * token des签名密钥
     */
    private String desKey = "AB8238DC";
}
