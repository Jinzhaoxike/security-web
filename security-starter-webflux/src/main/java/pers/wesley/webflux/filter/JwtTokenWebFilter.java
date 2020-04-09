package pers.wesley.webflux.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import pers.wesley.common.SecurityProperties;
import reactor.core.publisher.Mono;

/**
 * @Description : jwt token 过滤器
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/08 18:22
 */
//@Configuration
@Slf4j
public class JwtTokenWebFilter implements WebFilter {

    private static final String JWT_TOKEN_HEADER_PARAM = "X-Authorization";

    private static final String HEADER_PREFIX = "Bearer ";

    private final ServerWebExchangeMatcher matcher;

    public JwtTokenWebFilter(SecurityProperties securityProperties) {
        matcher = ServerWebExchangeMatchers.pathMatchers(securityProperties.getPermitUrl().toArray(new String[securityProperties.getPermitUrl().size()]));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return matcher.matches(exchange)
                .filter(matchResult -> matchResult.isMatch())
                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
                .flatMap(matchResult -> {
                    log.info("matchResult = {}", matchResult);
                    return Mono.empty();
                });
    }

    private Mono<Void> errorResponse(ServerHttpResponse serverHttpResponse, String message) {
        return Mono.empty();
    }
}
