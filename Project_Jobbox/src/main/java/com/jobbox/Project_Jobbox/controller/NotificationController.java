package com.jobbox.Project_Jobbox.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobbox.Project_Jobbox.entity.Notification;
import com.jobbox.Project_Jobbox.service.NotificationService;

@CrossOrigin(origins = {"http://51.79.18.21:3000", "http://localhost:3000"})
@Controller
@RequestMapping("/api/jobbox")
@RestController
public class NotificationController {

	@Autowired
	private NotificationService notificationService;

	@GetMapping("/getUnreadNotifications")
	public ResponseEntity<Map<String, Object>> getUnreadNotifications(@RequestParam int userId) {
		List<Notification> unreadNotifications = notificationService.getAllUnreadNotifications(userId);
		int unreadCount = unreadNotifications.size();

		Map<String, Object> response = new HashMap<>();
		response.put("notifications", unreadNotifications);
		response.put("count", unreadCount);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/markNotificationsAsRead")
	public ResponseEntity<Void> markNotificationsAsRead(@RequestParam int userId, int notificationId) {
		notificationService.markNotificationsAsRead(userId, notificationId);
		return ResponseEntity.ok().build();
	}
}
