package pers.wesley.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import pers.wesley.common.CorsProperties;
import pers.wesley.common.SecurityProperties;
import pers.wesley.common.exception.BaseException;
import pers.wesley.common.exception.ErrorCodeEnum;
import pers.wesley.common.exception.ErrorResponse;
import pers.wesley.common.jwt.JwtAuthenticationToken;
import pers.wesley.common.jwt.JwtTokenGenerate;
import pers.wesley.common.security.User;
import pers.wesley.common.util.LoggerUtils;
import pers.wesley.web.filter.NegatedRequestMatcher;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description :
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/05/11 10:16
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(value = {SecurityProperties.class, CorsProperties.class})
@Slf4j
public class SecurityWebConfiguration extends WebSecurityConfigurerAdapter {

    private SecurityProperties securityProperties;
    private CorsProperties corsProperties;
    private final JwtTokenGenerate jwtTokenGenerate;

    public SecurityWebConfiguration(SecurityProperties securityProperties, CorsProperties corsProperties, JwtTokenGenerate jwtTokenGenerate) {
        this.securityProperties = securityProperties;
        this.corsProperties = corsProperties;
        this.jwtTokenGenerate = jwtTokenGenerate;
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        return new AuthenticationProvider() {

            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                return authentication;
            }

            @Override
            public boolean supports(Class<?> authentication) {
                return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
            }
        };
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider())
                .userDetailsService(customUserDetailsService())
                .passwordEncoder(bCryptPasswordEncoder())
        ;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .authorizeRequests()
                .antMatchers(securityProperties.getPermitUrl().toArray(new String[securityProperties.getPermitUrl().size()])).permitAll()
                .anyRequest().access("@securityWebPathAccessAuthorization.authorization(authentication, request)")
                .and()
                .addFilterAfter(customAuthenticationFilter(this.authenticationManager()), CorsFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    private static final String JWT_TOKEN_HEADER_PARAM = "X-Authorization";

    private static final String HEADER_PREFIX = "Bearer ";

    @Bean
    AuthenticationConverter authenticationConverter() {
        return request -> {
            String token = request.getHeader(JWT_TOKEN_HEADER_PARAM);
            LoggerUtils.debug(log, "X-Authorization = [{}]", () -> token);
            if (StringUtils.isEmpty(token)) {
                throw new AuthenticationCredentialsNotFoundException("[X-Authorization]不存在");
            }
            if (!token.startsWith(HEADER_PREFIX)) {
                throw new AuthenticationCredentialsNotFoundException("[X-Authorization]格式错误");
            }
            try {
                Authentication authentication = jwtTokenGenerate.parseToken(token.substring(HEADER_PREFIX.length()));
                return authentication;
            } catch (BaseException e) {
                throw new AuthenticationCredentialsNotFoundException(e.getMessage());
            }
        };
    }


    @Bean
    AuthenticationFilter customAuthenticationFilter(AuthenticationManager authenticationManager) {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager, authenticationConverter());
        List<RequestMatcher> requestMatchers = securityProperties.getPermitUrl().stream().map(path -> new AntPathRequestMatcher(path)).collect(Collectors.toList());
        NegatedRequestMatcher negatedRequestMatcher = new NegatedRequestMatcher(requestMatchers);
        authenticationFilter.setRequestMatcher(negatedRequestMatcher);
        // 必须提供SuccessHandler，否则会出现 Cannot call sendError() after the response has been committed
        authenticationFilter.setSuccessHandler((request, response, authentication) -> LoggerUtils.debug(log, "请求报文 url=[{}],method=[{}],body=[{}]", () -> request.getRequestURI(), () -> request.getMethod(), () -> {
            Enumeration<String> parameterNames = request.getParameterNames();
            StringBuilder stringBuilder = new StringBuilder();
            while (parameterNames.hasMoreElements()) {
                String name = parameterNames.nextElement();
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(",");
                }
                stringBuilder.append(name).append("=").append(request.getParameter(name));
            }
            return stringBuilder;
        }));
        authenticationFilter.setFailureHandler((request, response, exception) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            BaseException be;
            if (exception instanceof AuthenticationException) {
                be = new BaseException(ErrorCodeEnum.AUTHORIZATION_ERROR, exception.getMessage());
            } else {
                log.error("鉴权异常", exception);
                be = new BaseException(ErrorCodeEnum.AUTHENTICATION_ERROR, "未知错误", exception);
            }
            response.getWriter().write(ErrorResponse.of(be).toJson());
        });
        return authenticationFilter;
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
    UserDetailsService customUserDetailsService() {
        return username -> {
            Map<String, Object> reserve = new HashMap<>(4);
            reserve.put("userType", "pltadmin");
            UserDetails userDetails = User
                    .withUsername("admin")
                    .nickname("管理员")
                    .authorities("/user/read")
                    .password("123456")
                    .passwordEncoder(bCryptPasswordEncoder()::encode)
                    .reserve(reserve)
                    .build();
            return userDetails;
        };
    }
}
