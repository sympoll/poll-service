package com.MTAPizza.Sympoll.pollmanagementservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfig class to configure CORS settings for the application.
 * This configuration enables cross-origin requests to be handled by the backend,
 * allowing the frontend application to interact with the backend services.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configures the CORS settings for the application.
     *
     * @param registry The CorsRegistry to be used for adding the CORS mappings.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Applies CORS configuration to all endpoints in the application.
        registry.addMapping("/**")
                // Allows requests from the specified origin.
                .allowedOrigins("http://frontend.default.svc.cluster.local:8080") // frontend.default.svc.cluster.local
                // Specifies the allowed HTTP methods for CORS requests.
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // Allows all headers to be included in the requests.
                .allowedHeaders("*")
                // Indicates whether user credentials are supported for CORS requests.
                .allowCredentials(true)
                // Sets the maximum age (in seconds) for the CORS preflight request to be cached by the browser.
                .maxAge(3600);
    }
}