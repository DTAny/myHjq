package com.example.myhjq.config;

import com.example.myhjq.interceptor.ManageInterceptor;
import com.example.myhjq.interceptor.SuperInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration manageRegistration = registry.addInterceptor(new ManageInterceptor());
        manageRegistration.addPathPatterns(
                "/**/bookManage/**",
                "/**/readerManage/**",
                "/**/logManage/**"
        );
        InterceptorRegistration superRegistration = registry.addInterceptor(new SuperInterceptor());
        superRegistration.addPathPatterns("/**/superManage/**");
    }
}


