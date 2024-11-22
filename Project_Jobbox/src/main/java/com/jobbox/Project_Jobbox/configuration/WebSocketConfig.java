package com.jobbox.Project_Jobbox.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic","/user"); // Enables a simple in-memory broker
		
		config.setApplicationDestinationPrefixes("/app"); // Prefix for messages to be routed to @MessageMapping
		
//		config.enableSimpleBroker("/chatroom","/user");
//		config.setUserDestinationPrefix("/user");
		
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// Register WebSocket endpoint for clients to connect to
		// Allow all origins, adjust as needed
		registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS(); // Enable SockJS fallback options
	}
}
