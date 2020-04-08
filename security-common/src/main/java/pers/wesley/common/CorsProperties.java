package pers.wesley.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

/**
 * @Description : Cors配置信息
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/08 10:54
 */
@ConfigurationProperties(prefix = "pers.security.cors")
@Setter
@Getter
public class CorsProperties {

    private String allowOrigin = "*";

    private String allowMethods = "*";

    private Long maxAges = 18000L;

    private Boolean allowCredentials = Boolean.TRUE;

    private List<String> allowHeaders = Arrays.asList("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS");

    private List<String> exposedHeaders = Arrays.asList("Content-Disposition");
}
