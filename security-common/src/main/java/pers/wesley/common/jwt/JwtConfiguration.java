package pers.wesley.common.jwt;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description : jwt配置
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/08 16:59
 */
@Configuration
@EnableConfigurationProperties(value = {JwtProperties.class})
public class JwtConfiguration {

    @Bean
    public JwtTokenGenerate jwtTokenGenerate(JwtProperties jwtProperties) {
        return new JwtTokenGenerate(jwtProperties);
    }
}
