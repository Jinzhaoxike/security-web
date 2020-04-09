package pers.wesley.webflux;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DelegatingReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import pers.wesley.common.SecurityProperties;
import pers.wesley.common.security.User;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

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

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveUserDetailsService reactiveUserDetailsService(BCryptPasswordEncoder bCryptPasswordEncoder) {
        return username -> {
            if (!"admin".equals(username)) {
                return Mono.empty();
            }
            Map<String, Object> reserve = new HashMap<>(4);
            reserve.put("userType", "pltadmin");
            UserDetails userDetails = User
                    .withUsername("admin")
                    .authorities("/user/permission/read")
                    .password("123456")
                    .passwordEncoder(bCryptPasswordEncoder::encode)
                    .reserve(reserve)
                    .build();
            return Mono.just(User.withUserDetails(userDetails).build());
        };
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveUserDetailsService reactiveUserDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        UserDetailsRepositoryReactiveAuthenticationManager userDetailsRepositoryReactiveAuthenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(reactiveUserDetailsService);
        userDetailsRepositoryReactiveAuthenticationManager.setPasswordEncoder(bCryptPasswordEncoder);
        return new DelegatingReactiveAuthenticationManager(userDetailsRepositoryReactiveAuthenticationManager);
    }
}
