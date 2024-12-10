package com.jobbox.Project_Jobbox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobbox.Project_Jobbox.entity.ChatModel;

@RestController
@CrossOrigin(origins = { "http://51.79.18.21:3000", "http://localhost:3000","http://jobbox.one" })
@Controller
@RequestMapping("/api/jobbox")
public class ChatModelController {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

//	@MessageMapping("/message")
//	@SendTo("/chatroom/public")
//	public Message receiveMessage(@Payload  message) {
//		return message;
//	}

	@MessageMapping("/private-message")
	public ChatModel recMessage(@Payload ChatModel message) {
		simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(), "/private", message);
		System.out.println(message.toString());
		return message;
	}
}
