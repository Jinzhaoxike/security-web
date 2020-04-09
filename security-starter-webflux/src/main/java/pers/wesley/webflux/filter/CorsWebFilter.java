package pers.wesley.webflux.filter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import pers.wesley.common.CorsProperties;
import reactor.core.publisher.Mono;

/**
 * @Description : 跨域过滤器
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/08 10:56
 */
@ConditionalOnProperty(prefix = "pers.security.cors", name = "enabled", havingValue = "true")
@Configuration
@EnableConfigurationProperties(CorsProperties.class)
public class CorsWebFilter implements WebFilter {

    private CorsProperties corsProperties;

    public CorsWebFilter(CorsProperties corsProperties) {
        this.corsProperties = corsProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (CorsUtils.isCorsRequest(request)) {
            ServerHttpResponse response = exchange.getResponse();
            HttpHeaders headers = response.getHeaders();
            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, corsProperties.getAllowOrigin());
            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, corsProperties.getAllowCredentials().toString());
            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, String.join(",", corsProperties.getAllowHeaders()));
            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, String.join(",", corsProperties.getAllowMethods()));
            headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, corsProperties.getMaxAges().toString());
            headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, String.join(",", corsProperties.getExposedHeaders()));
        }
        return chain.filter(exchange);
    }
}
