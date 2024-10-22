package com.jobbox.Project_Jobbox.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.jobbox.Project_Jobbox.serviceImpl.UserServicImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private UserServicImpl userServicImpl;
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		int strength = 10;
		return new BCryptPasswordEncoder(strength);
	}
	
//	   @Bean
//	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//	        http.csrf().disable()
//	            .authorizeRequests()
//	            .requestMatchers("/api/jobbox/auth/token").permitAll()
//	            .anyRequest().authenticated();
//	        return http.build();
//	    }
	   @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http.csrf().disable()
	            .authorizeRequests()
	            .requestMatchers("/api/jobbox/login").permitAll() // Require JWT token for login
	            .requestMatchers("/api/jobbox/**").permitAll() // Allow public access to all other endpoint
	            .anyRequest().authenticated();
	        return http.build();
	    }
//	   @Bean
//		public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		    http.csrf().disable()
//		        .authorizeRequests()
//		        .requestMatchers("/auth/token").permitAll()  // Updated endpoint
//		        .anyRequest().authenticated();
//		    return http.build();
//		}
	    @Bean
	    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
	        AuthenticationManagerBuilder authenticationManagerBuilder = 
	                http.getSharedObject(AuthenticationManagerBuilder.class);
	        authenticationManagerBuilder.userDetailsService(userServicImpl).passwordEncoder(passwordEncoder());
	        return authenticationManagerBuilder.build();
	    }


}
