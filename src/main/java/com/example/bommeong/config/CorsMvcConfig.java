package com.example.bommeong.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**") // 모든 요청에 대해
                .exposedHeaders("Set-Cookie") // Set-Cookie 헤더를 노출
                .allowedOrigins("*") // 모든 오리진 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH") // 모든 HTTP 메서드 허용
    ;
    }
}
