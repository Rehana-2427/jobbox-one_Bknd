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
		registry.addEndpoint("/api/jobbox/ws").setAllowedOriginPatterns("http://jobbox.one", "http://localhost:3000") // Allow all origins, adjust as needed
				.withSockJS(); // Enable SockJS fallback options
	}
	
//	 @Override
//	    public void configureMessageBroker(MessageBrokerRegistry config) {
//	        // Enable a simple memory-based message broker to carry messages back to the client
//	        config.enableSimpleBroker("/topic"); // Where the messages will be sent to
//	        config.setApplicationDestinationPrefixes("/app"); // Prefix for sending messages
//	    }
//
//	    @Override
//	    public void registerStompEndpoints(StompEndpointRegistry registry) {
//	        // Register the "/ws" endpoint for clients to connect to
//	        registry.addEndpoint("/ws").withSockJS();
//	    }
}
