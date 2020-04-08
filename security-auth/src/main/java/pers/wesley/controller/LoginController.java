package pers.wesley.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pers.wesley.common.jwt.JwtTokenGenerate;
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
public class LoginController {

    private JwtTokenGenerate jwtTokenGenerate;

    public LoginController(JwtTokenGenerate jwtTokenGenerate) {
        this.jwtTokenGenerate = jwtTokenGenerate;
    }

    @PostMapping(value = "/login")
    public Mono<LoginResponseVO> login(@RequestBody @Valid LoginRequestVO loginRequestVO) {
        LoginResponseVO loginResponseVO = new LoginResponseVO();
        loginResponseVO.setToken(jwtTokenGenerate.getToken("1", "admin", "管理员", null));
        return Mono.just(loginResponseVO);
    }
}
