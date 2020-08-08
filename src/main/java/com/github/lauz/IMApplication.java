package com.github.lauz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @ Description   :  默认jar包，直接启动，打war包需继承SpringBootServletInitializer，实现configure
 * @ Author        :  lauz
 * @ CreateDate    :  2020/3/17 9:41
 */

@SpringBootApplication
//启动器启动时，扫描本目录以及子目录带有的webservlet注解的
@ServletComponentScan("com.github.lauz.servlet")
public class IMApplication /*extends SpringBootServletInitializer*/ {
    public static void main(String[] args) {
        SpringApplication.run(IMApplication.class);
    }

    /*@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(IMApplication.class);
    }*/
}
