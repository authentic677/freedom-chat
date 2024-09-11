package xyz677123.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.liuqq.freedom_chat.pojo.Result;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result exception(Exception e) {

        System.out.println(e.getMessage());

        return Result.error("程序出现异常:" + e.getMessage());
    }
}
