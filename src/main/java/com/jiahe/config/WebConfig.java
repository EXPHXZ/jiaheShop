package com.jiahe.config;

import com.jiahe.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("interceptors..............");
        InterceptorRegistration registration = registry.addInterceptor(loginInterceptor());
        registration.addPathPatterns("/**");
        //配置可放行路径
        registration.excludePathPatterns(
                "/login.html",
                "/userManagement/login",
                "/css/*",
                "/js/*",
                "/element-ui/**",
                "/images/*",
                "/favicon.ico"
        );
    }

    @Bean LoginInterceptor loginInterceptor(){
        return new LoginInterceptor();
    }

}
