package com.sublethub.exception;

import com.sublethub.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理，统一返回 ApiResponse 格式
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  @ResponseStatus(HttpStatus.OK)
  public ApiResponse<?> handleBusiness(BusinessException e) {
    return ApiResponse.fail(e.getCode(), e.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResponse<?> handleValid(MethodArgumentNotValidException e) {
    String msg = e.getBindingResult().getFieldErrors().stream()
        .map(err -> err.getField() + ": " + err.getDefaultMessage())
        .reduce((a, b) -> a + "; " + b)
        .orElse("参数校验失败");
    return ApiResponse.fail(400, msg);
  }

  @ExceptionHandler(BindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResponse<?> handleBind(BindException e) {
    String msg = e.getBindingResult().getFieldErrors().stream()
        .map(err -> err.getField() + ": " + err.getDefaultMessage())
        .reduce((a, b) -> a + "; " + b)
        .orElse("参数错误");
    return ApiResponse.fail(400, msg);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ApiResponse<?> handleOther(Exception e) {
    log.error("server error", e);
    return ApiResponse.fail(500, "服务器繁忙，请稍后重试");
  }
}
