package com.jobbox.Project_Jobbox.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Profile("dev")
public class CorsConfig {

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/jobbox").allowedOrigins("http://localhost:3000","http://51.79.18.21:3000")
						.allowedMethods("GET", "POST", "PUT", "DELETE").allowCredentials(true);
				  // Allow CORS for WebSocket endpoint (e.g., /ws or whatever path you're using)
		        registry.addMapping("/api/jobbox/ws/**")
		                .allowedOrigins("http://localhost:3000", "http://51.79.18.21:3000")
		                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
		                .allowCredentials(true);

			}
		};
					
	}
}
