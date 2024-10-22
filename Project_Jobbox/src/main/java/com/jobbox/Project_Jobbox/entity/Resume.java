package com.jobbox.Project_Jobbox.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity

public class Resume {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String fileName;

	private String message;

	private String fileType;
	private Boolean resumeStatus = true; // Default value
	private int userId;
	@Lob
	@Column(name = "content", columnDefinition = "LONGBLOB")
	private byte[] content;

	public boolean isResumeStatus() {
		return resumeStatus;
	}

	public void setResumeStatus(boolean resumeStatus) {
		this.resumeStatus = resumeStatus;
	}

	public int getUserId() {
		return userId;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getResumeStatus() {
		return resumeStatus;
	}

	public void setResumeStatus(Boolean resumeStatus) {
		resumeStatus = resumeStatus;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Resume(Long id, String fileName, String message, String fileType, int userId, byte[] content) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.message = message;
		this.fileType = fileType;
		this.userId = userId;
		this.content = content;
	}

	public Resume() {
		super();
	}

}
