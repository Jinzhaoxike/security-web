package pers.wesley.webflux;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.DelegatingReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;
import pers.wesley.common.CorsProperties;
import pers.wesley.common.SecurityProperties;
import pers.wesley.common.jwt.JwtTokenGenerate;
import pers.wesley.common.security.User;
import pers.wesley.webflux.filter.AuthorizationWebFilter;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description : 安全校验配置
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/08 10:46
 */
@Configuration
@EnableWebFluxSecurity
@EnableConfigurationProperties(value = {SecurityProperties.class, CorsProperties.class})
public class SecurityWebfluxConfiguration {

    private SecurityProperties securityProperties;
    private CorsProperties corsProperties;

    public SecurityWebfluxConfiguration(SecurityProperties securityProperties, CorsProperties corsProperties) {
        this.securityProperties = securityProperties;
        this.corsProperties = corsProperties;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity, JwtTokenGenerate jwtTokenGenerate, ReactiveUserDetailsService reactiveUserDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        httpSecurity
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .authorizeExchange()
                .pathMatchers(securityProperties.getPermitUrl().toArray(new String[securityProperties.getPermitUrl().size()])).permitAll()
                .and()
                .addFilterAt(new AuthorizationWebFilter(securityProperties, jwtTokenGenerate), SecurityWebFiltersOrder.AUTHORIZATION)
                .authorizeExchange()
                .anyExchange()
                .authenticated();
        return  httpSecurity.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        return exchange -> {
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            corsConfiguration.setAllowedOrigins(corsProperties.getAllowOrigin());
            corsConfiguration.setAllowedHeaders(corsProperties.getAllowHeaders());
            corsConfiguration.setAllowedMethods(corsProperties.getAllowMethods());
            corsConfiguration.setExposedHeaders(corsProperties.getExposedHeaders());
            corsConfiguration.setMaxAge(corsProperties.getMaxAges());
            return corsConfiguration;
        };
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
                    .nickname("管理员")
                    .authorities("/user/read")
                    .password("123456")
                    .passwordEncoder(bCryptPasswordEncoder::encode)
                    .reserve(reserve)
                    .build();
            return Mono.just(User.withUserDetails(userDetails).build());
        };
    }

    @Bean
    public ServerSecurityContextRepository serverSecurityContextRepository(JwtTokenGenerate jwtTokenGenerate) {

        return new ServerSecurityContextRepository() {

            private final Logger logger = LoggerFactory.getLogger(ServerSecurityContextRepository.class);

            private static final String JWT_TOKEN_HEADER_PARAM = "X-Authorization";

            private static final String HEADER_PREFIX = "Bearer ";

            @Override
            public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
                logger.info("SecurityContext = {}", context);
                return Mono.empty();
            }

            @Override
            public Mono<SecurityContext> load(ServerWebExchange exchange) {
                ServerHttpRequest serverHttpRequest = exchange.getRequest();
                String token = serverHttpRequest.getHeaders().getFirst(JWT_TOKEN_HEADER_PARAM);
                if (StringUtils.isEmpty(token) || !token.startsWith(HEADER_PREFIX)) {
                    return Mono.empty();
                }
                jwtTokenGenerate.parseToken(token);
                SecurityContext securityContext = new SecurityContextImpl();
                return Mono.just(securityContext);
            }
        };
    }

    @Bean
    public ServerAuthenticationConverter serverAuthenticationConverter(JwtTokenGenerate jwtTokenGenerate) {

        final String JWT_TOKEN_HEADER_PARAM = "X-Authorization";

        final String HEADER_PREFIX = "Bearer ";

        return exchange -> {
            ServerHttpRequest serverHttpRequest = exchange.getRequest();
            String token = serverHttpRequest.getHeaders().getFirst(JWT_TOKEN_HEADER_PARAM);
            if (StringUtils.isEmpty(token) || !token.startsWith(HEADER_PREFIX)) {
                return Mono.empty();
            }
            return Mono.just(jwtTokenGenerate.parseToken(token.substring(HEADER_PREFIX.length())));
        };
    }

    @Bean
    public AuthenticationWebFilter authenticationWebFilter(JwtTokenGenerate jwtTokenGenerate) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter((ReactiveAuthenticationManager) authentication -> Mono.just(authentication));
        NegatedServerWebExchangeMatcher negatedServerWebExchangeMatcher = new NegatedServerWebExchangeMatcher(ServerWebExchangeMatchers.pathMatchers(securityProperties.getPermitUrl().toArray(new String[securityProperties.getPermitUrl().size()])));
        authenticationWebFilter.setRequiresAuthenticationMatcher(negatedServerWebExchangeMatcher);
        authenticationWebFilter.setSecurityContextRepository(serverSecurityContextRepository(jwtTokenGenerate));
        authenticationWebFilter.setServerAuthenticationConverter(serverAuthenticationConverter(jwtTokenGenerate));
        return authenticationWebFilter;
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveUserDetailsService reactiveUserDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        UserDetailsRepositoryReactiveAuthenticationManager userDetailsRepositoryReactiveAuthenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(reactiveUserDetailsService);
        userDetailsRepositoryReactiveAuthenticationManager.setPasswordEncoder(bCryptPasswordEncoder);
        return new DelegatingReactiveAuthenticationManager(userDetailsRepositoryReactiveAuthenticationManager);
    }
}
