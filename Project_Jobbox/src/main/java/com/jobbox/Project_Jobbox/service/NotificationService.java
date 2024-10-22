package com.jobbox.Project_Jobbox.service;

import java.util.List;

import com.jobbox.Project_Jobbox.entity.Application;
import com.jobbox.Project_Jobbox.entity.Notification;

public interface NotificationService {

	void sendShortlistNotification(Application application);

	List<Notification> getAllUnreadNotifications(int candidateId);

	void markNotificationsAsRead(int userId, int notificationId);
}
