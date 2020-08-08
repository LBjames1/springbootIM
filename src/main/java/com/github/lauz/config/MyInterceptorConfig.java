package com.github.lauz.config;

import com.github.lauz.interceptor.LoginIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @ Description   :  拦截器interceptor配置类
 * Spring Boot 2.0 废弃了 WebMvcConfigurerAdapter，
 * 但是继承 WebMvcConfigurationSupport 又会导致默认的静态资源被拦截
 * 因此实现 WebMvcConfigurer 接口
 * @ Author        :  lauz
 * @ CreateDate    :  2020/3/13 16:02
 */
@Configuration
public class MyInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private LoginIntercepter loginIntercepter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 实现 WebMvcConfigurer 不会导致静态资源被拦截
        registry.addInterceptor(loginIntercepter).addPathPatterns("/**").excludePathPatterns("/login").excludePathPatterns("/authImg")
                .excludePathPatterns("/loginvalidate");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
}
