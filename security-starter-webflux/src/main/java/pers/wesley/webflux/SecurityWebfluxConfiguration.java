package pers.wesley.webflux;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import pers.wesley.common.SecurityProperties;

/**
 * @Description : SecurityWebflux配置
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/08 10:46
 */
@Configuration
@EnableWebFluxSecurity
@EnableConfigurationProperties(value = {SecurityProperties.class})
public class SecurityWebfluxConfiguration {

    private SecurityProperties securityProperties;

    public SecurityWebfluxConfiguration(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeExchange()
                .pathMatchers(securityProperties.getPermitUrl().toArray(new String[securityProperties.getPermitUrl().size()])).permitAll()
                .anyExchange()
                .authenticated();
        return  httpSecurity.build();
    }
}
