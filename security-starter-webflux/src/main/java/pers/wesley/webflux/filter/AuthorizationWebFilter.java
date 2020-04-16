package pers.wesley.webflux.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
import pers.wesley.common.security.PermissionUriConfiguration;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

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
        String token = exchange.getRequest().getHeaders().getFirst(JWT_TOKEN_HEADER_PARAM);
        if (StringUtils.isEmpty(token)) {
            throw new BaseException(ErrorCodeEnum.AUTHORIZATION_ERROR, "[X-Authorization]不存在");
        }
        if (!token.startsWith(HEADER_PREFIX)) {
            throw new BaseException(ErrorCodeEnum.AUTHORIZATION_ERROR, "[X-Authorization]格式错误");
        }
        return Mono.just(jwtTokenGenerate.parseToken(token.substring(HEADER_PREFIX.length())));
    }

    private Mono<Void> authorization(ServerWebExchange exchange, WebFilterChain chain, Authentication authentication) {

        ServerHttpRequest request = exchange.getRequest();

        Optional<? extends GrantedAuthority> optionalGrantedAuthority = authentication.getAuthorities()
                .stream()
                .filter(grantedAuthority -> {
                    List<PermissionUriConfiguration.UrlFunction> urlFunctions = PermissionUriConfiguration.get(grantedAuthority.getAuthority());
                    return urlFunctions.stream().filter(urlFunction -> {
                        Pattern compile = Pattern.compile(urlFunction.getUri());
                        return request.getMethod().matches(urlFunction.getMethod())
                                && compile.matcher(request.getURI().getPath()).find();
                    }).findAny().isPresent();

                }).findAny();

        if (!optionalGrantedAuthority.isPresent()) {
            throw new BaseException(ErrorCodeEnum.AUTHORIZATION_ERROR, "无访问权限");
        }

        WebFilterExchange webFilterExchange = new WebFilterExchange(exchange, chain);
        SecurityContextImpl securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(authentication);
        return webFilterExchange.getChain().filter(exchange).subscriberContext(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
    }
}
