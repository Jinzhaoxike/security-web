package pers.wesley.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @Description : 权限配置
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/08 10:49
 */
@ConfigurationProperties(prefix = "pers.security")
@Setter
@Getter
public class SecurityProperties {
    /**
     * 跳过权限验证url
     */
    private List<String> permitUrl;
}
