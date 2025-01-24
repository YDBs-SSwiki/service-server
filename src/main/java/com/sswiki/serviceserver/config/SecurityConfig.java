package com.sswiki.serviceserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

// 아래 3개는 CORS 관련 클래스들
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    // 1) CorsConfigurationSource를 Bean으로 등록
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 허용할 Origin(프론트엔드 주소)
        configuration.addAllowedOrigin("http://localhost:52904");
        // 모든 메서드 허용 또는 "GET", "POST" 등 명시 가능
        configuration.addAllowedMethod("*");
        // 모든 헤더 허용
        configuration.addAllowedHeader("*");
        // 자격 증명(쿠키 등) 허용
        configuration.setAllowCredentials(true);

        // UrlBasedCorsConfigurationSource에 등록
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 모든 경로에 위 설정을 적용
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // 2) SecurityFilterChain 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // cors()를 사용하여, 위에서 만든 corsConfigurationSource를 참조
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().permitAll()
                );
        return http.build();
    }
}
