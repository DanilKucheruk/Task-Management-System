package com.tsm.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for setting up Cross-Origin Resource Sharing in the application.
 * This configuration allows the backend to handle requests from different origins, 
 * enabling communication between the frontend and backend in cross-domain setups.
 * 
 * <p><b>Usage:</b></p>
 * <ul>
 *   <li>Enables CORS for all paths in the application ("/**").</li>
 *   <li>Allows all origins, methods, and headers, while permitting credentials.</li>
 *   <li>Recommended to restrict configurations for production environments.</li>
 * </ul>
 * 
 * <p><b>Security Note:</b></p>
 * Be cautious with permissive settings (e.g., {@code allowedOriginPatterns("*")}) in production.
 * Restrict origins and headers to trusted sources to avoid security vulnerabilities.
 */

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")  
                .allowedOriginPatterns("*") 
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("*") 
                .allowCredentials(true); 
    }
}