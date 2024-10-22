package com.jobbox.Project_Jobbox.serviceImpl;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.jobbox.Project_Jobbox.entity.EmailRequest;
import com.jobbox.Project_Jobbox.repository.EmailRepo;

import jakarta.mail.Folder;
import jakarta.mail.Session;
import jakarta.mail.Store;

@Service
public class ContactEmailingService {


	@Autowired
	private EmailRepo emailRepo;

	@Autowired
	private EmailService emailService;

	private final String host = "imap.globat.com";
	private final String userName = "info@paisafund.com";
	private final String password = "Jobbox@100";

	public void receiveEmails(EmailRequest emailRequest) {
		Properties properties = new Properties();
		properties.put("mail.store.protocol", "imaps");
		try {
			Session emailSession = Session.getDefaultInstance(properties);
			Store store = emailSession.getStore("imaps");
			store.connect(host, userName, password);

			Folder emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);

//			String userEmail = emailRequest.getEmail();
//			String name = emailRequest.getName();
//			String subject = emailRequest.getSubject();
//			String message = emailRequest.getMessage();

			emailFolder.close(false);
			store.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		emailRepo.save(emailRequest);
	}

	public Page<EmailRequest> getContactMessages(int page, int size) {
		PageRequest pageRequest = PageRequest.of(page, size);
		return emailRepo.findAll(pageRequest);
	}

	public EmailRequest sendReplyMessages(String message, String replyTo, int contactId) {
		EmailRequest emailRequest = emailRepo.getById(contactId);

		// Update the reply message in the database
		emailRequest.setReplyMsg("Replied");
		emailRepo.save(emailRequest);

		// Send the reply email
		String subject = "Reply to your query" + "\n\n" + emailRequest.getMessage();
		emailService.sendEmail(emailRequest.getEmail(), subject, message);

		return emailRequest;
	}

	public EmailRequest saveContactMessage(EmailRequest contactForm) {
		// TODO Auto-generated method stub
		return emailRepo.save(contactForm);
	}
}
