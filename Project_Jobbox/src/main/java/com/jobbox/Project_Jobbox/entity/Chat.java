package com.jobbox.Project_Jobbox.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Chat {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long chatId;
    private int applicationId;
    private String hrMessage;
    private String candidateMessage;
    private LocalDateTime createdAt;
    private boolean isHRRead;
    private boolean isCandidateRead;
	public long getChatId() {
		return chatId;
	}
	public void setChatId(long chatId) {
		this.chatId = chatId;
	}
	public int getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}
	public String getHrMessage() {
		return hrMessage;
	}
	public void setHrMessage(String hrMessage) {
		this.hrMessage = hrMessage;
	}
	public String getCandidateMessage() {
		return candidateMessage;
	}
	public void setCandidateMessage(String candidateMessage) {
		this.candidateMessage = candidateMessage;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public boolean isHRRead() {
		return isHRRead;
	}
	public void setHRRead(boolean isHRRead) {
		this.isHRRead = isHRRead;
	}
	public boolean isCandidateRead() {
		return isCandidateRead;
	}
	public void setCandidateRead(boolean isCandidateRead) {
		this.isCandidateRead = isCandidateRead;
	}
	@Override
	public String toString() {
		return "Chat [chatId=" + chatId + ", applicationId=" + applicationId + ", hrMessage=" + hrMessage
				+ ", candidateMessage=" + candidateMessage + ", createdAt=" + createdAt + ", isHRRead=" + isHRRead
				+ ", isCandidateRead=" + isCandidateRead + "]";
	}

    
    

}
