package pers.wesley.webflux.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import pers.wesley.common.SecurityProperties;
import pers.wesley.common.exception.BaseException;
import pers.wesley.common.exception.ErrorCodeEnum;
import pers.wesley.common.exception.ErrorResponse;
import pers.wesley.common.jwt.JwtTokenGenerate;
import reactor.core.publisher.Mono;

/**
 * @Description : 权限校验
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/15 15:12
 */
@Slf4j
public class AuthorizationWebFilter implements WebFilter {

    private static final String JWT_TOKEN_HEADER_PARAM = "X-Authorization";

    private static final String HEADER_PREFIX = "Bearer ";

    private final NegatedServerWebExchangeMatcher negatedServerWebExchangeMatcher;

    private final JwtTokenGenerate jwtTokenGenerate;

    public AuthorizationWebFilter(SecurityProperties securityProperties, JwtTokenGenerate jwtTokenGenerate) {
        negatedServerWebExchangeMatcher = new NegatedServerWebExchangeMatcher(ServerWebExchangeMatchers.pathMatchers(securityProperties.getPermitUrl().toArray(new String[securityProperties.getPermitUrl().size()])));
        this.jwtTokenGenerate = jwtTokenGenerate;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return negatedServerWebExchangeMatcher
                .matches(exchange)
                .filter(matchResult -> matchResult.isMatch())
                .flatMap(matchResult -> convert(exchange))
                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
                .flatMap(token -> authorization(exchange, chain, token))
                .onErrorResume(BaseException.class, e -> {
                    ErrorResponse errorResponse = ErrorResponse.of(e);
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    return response.writeWith(Mono.just(response.bufferFactory().wrap(errorResponse.toJson().getBytes())));
                }).onErrorResume(Exception.class, e -> {
                    log.error("权限校验异常", e);
                    ErrorResponse errorResponse = ErrorResponse.of(e);
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    return response.writeWith(Mono.just(response.bufferFactory().wrap(errorResponse.toJson().getBytes())));
                })
                ;
    }

    private Mono<Authentication> convert(ServerWebExchange exchange) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        String token = serverHttpRequest.getHeaders().getFirst(JWT_TOKEN_HEADER_PARAM);
        if (StringUtils.isEmpty(token) || !token.startsWith(HEADER_PREFIX)) {
            return Mono.empty();
        }
        return Mono.just(jwtTokenGenerate.parseToken(token.substring(HEADER_PREFIX.length())));
    }

    private Mono<Void> authorization(ServerWebExchange exchange, WebFilterChain chain, Authentication authentication) {

        String path = exchange.getRequest().getURI().getPath();
        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority(path))) {
            throw new BaseException(ErrorCodeEnum.AUTHENTICATION_ERROR, "无访问权限");
        }

        WebFilterExchange webFilterExchange = new WebFilterExchange(exchange, chain);
        SecurityContextImpl securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(authentication);
        return webFilterExchange.getChain().filter(exchange).subscriberContext(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
    }
}