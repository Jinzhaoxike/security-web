package pers.wesley.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import pers.wesley.common.security.WebPathAuthorizationValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @Description : 资源权限校验
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/05/12 14:06
 */
@Component
@Slf4j
public class SecurityWebPathAccessAuthorization {

    public boolean authorization(Authentication authentication, HttpServletRequest request) {
        // 资源权限校验
        Optional<? extends GrantedAuthority> optionalGrantedAuthority = WebPathAuthorizationValidator.authorization(request.getRequestURI(), request.getMethod(), authentication.getAuthorities());
        if (!optionalGrantedAuthority.isPresent()) {
            throw new AuthenticationServiceException("无访问权限");
        }
        return true;
    }
}
