package com.medreserve.config;

import com.medreserve.interceptor.RateLimitInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableAsync
@EnableScheduling
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    
    private final RateLimitInterceptor rateLimitInterceptor;
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Global CORS configuration as backup
        registry.addMapping("/**")
                .allowedOriginPatterns(
                    "https://med-reserve-ai.vercel.app",
                    "https://rishith2903.github.io",
                    "http://localhost:*",
                    "https://localhost:*"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .exposedHeaders("Authorization", "Content-Type")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/auth/login",
                        "/auth/signup",
                        "/auth/**",
                        "/actuator/**",
                        "/test/**",
                        "/debug/**",
                        "/doctors/specialties",
                        "/doctors/search",
                        "/doctors",
                        "/doctors/*",
                        "/smart-features/conditions/*",
                        "/swagger-ui/**",
                        "/api-docs/**"
                );
    }
}
