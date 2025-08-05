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
        // TEMPORARILY DISABLED: Rate limiter causing 403 errors on all endpoints
        // TODO: Re-enable after fixing the path pattern matching issue
        /*
        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/auth/login",
                        "/auth/signup",
                        "/auth/refresh",
                        "/auth/**",
                        "/api/auth/login",
                        "/api/auth/signup",
                        "/api/auth/refresh",
                        "/api/auth/**",
                        "/actuator/**",
                        "/api/actuator/**",
                        "/test/**",
                        "/api/test/**",
                        "/debug/**",
                        "/api/debug/**",
                        "/doctors/specialties",
                        "/doctors/search",
                        "/doctors",
                        "/doctors/*",
                        "/api/doctors/specialties",
                        "/api/doctors/search",
                        "/api/doctors",
                        "/api/doctors/*",
                        "/smart-features/conditions/*",
                        "/api/smart-features/conditions/*",
                        "/swagger-ui/**",
                        "/api-docs/**"
                );
        */
    }
}
