package com.sswiki.serviceserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 모든 경로에 대해
        registry.addMapping("/**")
                // 실제 허용할 Origin(프론트엔드 주소)
                // 여러 개 추가 가능: allowedOrigins("http://localhost:3000", "http://127.0.0.1:5500")
                .allowedOrigins("http://localhost:52904")
                // 허용할 메서드들
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // 허용할 헤더들
                .allowedHeaders("*")
                // 자격 증명(쿠키 등) 허용 여부
                .allowCredentials(true);
    }
}
