package pers.wesley.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.WebExchangeBindException;
import pers.wesley.common.exception.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description : 统一异常处理
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/05/11 11:31
 */
@ControllerAdvice
@Slf4j
public class SecurityWebExceptionHandler {
    @ExceptionHandler(BaseException.class)
    @ResponseBody
    public ErrorResponse baseException(BaseException baseException, HttpServletResponse response) {
        if (baseException instanceof NotFoundException) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        } else if (baseException instanceof ExistsException) {
            response.setStatus(HttpStatus.CONFLICT.value());
        } else if (baseException instanceof NetworkConnectionException) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
        return ErrorResponse.of(baseException);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseBody
    public ErrorResponse handLerMethodArgumentNotValidException(WebExchangeBindException exception, HttpServletResponse response) {
        FieldError fieldError = exception.getFieldErrors().stream().findFirst().get();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return ErrorResponse.of(ErrorCodeEnum.PARAMETER_ERROR.name(),
                ErrorCodeEnum.PARAMETER_ERROR.getMessage(),
                fieldError.getField(),
                fieldError.getDefaultMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorResponse exception(Exception exception, HttpServletResponse response) {
        log.error("系统异常", exception);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ErrorResponse.of(exception);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public ErrorResponse exception(AuthenticationException e, HttpServletResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        BaseException be;
        if (e instanceof BadCredentialsException) {
            be = new BaseException(ErrorCodeEnum.AUTHENTICATION_ERROR, "用户名或密码错误", e);
        } else if (e instanceof UsernameNotFoundException) {
            be = new BaseException(ErrorCodeEnum.AUTHENTICATION_ERROR, "用户名不存在", e);
        } else if (e instanceof DisabledException) {
            be = new BaseException(ErrorCodeEnum.AUTHENTICATION_ERROR, "账户已禁用", e);
        } else if (e instanceof LockedException) {
            be = new BaseException(ErrorCodeEnum.AUTHENTICATION_ERROR, "账户已锁定", e);
        } else if (e instanceof AccountExpiredException) {
            be = new BaseException(ErrorCodeEnum.AUTHENTICATION_ERROR, "账户已过期", e);
        } else if (e instanceof CredentialsExpiredException) {
            be = new BaseException(ErrorCodeEnum.AUTHENTICATION_ERROR, "账户凭证已过期", e);
        } else {
            log.error("鉴权异常", e);
            be = new BaseException(ErrorCodeEnum.AUTHENTICATION_ERROR, "未知错误", e);
        }
        return ErrorResponse.of(be);
    }
}
