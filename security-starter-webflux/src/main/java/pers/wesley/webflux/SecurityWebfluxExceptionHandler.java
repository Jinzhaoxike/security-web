package pers.wesley.webflux;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import pers.wesley.common.exception.*;

/**
 * @Description : 异常处理
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/08 14:12
 */
@ControllerAdvice
@Slf4j
public class SecurityWebfluxExceptionHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseBody
    public ErrorResponse handLerMethodArgumentNotValidException(WebExchangeBindException exception, ServerWebExchange exchange) {
        FieldError fieldError = exception.getFieldErrors().stream().findFirst().get();
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return ErrorResponse.of(ErrorCodeEnum.PARAMETER_ERROR.name(),
                ErrorCodeEnum.PARAMETER_ERROR.getMessage(),
                fieldError.getField(),
                fieldError.getDefaultMessage());
    }

    @ExceptionHandler(BaseException.class)
    @ResponseBody
    public ErrorResponse baseException(BaseException baseException, ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        if (baseException instanceof NotFoundException) {
            response.setStatusCode(HttpStatus.NOT_FOUND);
        } else if (baseException instanceof ExistsException) {
            response.setStatusCode(HttpStatus.CONFLICT);
        } else if (baseException instanceof NetworkConnectionException) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            response.setStatusCode(HttpStatus.BAD_REQUEST);
        }
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return ErrorResponse.of(baseException);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorResponse exception(Exception exception, ServerWebExchange exchange) {
        log.error("系统异常", exception);
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return ErrorResponse.of(exception);
    }
}
