package com.jobbox.Project_Jobbox.service;

import java.util.List;

import com.jobbox.Project_Jobbox.entity.Chat;

public interface ChatService {

	Chat saveHRChat(int applicationId, String hrchat);

	List<Chat> fetchChat(int applicationId);

	Chat saveCandidateChat(int applicationId, String candidatechat);

	void markCandidateMessagesAsRead(int applicationId);

	Integer fetchCountUnreadMessageForCandidateByApplicationId(int applicationId);

	Integer fetchCountUnreadMessageForHRByApplicationId(int applicationId);

	void markHRMessagesAsRead(int applicationId);

	Chat saveMessage(Chat message);

	List<Chat> getMessagesByApplicationId(int applicationId);

//	Chat updateChatMessage(Long chatId, String hrMessage, String candidateMessage);

	void deleteChatMessage(Long chatId);

	void updateChatMessage(Long chatId, Chat updatedChat);

	

}
