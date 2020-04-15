package pers.wesley.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pers.wesley.common.jwt.JwtTokenGenerate;
import pers.wesley.common.security.User;
import pers.wesley.common.vo.LoginRequestVO;
import pers.wesley.common.vo.LoginResponseVO;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
        return reactiveAuthenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequestVO.getUsername(), loginRequestVO.getPassword()))
                .flatMap(authentication -> {
                    User user = (User)authentication.getPrincipal();
                    log.info("user ={}", user);
                    List<String> permissions = user.getAuthorities()
                            .stream()
                            .map(grantedAuthority -> grantedAuthority.getAuthority())
                            .collect(Collectors.toList());
                    LoginResponseVO loginResponseVO = new LoginResponseVO();
                    loginResponseVO.setToken(jwtTokenGenerate.getToken("1", loginRequestVO.getUsername(), user.getNickname(), permissions, user.getReserve()));
                    return Mono.just(loginResponseVO);
        });
    }

    @GetMapping(value = "/user/info")
    public Mono<String> getINfo() {
        return Mono.just("admin");
    }
}
