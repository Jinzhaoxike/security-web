package pers.wesley.webflux.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import pers.wesley.common.SecurityProperties;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @Description : jwt token 过滤器
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/08 18:22
 */
//@Configuration
//@Slf4j
public class JwtTokenWebFilter implements WebFilter {

    private static final String JWT_TOKEN_HEADER_PARAM = "X-Authorization";

    private static final String HEADER_PREFIX = "Bearer ";

    private final ServerWebExchangeMatcher matcher;

    public JwtTokenWebFilter(SecurityProperties securityProperties) {
        matcher = ServerWebExchangeMatchers.pathMatchers(securityProperties.getPermitUrl().toArray(new String[securityProperties.getPermitUrl().size()]));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

//        return DataBufferUtils.join(exchange.getRequest().getBody())
//                .flatMap(dataBuffer -> {
//                    CharBuffer charBuffer = StandardCharsets.UTF_8.decode(dataBuffer.asByteBuffer());
//                    log.info("charBuffer = {}", charBuffer.toString());
//                    ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(
//                            exchange.getRequest()) {
//                        @Override
//                        public Flux<DataBuffer> getBody() {
//                            return Mono.<DataBuffer>fromSupplier(() -> {
//                                NettyDataBuffer pdb = (NettyDataBuffer) dataBuffer;
//                                return pdb.factory()
//                                        .wrap(pdb.getNativeBuffer().retainedSlice());
//                            }).flux();
//                        }
//                    };
//                    return chain.filter(exchange.mutate().request(decorator).build());
//                });
        return matcher.matches(exchange)
                .filter(matchResult -> !matchResult.isMatch())
                .flatMap(matchResult -> {
                    String authorization = exchange.getRequest().getHeaders().getFirst(JWT_TOKEN_HEADER_PARAM);
                    if (StringUtils.isEmpty(authorization)) {
                        return errorResponse(exchange.getResponse(), "Authorization 不能为空");
                    }
                    return chain.filter(exchange);
                });
    }

    private Mono<Void> errorResponse(ServerHttpResponse serverHttpResponse, String message) {
        serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
        serverHttpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return serverHttpResponse.writeWith(Mono.just(serverHttpResponse.bufferFactory().wrap(message.getBytes())));
    }
}
