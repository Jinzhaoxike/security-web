package pers.wesley.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pers.wesley.common.jwt.JwtTokenGenerate;
import pers.wesley.common.security.User;
import pers.wesley.common.vo.LoginRequestVO;
import pers.wesley.common.vo.LoginResponseVO;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * @Description :
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/08 13:49
 */
@RestController
@Slf4j
public class LoginController {

    private JwtTokenGenerate jwtTokenGenerate;

    ReactiveAuthenticationManager reactiveAuthenticationManager;

    public LoginController(JwtTokenGenerate jwtTokenGenerate, ReactiveAuthenticationManager reactiveAuthenticationManager) {
        this.jwtTokenGenerate = jwtTokenGenerate;
        this.reactiveAuthenticationManager = reactiveAuthenticationManager;
    }

    @PostMapping(value = "/login")
    public Mono<LoginResponseVO> login(@RequestBody @Valid LoginRequestVO loginRequestVO) {
        return reactiveAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestVO.getUsername(),
                loginRequestVO.getPassword())).flatMap(authentication -> {
            User user = (User)authentication.getPrincipal();
                    log.info("user ={}", user);
            LoginResponseVO loginResponseVO = new LoginResponseVO();
            loginResponseVO.setToken(jwtTokenGenerate.getToken("1", "admin", "管理员", null));
            return Mono.just(loginResponseVO);
        });
    }
}
