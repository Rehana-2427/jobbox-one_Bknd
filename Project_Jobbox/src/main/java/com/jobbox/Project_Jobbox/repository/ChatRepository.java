package com.jobbox.Project_Jobbox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jobbox.Project_Jobbox.entity.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

	@Query("SELECT chat from Chat chat where chat.applicationId=?1")
	List<Chat> fetchChat(int applicationId);

	@Query("SELECT chat from Chat chat where chat.applicationId=?1 And chat.isHRRead=?2")
	List<Chat> fetchHRNotReadChat(int applicationId, boolean isRead);

	@Query("SELECT COUNT(c.chatId) FROM Chat c WHERE c.applicationId = :applicationId AND c.isCandidateRead = :isRead")
	Integer fetchCountUnreadMessageForCandidateByApplicationId(int applicationId, boolean isRead);

	@Query("SELECT COUNT(c.chatId) FROM Chat c WHERE c.applicationId = :applicationId AND c.isHRRead = :isRead")
	Integer fetchCountUnreadMessageForHRByApplicationId(int applicationId, boolean isRead);

	@Query("SELECT chat from Chat chat where chat.applicationId=?1 And chat.isCandidateRead=?2")
	List<Chat> fetchCandidateNotReadChat(int applicationId, boolean b);

}
