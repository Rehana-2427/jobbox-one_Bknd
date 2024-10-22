package com.jobbox.Project_Jobbox.controller;

import java.util.List;

import javax.management.Notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobbox.Project_Jobbox.entity.Application;
import com.jobbox.Project_Jobbox.service.ApplicationService;

@CrossOrigin(origins = {"http://51.79.18.21:3000", "http://localhost:3000"})
@Controller
@RequestMapping("/api/jobbox")
@RestController
public class WebSocketController {

	@Autowired
	private final SimpMessagingTemplate messagingTemplate;

	@Autowired
	public ApplicationService applicationService;

	public WebSocketController(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	@MessageMapping("/notifications")
	@SendTo("/topic/notifications")
	public Notification sendNotification(@Payload Notification notification) {
		return notification;
	}

	@GetMapping("/notifications")
	public ResponseEntity<List<Application>> getUserNotifications(@RequestParam int candidateId) {
		List<Application> notifications = applicationService.getShortlistedJobs(candidateId);
		return ResponseEntity.ok(notifications);
	}
}
