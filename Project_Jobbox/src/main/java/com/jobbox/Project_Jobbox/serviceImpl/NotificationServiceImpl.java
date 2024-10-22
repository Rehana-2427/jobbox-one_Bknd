package com.jobbox.Project_Jobbox.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobbox.Project_Jobbox.entity.Application;
import com.jobbox.Project_Jobbox.entity.Notification;
import com.jobbox.Project_Jobbox.repository.NotificationRepository;
import com.jobbox.Project_Jobbox.repository.UserRepository;
import com.jobbox.Project_Jobbox.service.NotificationService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Transactional
	public void sendShortlistNotification(Application application) {
		// Construct the notification message
		String message;
		int jobId = application.getJobId();
		if (jobId == 0) {
			message = "You have been shortlisted at " + application.getCompanyName();
		} else {
			message = "You have been shortlisted for the position of " + application.getJobRole() + " at "
					+ application.getCompanyName();
		}
		// Create a new notification
		Notification notification = new Notification();
		notification.setCandidateId(application.getCandidateId()); // Candidate's ID
		notification.setMessage(message);
		notification.setDate(LocalDateTime.now());
		notification.setRead(false); // Initially unread

		// Save the notification
		notificationRepository.save(notification);
	}

	@Override
	public List<Notification> getAllUnreadNotifications(int candidateId) {
		// TODO Auto-generated method stub
		return notificationRepository.findUnreadNotifications(candidateId);
	}

	@Override
	public void markNotificationsAsRead(int userId, int notificationId) {
		Optional<Notification> optionalNotification = notificationRepository.findById(notificationId);

		if (optionalNotification.isPresent()) {
			Notification notification = optionalNotification.get();

			// Check if the notification belongs to the given user
			if (notification.getCandidateId() == userId) {
				notification.setRead(true);
				notificationRepository.save(notification); // Save the updated notification

				notificationRepository.delete(notification); // Delete the notification
			} else {
				// Handle unauthorized access or invalid notification ownership
				throw new IllegalArgumentException("Notification does not belong to the user.");
			}
		} else {
			// Handle notification not found
			throw new EntityNotFoundException("Notification not found with id: " + notificationId);
		}
	}


}
