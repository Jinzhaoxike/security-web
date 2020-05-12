package pers.wesley.common.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @Description : 资源权限校验
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/05/12 14:27
 */
public abstract class WebPathAuthorizationValidator {

    public static Optional<? extends GrantedAuthority> authorization(String requestPath, String requestMethod, Collection<? extends GrantedAuthority> authorities) {
        // 资源权限校验
        return authorities
                .stream()
                .filter(grantedAuthority -> {
                    List<PermissionUriConfiguration.UrlFunction> urlFunctions = PermissionUriConfiguration.get(grantedAuthority.getAuthority());
                    return urlFunctions.stream().filter(urlFunction -> {
                        Pattern compile = Pattern.compile(urlFunction.getUri());
                        return requestMethod.matches(urlFunction.getMethod())
                                && compile.matcher(requestPath).find();
                    }).findAny().isPresent();
                }).findAny();
    }
}
