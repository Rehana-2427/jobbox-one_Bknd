package com.jobbox.Project_Jobbox.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobbox.Project_Jobbox.entity.Chat;
import com.jobbox.Project_Jobbox.service.ChatService;



@CrossOrigin(origins = {"http://51.79.18.21:3000", "http://localhost:3000"})
@Controller
@RequestMapping("/api/jobbox")
@RestController
public class ChatController {

	@Autowired
	public ChatService chatService;

	@Autowired
	private final SimpMessagingTemplate messagingTemplate;
	public ChatController(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}


	//	 // Handle incoming messages
	//    @MessageMapping("/chat")
	//    @SendTo("/topic/chat")
	//    public Chat sendMessage(Chat message) {
	//        // Save the message to the database
	//        Chat savedMessage = chatService.saveMessage(message);
	//        
	//        // Optionally, you can add logic here to send the saved message back to clients
	//
	//        return savedMessage;
	//    }
	// GET: Retrieve all messages for a specific applicationId
	@GetMapping("/messages")
	public List<Chat> getMessages(@RequestParam int applicationId) {
		return chatService.getMessagesByApplicationId(applicationId);
	}

//	@MessageMapping("/chat")
//	public void sendMessage(Chat message) {
//		messagingTemplate.convertAndSend("/topic/app", message);
//	}
	// POST: Save a new chat message to the database
	//@PostMapping("/messagesPost")
	@MessageMapping("/chat") // Maps the message coming to /app/chat
	@SendTo("/topic/app")   // Broadcasts the message to subscribers
	public Chat saveMessage(@RequestBody Chat message) {
		return chatService.saveMessage(message);
	}

	@PutMapping("/saveHRChatByApplicationId")
	public ResponseEntity<Chat> saveHRChat(@RequestParam int applicationId, @RequestParam String hrchat) {
		Chat chat = chatService.saveHRChat(applicationId, hrchat);

		return new ResponseEntity<Chat>(chat, HttpStatus.OK);
	}

	@PutMapping("/saveCandidateChatByApplicationId")
	public ResponseEntity<Chat> saveCandidateChat(@RequestParam int applicationId, @RequestParam String candidatechat) {
		Chat chat = chatService.saveCandidateChat(applicationId, candidatechat);

		return new ResponseEntity<Chat>(chat, HttpStatus.OK);
	}

	@GetMapping("/fetchChatByApplicationId")
	public ResponseEntity<List<Chat>> fetchChatByApplicationId(@RequestParam int applicationId) {
		List<Chat> fetchChat = chatService.fetchChat(applicationId);
		return new ResponseEntity<List<Chat>>(fetchChat, HttpStatus.OK);

	}

	@PutMapping("/markCandidateMessagesAsRead")
	public ResponseEntity<?> markCandidateMessagesAsRead(@RequestParam int applicationId) {
		chatService.markCandidateMessagesAsRead(applicationId);
		return ResponseEntity.ok("Messages marked as read successfully.");

	}

	@PutMapping("/markHRMessagesAsRead")
	public ResponseEntity<?> markHRMessagesAsRead(@RequestParam int applicationId) {
		chatService.markHRMessagesAsRead(applicationId);
		return ResponseEntity.ok("Messages marked as read successfully.");

	}

	@GetMapping("/fetchCountUnreadMessageForCandidateByApplicationId")
	public ResponseEntity<Integer> fetchCountUnreadMessageForCandidateByApplicationId(@RequestParam int applicationId) {
		Integer fetchCountUnreadMessageForCandidateByApplicationId = chatService
				.fetchCountUnreadMessageForCandidateByApplicationId(applicationId);
		return new ResponseEntity<Integer>(fetchCountUnreadMessageForCandidateByApplicationId, HttpStatus.OK);

	}

	@GetMapping("/fetchCountUnreadMessageForHRByApplicationId")
	public ResponseEntity<Integer> fetchCountUnreadMessageForHRByApplicationId(@RequestParam int applicationId) {
		Integer fetchCountUnreadMessageForCandidateByApplicationId = chatService
				.fetchCountUnreadMessageForHRByApplicationId(applicationId);
		return new ResponseEntity<Integer>(fetchCountUnreadMessageForCandidateByApplicationId, HttpStatus.OK);

	}
}
















