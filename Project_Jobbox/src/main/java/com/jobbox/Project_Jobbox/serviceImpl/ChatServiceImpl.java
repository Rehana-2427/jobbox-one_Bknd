package com.jobbox.Project_Jobbox.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobbox.Project_Jobbox.entity.Chat;
import com.jobbox.Project_Jobbox.repository.ChatRepository;
import com.jobbox.Project_Jobbox.service.ChatService;

@Service
public class ChatServiceImpl implements ChatService {

	Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);

	@Autowired
	public ChatRepository chatRepository;

	@Override
	public Chat saveHRChat(int applicationId, String hrchat) {
		// TODO Auto-generated method stub
		logger.info("class:: ChatServiceImpl -> method saveHRChat ::{ hrchat : " + hrchat + " applicationId : "
				+ applicationId + " }");
		Chat chat = new Chat();
		chat.setApplicationId(applicationId);
		chat.setHrMessage(hrchat);
		chat.setCreatedAt(LocalDateTime.now());
		chat.setHRRead(true);
		chatRepository.save(chat);
		return chat;
	}

	@Override
	public Chat saveCandidateChat(int applicationId, String candidatechat) {
		// TODO Auto-generated method stub
		logger.info("class:: ChatServiceImpl -> method saveCandidateChat ::{candidatechat : " + candidatechat
				+ " applicationId : " + applicationId + " }");
		Chat chat = new Chat();
		chat.setApplicationId(applicationId);
		chat.setCandidateMessage(candidatechat);
		chat.setCreatedAt(LocalDateTime.now());
		chat.setCandidateRead(true);
		chatRepository.save(chat);
		return chat;
	}

	@Override
	public List<Chat> fetchChat(int applicationId) {

		logger.info("class:: ChatServiceImpl -> method fetchChat :: applicationId : " + applicationId + " }");
		System.out.println("application Id  " + applicationId);
		// TODO Auto-generated method stub
		List<Chat> fetchChat = chatRepository.fetchChat(applicationId);
		System.out.println(fetchChat);

		return fetchChat;
	}

	@Override
	public void markCandidateMessagesAsRead(int applicationId) {
		// TODO Auto-generated method stub

		logger.info("class:: ChatServiceImpl -> method markCandidateMessagesAsRead :: applicationId : " + applicationId
				+ " }");
		List<Chat> fetchNotReadChat = chatRepository.fetchHRNotReadChat(applicationId, false);
		for (Chat chat : fetchNotReadChat) {
			chat.setHRRead(true);
			chatRepository.save(chat);
		}
	}

	@Override
	public Integer fetchCountUnreadMessageForCandidateByApplicationId(int applicationId) {
		// TODO Auto-generated method stub

		logger.info(
				"class:: ChatServiceImpl -> method fetchCountUnreadMessageForCandidateByApplicationId :: applicationId : "
						+ applicationId + " }");
		Integer fetchCountUnreadMessageForCandidateByApplicationId = chatRepository
				.fetchCountUnreadMessageForCandidateByApplicationId(applicationId, false);
		System.out.println(fetchCountUnreadMessageForCandidateByApplicationId);
		return fetchCountUnreadMessageForCandidateByApplicationId;
	}

	@Override
	public Integer fetchCountUnreadMessageForHRByApplicationId(int applicationId) {
		// TODO Auto-generated method stub
		logger.info("class:: ChatServiceImpl -> method fetchCountUnreadMessageForHRByApplicationId :: applicationId : "
				+ applicationId + " }");
		Integer fetchCountUnreadMessageForHRByApplicationId = chatRepository
				.fetchCountUnreadMessageForHRByApplicationId(applicationId, false);
		System.out.println(fetchCountUnreadMessageForHRByApplicationId);
		return fetchCountUnreadMessageForHRByApplicationId;
	}

	@Override
	public void markHRMessagesAsRead(int applicationId) {

		logger.info(
				"class:: ChatServiceImpl -> method markHRMessagesAsRead :: applicationId : " + applicationId + " }");
		List<Chat> fetchNotReadChat = chatRepository.fetchCandidateNotReadChat(applicationId, false);
		for (Chat chat : fetchNotReadChat) {
			chat.setCandidateRead(true);
			chatRepository.save(chat);
		}

	}

	@Override
	public Chat saveMessage(Chat message) {
		// TODO Auto-generated method stub
		message.setCreatedAt(LocalDateTime.now());
		return chatRepository.save(message);
	}

	@Override
	public List<Chat> getMessagesByApplicationId(int applicationId) {
		// TODO Auto-generated method stub
		return chatRepository.fetchChat(applicationId);
	}

//	// Update Chat Message
//	@Override
//	public Chat updateChatMessage(Long chatId, String hrMessage, String candidateMessage) {
//		System.out.println("update hr message : "+hrMessage+ " "+"update candidate message : "+ candidateMessage);
//		Optional<Chat> optionalChat = chatRepository.findById(chatId);
//		if (optionalChat.isPresent()) {
//			Chat chat = optionalChat.get();
//			if (hrMessage != null) {
//				chat.setHrMessage(hrMessage);
//			}
//			if (candidateMessage != null) {
//				chat.setCandidateMessage(candidateMessage);
//			}
//			 // Log the chat before and after saving it
//	       Chat save = chatRepository.save(chat);
//			return save;
//		} else {
//			throw new RuntimeException("Chat not found with id: " + chatId);
//		}
//	}

	@Override
	public void updateChatMessage(Long chatId, Chat updatedChat) {
		// Find the existing chat message by ID
		Chat existingChat = chatRepository.getById(chatId);
			
		System.out.println("existingChat" + existingChat.getCreatedAt() );

		// Update the necessary fields
		if (updatedChat.getHrMessage() != null) {
			existingChat.setHrMessage(updatedChat.getHrMessage());
		}
		if (updatedChat.getCandidateMessage() != null) {
			existingChat.setCandidateMessage(updatedChat.getCandidateMessage());
		}
		System.out.println("updatedChat  "+updatedChat.getCreatedAt());
		if (updatedChat.getCreatedAt() == null) {
			existingChat.setCreatedAt(existingChat.getCreatedAt());
		}

		// Save the updated chat
		chatRepository.save(existingChat);
	}

	@Override
	public void deleteChatMessage(Long chatId) {
		// TODO Auto-generated method stub
		if (chatRepository.existsById(chatId)) {
			chatRepository.deleteById(chatId);			
		}

	}
	@Override
	 public boolean deleteMessage(Long chatId) {
	        // Check if the message exists
	        if (chatRepository.existsById(chatId)) {
	            // Delete the message from the database
	        	chatRepository.deleteById(chatId);
	            return true;
	        }
	        return false;
	    }

}
