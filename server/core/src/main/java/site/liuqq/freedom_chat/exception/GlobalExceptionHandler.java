package site.liuqq.freedom_chat.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.liuqq.freedom_chat.pojo.Result;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result exception(Exception e) {

        e.printStackTrace();

        return Result.error("程序出现异常:" + e.getMessage());
    }
}
