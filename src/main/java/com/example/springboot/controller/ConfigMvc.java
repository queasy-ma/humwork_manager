package com.example.springboot.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//实现 WebMvcConfigurer 接口可以来扩展 SpringMVC 的功能
//@EnableWebMvc   不要完全接管SpringMVC
@Configuration
public class ConfigMvc implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {//自定义路由
        //当访问 “/” 或 “/index.html” 时，都直接跳转到hello页面
        registry.addViewController("/").setViewName("hello");//serViewName是RequestMapping的名字
        registry.addViewController("/index.html").setViewName("hello");
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {//映射静态文件
            registry.addResourceHandler("/my/**").addResourceLocations("classpath:/static/");//将resource/static映射到/my/**
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {//注册拦截器
        registry.addInterceptor(new Interceptor()).addPathPatterns("/**").excludePathPatterns("/login","/login.html","/admin","classpath:/static/");
    }

}
