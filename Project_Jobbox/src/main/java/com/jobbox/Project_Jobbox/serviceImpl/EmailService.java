package com.jobbox.Project_Jobbox.serviceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.jobbox.Project_Jobbox.entity.Application;
import com.jobbox.Project_Jobbox.entity.Company;
import com.jobbox.Project_Jobbox.entity.Resume;
import com.jobbox.Project_Jobbox.entity.User;
import com.jobbox.Project_Jobbox.repository.ApplicationRepository;
import com.jobbox.Project_Jobbox.repository.CompanyRepository;
import com.jobbox.Project_Jobbox.repository.ResumeRepository;
import com.jobbox.Project_Jobbox.repository.UserRepository;

import jakarta.mail.Address;
import jakarta.mail.BodyPart;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.NoSuchProviderException;
import jakarta.mail.Part;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	Logger logger=LoggerFactory.getLogger(EmailService.class);

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private ResumeRepository resumeRepository;

	@Autowired
	private ApplicationRepository applicationRepository;

	private final String imapHost = "imap.globat.com";
	private final String username = "info@paisafund.com";
	private final String jbpassword = "Jobbox@100";

	public void sendEmail(String to, String subject, String body) {
		logger.info("class::  EmailService -> method sendEmail :: to "+to+ " subject "+subject+" body "+body +"from "+username);
		try {
			// Create a SimpleMailMessage
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(username);
			message.setTo(to);
			message.setSubject(subject);
			message.setText(body);

			// Send the email
			mailSender.send(message);

			// Save a copy to "Sent Items" using MimeMessage
			saveToSentItems(username, to, subject, body);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveToSentItems(String from, String to, String subject, String body) {
		Properties properties = new Properties();
		properties.put("mail.store.protocol", "imaps");
		try {
			Session emailSession = Session.getDefaultInstance(properties);
			Store store = emailSession.getStore("imaps");
			store.connect(imapHost, username, jbpassword);

			Folder[] folders = store.getDefaultFolder().list("*");
			for (Folder folder : folders) {
				if ((folder.getType() & Folder.HOLDS_MESSAGES) != 0) {
					String folderName = folder.getName();
					if (folderName.contains("Inbox.Sent")) {
						Folder sentFolder = store.getFolder(folderName);
						sentFolder.open(Folder.READ_WRITE);
						MimeMessage message = new MimeMessage(emailSession);
						message.setFrom(new InternetAddress(username));
						message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
						message.setSubject(subject);
						message.setText(body);
						sentFolder.appendMessages(new Message[] { message });
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// imaps for recieving emails
	@Value("${mail.store.protocol}")
	String protocol;

	@Value("${mail.imaps.host}")
	String host;

	@Value("${mail.imaps.port}")
	String port;

	@Value("${spring.mail.username}")
	String userName;

	@Value("${spring.mail.password}")
	String password;


	private String lastProcessedEmailUID = null;

//	@Scheduled(fixedRate = 10000) // every 10 seconds
	public void receiveEmail() throws IOException, MessagingException {
		logger.info("class::  EmailService -> method receiveEmail() ::  protocol "+  protocol+ " host "+host+"  port "+  port +"from "+userName);
		Properties prop = new Properties();
		prop.setProperty("mail.store.protocol", protocol);
		prop.setProperty("mail.imaps.host", host);
		prop.setProperty("mail.imaps.port", port);
		Session session = Session.getDefaultInstance(prop);

		Store store = null;
		Folder inbox = null;

		try {
			store = session.getStore();
			store.connect(host, userName, password);
			inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);

			// Get messages
			Message[] messages = inbox.getMessages();
			if (messages.length > 0) {
				Message latestMessage = messages[messages.length - 1];
				String latestMessageUID = latestMessage.getHeader("Message-ID")[0]; // Use a unique identifier

//
//	            // Process the new email
//	            String subject = latestMessage.getSubject();
//	            if (subject.toLowerCase().contains("apply".toLowerCase()))  {
//	            boolean hasFiles = hasFiles(latestMessage);
//	            List<FileData> fileDataList = null;
//
//				// Check if this message has already been processed
//				if (latestMessageUID.equals(lastProcessedEmailUID)) {
//					System.out.println("No new email to process.");
//					return; // Exit if this email has already been processed
//				}
//				lastProcessedEmailUID = latestMessageUID; // Update the last processed UID

				// Process the new email
				String subject = latestMessage.getSubject();
				if (subject.contains("Apply")) {
					boolean hasFiles = hasFiles(latestMessage);
					List<FileData> fileDataList = null;

					if (hasFiles) {
						System.out.println("File Found");
						fileDataList = getFilesFromMail(latestMessage);
					} else {
						System.out.println("The Content does not contain any File");
					}

					String content = getContentFromMail(latestMessage);
					System.out.println("Subject: " + subject);
					String from = getFromAddress(latestMessage);
					System.out.println(from);
					System.out.println("Content: " + content);
					boolean hasLink = containsLink(content);
					List<String> links = null;
					if (hasLink) {
						links = extractLinks(content);
						System.out.println("The content contains a link.");
					} else {
						System.out.println("The content does not contain a link.");
					}

					List<String> to = getToAddresses(latestMessage);


					processApplication(from, fileDataList, links, content, to);
				} else {
					System.out.println("Message not processed" +"\n"+"It not a application message");
				}
			}

			inbox.close(false);
			store.close();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
		catch (MessagingException e) {
			e.printStackTrace();
		}finally {
			// Close resources
			if (inbox != null && inbox.isOpen()) {
				inbox.close(false);
			}
			if (store != null) {
				store.close();
			}
		}

	}

	private void processApplication(String from, List<FileData> fileDataList, List<String> links, String content, List<String> to) {
		if (userRepository.findUserByEmail(from) == null) {
			User user = new User();
			user.setUserEmail(from);
			user.setUserRole("Candidate");
			userRepository.save(user);
		}
		Resume resume = new Resume();
		resume.setUserId(userRepository.getUserIdByEmail(from));

		if (fileDataList != null) {
			int count = 1;
			for (FileData file : fileDataList) {
				resume.setFileName(file.getFileName());
				resume.setFileType("file");
				resume.setContent(file.getContent());
				resume.setMessage(from.substring(0, from.indexOf("@")) + " resume" + count++);
				resume = resumeRepository.save(resume);
			}
		} else if (links != null) {
			int count = 1;
			for (String link : links) {
				resume.setUserId(userRepository.getUserIdByEmail(from));
				resume.setFileName(link);
				resume.setFileType("link");
				resume.setMessage(from.substring(0, from.indexOf("@")) + " resumeLink" + count++);
				resume = resumeRepository.save(resume);
			}
		} else {
			resume.setUserId(userRepository.getUserIdByEmail(from));
			resume.setFileName(content);
			resume.setFileType("brief");
			resume.setMessage(from.substring(0, from.indexOf("@")) + " resumeBrief");
			resume = resumeRepository.save(resume);
		}

		for (String toEmail : to) {
			Application application = new Application();
			Date date = new Date();
			application.setAppliedOn(date);
			application.setCompanyName(toEmail.substring(0, toEmail.indexOf("@")).toUpperCase());
			application.setCandidateId(userRepository.getUserIdByEmail(from));
			application.setApplicationStatus("Not Seen");

			if (companyRepository.findCompanyByName(toEmail.substring(0, toEmail.indexOf("@"))) == null) {
				Company company = new Company();
				company.setCompanyName(toEmail.substring(0, toEmail.indexOf("@")).toUpperCase());
				company.setJobboxEmail(toEmail);
				company.setDate(date);
				companyRepository.save(company);
			}

			application.setResumeId(resume.getId());
			applicationRepository.save(application);
		}
	}

	public static boolean hasFiles(Message latestMessage) throws MessagingException {
		try {
			Object content = latestMessage.getContent();
			if (content instanceof Multipart) {
				Multipart multipart = (Multipart) content;
				for (int i = 0; i < multipart.getCount(); i++) {
					BodyPart bodyPart = multipart.getBodyPart(i);
					if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
						return true; // Found an attachment
					}
				}
			}
		} catch (MessagingException | IOException e) {
			e.printStackTrace(); // Handle the exception appropriately
		}
		return false; // No attachments found
	}

	private List<FileData> getFilesFromMail(Message message) throws IOException, MessagingException {
		List<FileData> fileDataList = new ArrayList<>();
		if (message.getContent() instanceof Multipart) {
			Multipart multipart = (Multipart) message.getContent();
			for (int i = 0; i < multipart.getCount(); i++) {
				BodyPart bodyPart = multipart.getBodyPart(i);
				if (bodyPart.getDisposition() != null && bodyPart.getDisposition().equalsIgnoreCase("attachment")) {
					String fileName = bodyPart.getFileName();
					String fileType = bodyPart.getContentType();
					byte[] content = IOUtils.toByteArray(bodyPart.getInputStream());
					fileDataList.add(new FileData(fileName, fileType, content));
				}
			}
		}
		return fileDataList;
	}

	public static String getContentFromMail(Message latestMessage) throws MessagingException, IOException {
		Object content = latestMessage.getContent();
		if (content instanceof String) {
			return (String) content;
		} else if (content instanceof Multipart) {
			Multipart multipart = (Multipart) content;
			for (int i = 0; i < multipart.getCount(); i++) {
				BodyPart bodyPart = multipart.getBodyPart(i);
				if (bodyPart.isMimeType("text/plain")) {
					return (String) bodyPart.getContent();
				}
			}
		}
		return "";
	}

	public String getFromAddress(Message message) throws MessagingException {
		Address[] fromAddresses = message.getFrom();
		if (fromAddresses != null && fromAddresses.length > 0) {
			return ((InternetAddress) fromAddresses[0]).getAddress();
		}
		return null;
	}

	public static List<String> getToAddresses(Message message) throws MessagingException {
		Address[] toAddresses = message.getRecipients(Message.RecipientType.TO);
		List<String> toEmails = new ArrayList<>();
		if (toAddresses != null) {
			for (Address address : toAddresses) {
				toEmails.add(((InternetAddress) address).getAddress());
			}
		}
		return toEmails;
	}

	private boolean containsLink(String content) {
		Pattern linkPattern = Pattern.compile("https?://\\S+");
		Matcher matcher = linkPattern.matcher(content);
		return matcher.find();
	}

	private List<String> extractLinks(String content) {
		Pattern linkPattern = Pattern.compile("https?://\\S+");
		Matcher matcher = linkPattern.matcher(content);
		List<String> links = new ArrayList<>();
		while (matcher.find()) {
			links.add(matcher.group());
		}
		return links;
	}

	// Helper class to store file data
	private static class FileData {
		private String fileName;
		private String fileType;
		private byte[] content;

		public FileData(String fileName, String fileType, byte[] content) {
			this.fileName = fileName;
			this.fileType = fileType;
			this.content = content;
		}

		public String getFileName() {
			return fileName;
		}

		public String getFileType() {
			return fileType;
		}

		public byte[] getContent() {
			return content;
		}
	}
}
