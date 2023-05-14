package com.jiahe.exception;

import com.jiahe.utils.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//设置异常处理，处理所有异常
@RestControllerAdvice
public class ProjectException {
    //拦截所有异常，返回一个Result
    @ExceptionHandler(Exception.class)
    public Result doException(Exception ex) {
        ex.printStackTrace();
        return new Result(null,0,"服务器故障，请稍后再试");
    }
}
