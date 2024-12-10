package com.jobbox.Project_Jobbox.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
@Profile("live")
public class LiveConfig {
	 public void addCorsMappings(CorsRegistry registry) {
	        registry.addMapping("/**")
	                .allowedOrigins("http://51.79.18.21:3000","http://51.79.18.21:8082","http://jobbox.one") // Your production domain
	                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
	                .allowedHeaders("*");
	    }
}
