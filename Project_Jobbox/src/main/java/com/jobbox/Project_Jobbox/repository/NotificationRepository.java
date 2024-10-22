package com.jobbox.Project_Jobbox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jobbox.Project_Jobbox.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

	@Query("SELECT n FROM Notification n WHERE n.isRead = false AND n.candidateId = :candidateId")
	List<Notification> findUnreadNotifications(@Param("candidateId") int candidateId);

}
