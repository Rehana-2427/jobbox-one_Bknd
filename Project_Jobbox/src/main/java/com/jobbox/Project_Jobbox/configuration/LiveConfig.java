package com.jobbox.Project_Jobbox.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
@Profile("live")
public class LiveConfig {
	 public void addCorsMappings(CorsRegistry registry) {
	        registry.addMapping("/**")
	        .allowedOrigins("https://jobbox.one","https://jobbox.one:8082") // Your production domainn
	                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
	                .allowedHeaders("*");
	    }
}
