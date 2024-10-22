package com.jobbox.Project_Jobbox.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int candidateId;
	private String message;
	private boolean isRead;
	private LocalDateTime date;

	public Notification() {
		super();
	}

	public Notification(int id, int candidateId, String message, boolean isRead, LocalDateTime date) {
		super();
		this.id = id;
		this.candidateId = candidateId;
		this.message = message;
		this.isRead = isRead;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(int candidateId) {
		this.candidateId = candidateId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Notification [id=" + id + ", candidateId=" + candidateId + ", message=" + message + ", isRead=" + isRead
				+ ", date=" + date + "]";
	}

}
