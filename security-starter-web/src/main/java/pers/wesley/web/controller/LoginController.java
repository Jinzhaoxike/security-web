package pers.wesley.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pers.wesley.common.jwt.JwtTokenGenerate;
import pers.wesley.common.security.User;
import pers.wesley.common.vo.LoginRequestVO;
import pers.wesley.common.vo.LoginResponseVO;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description :
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/05/11 10:53
 */
@RestController
@Slf4j
public class LoginController {

    private JwtTokenGenerate jwtTokenGenerate;

    private AuthenticationManager authenticationManager;

    public LoginController(JwtTokenGenerate jwtTokenGenerate, AuthenticationManager authenticationManager) {
        this.jwtTokenGenerate = jwtTokenGenerate;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(value = "/login")
    public LoginResponseVO login(@RequestBody @Valid LoginRequestVO loginRequestVO) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestVO.getUsername(), loginRequestVO.getPassword()));
        User user = (User)authenticate.getPrincipal();
        log.info("user ={}", user);
        List<String> permissions = user.getAuthorities()
                .stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.toList());
        LoginResponseVO loginResponseVO = new LoginResponseVO();
        loginResponseVO.setToken(jwtTokenGenerate.getToken("1", loginRequestVO.getUsername(), user.getNickname(), permissions, user.getReserve()));
        return loginResponseVO;
    }

    @GetMapping(value = "/user/info")
    public String getINfo() {
        log.info("success admin");
        return "admin";
    }
}
