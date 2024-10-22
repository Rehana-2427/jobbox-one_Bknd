package com.jobbox.Project_Jobbox.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobbox.Project_Jobbox.entity.EmailRequest;
import com.jobbox.Project_Jobbox.serviceImpl.ContactEmailingService;
import com.jobbox.Project_Jobbox.serviceImpl.EmailService;

import jakarta.mail.MessagingException;

@RestController
@CrossOrigin(origins = {"http://51.79.18.21:3000", "http://localhost:3000"})
@Controller
@RequestMapping("/api/jobbox")
public class EmailController {

	@Autowired
	private EmailService emailService;

	@Autowired
	private ContactEmailingService contactEmailingService;

	@PostMapping("/savemessage")
	public ResponseEntity<EmailRequest> SaveContactMessage(@RequestBody EmailRequest contactForm){
		return new ResponseEntity<EmailRequest>(contactEmailingService.saveContactMessage(contactForm), HttpStatus.OK);

	}

//	// send message contact
//	@PostMapping("/send-message")
//	public ResponseEntity<String> sendMessage(@RequestBody EmailRequest emailRequest) {
//		try {
//			contactEmailingService.receiveEmails(emailRequest);
//			return ResponseEntity.ok("Email sent successfully");
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email");
//		}
//	}

	

	// getting contact messages
	@GetMapping("/getContactMessages")
	public ResponseEntity<Page<EmailRequest>> getContactMessages(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {
		Page<EmailRequest> recieveEmails = contactEmailingService.getContactMessages(page, size);
		return new ResponseEntity<Page<EmailRequest>>(recieveEmails, HttpStatus.OK);
	}

	// send reply to users of contact messages
	@PutMapping("/sendReplyMessages")
	public EmailRequest sendReplyMessages(@RequestParam String message, @RequestParam String replyTo,
			@RequestParam int contactId) throws Exception {
		return contactEmailingService.sendReplyMessages(message, replyTo, contactId);

	}

	// recive emails from users contact without registering
	@GetMapping("/receivingemailfromusers")
	public ResponseEntity<String> receivingemailfromusers() throws IOException, MessagingException {
		emailService.receiveEmail();
		return ResponseEntity.ok("Email Recieve successfully");

	}

}
