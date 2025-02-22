package com.org.farm.hub.FarmHubApplication.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // Allow all origins, adjust as needed
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP Methods
                .allowedHeaders("Origin", "Content-Type", "Accept", "X-Requested-With", "Authorization") // Allowed Headers
                .allowCredentials(false) // Allow credentials
                .maxAge(3600); // Cache CORS pre-flight request for 1 hour
    }
}
