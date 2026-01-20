package com.library.project.config;

import com.library.project.interceptor.RbacInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private RbacInterceptor rbacInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Kích hoạt Interceptor cho toàn bộ hệ thống
        registry.addInterceptor(rbacInterceptor)
                .addPathPatterns("/**"); // Dấu /** nghĩa là tất cả mọi ngóc ngách
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}