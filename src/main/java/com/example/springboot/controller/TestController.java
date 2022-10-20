package com.example.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class TestController {
    @ResponseBody
    @GetMapping("/api")
    public String api() {
        // 模拟各种api，访问之前都要检查有没有登录，没有登录就提示用
        return "成功返回数据";
    }
}
