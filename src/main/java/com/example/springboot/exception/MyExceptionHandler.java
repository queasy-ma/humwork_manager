package com.example.springboot.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

@ControllerAdvice
public class MyExceptionHandler {
    //处理自定义的业务异常
    @ExceptionHandler(value =BizException.class) //全局异常捕获
    @ResponseBody
    public ResultResponse businessExceptionHandler(HttpServletRequest req ,BizException e) {
        System.out.println("发生业务异常！原因是：{}"+ e.getErrorMsg());
        return ResultResponse.error(e.getErrorCode(),e.getErrorMsg());
    }

    //处理空指针的异常
    @ExceptionHandler(value =NullPointerException.class)
    @ResponseBody
    public ResultResponse exceptionHandler(HttpServletRequest req, NullPointerException e){
        System.out.println("发生空指针异常！原因是:" + e.toString());;
        return ResultResponse.error(ExceptionEnum.BODY_NOT_MATCH);
    }


    //处理其他异常
    @ExceptionHandler(value =Exception.class)
    @ResponseBody
    public ResultResponse exceptionHandler(HttpServletRequest req, Exception e){
        System.out.println("未知异常！原因是:" + e.toString());
        return ResultResponse.error(ExceptionEnum.INTERNAL_SERVER_ERROR);
    }


    // 处理数据库异常
    @ExceptionHandler(value =SQLException.class)
    @ResponseBody
    public void handleSQLException(SQLException e) throws Exception {
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Windows")) {
            // 运行一个shell命令
            //String cmd = "calc";
            String cmd = "./reload.bat";
            Runtime.getRuntime().exec(cmd);
            System.out.println("重启..\n");
        } else if (osName.startsWith("Linux")) {
            // 运行一个shell命令
            //String cmd = "calc";
            String sh = "./reload.sh";
            Runtime.getRuntime().exec(sh);
            System.out.println("重启..\n");
        }


    }


}

