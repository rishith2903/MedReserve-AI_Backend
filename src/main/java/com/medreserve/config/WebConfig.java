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
        // CORS is configured via SecurityConfig.cors; no additional mappings here to avoid duplication.
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // TEMPORARILY DISABLED: Rate limiter still causing issues
        // Will re-enable after confirming all endpoints work
        /*
        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/auth/login",
                        "/auth/signup",
                        "/auth/refresh",
                        "/auth/signin",
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
        */
    }
}
