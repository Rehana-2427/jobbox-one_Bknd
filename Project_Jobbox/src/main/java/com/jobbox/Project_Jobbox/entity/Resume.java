package com.jobbox.Project_Jobbox.entity;

import java.util.Arrays;

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
	
    private String action; // "download", "brief", "link"
    
    @Column(nullable = false)
    private int viewCount = 0; // Tracks the number of times the resume is viewed
	
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
	

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public Resume() {
		super();
	}

	public Resume(Long id, String fileName, String message, String fileType, String action, int viewCount,
			Boolean resumeStatus, int userId, byte[] content) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.message = message;
		this.fileType = fileType;
		this.action = action;
		this.viewCount = viewCount;
		this.resumeStatus = resumeStatus;
		this.userId = userId;
		this.content = content;
	}

	@Override
	public String toString() {
		return "Resume [id=" + id + ", fileName=" + fileName + ", message=" + message + ", fileType=" + fileType
				+ ", action=" + action + ", viewCount=" + viewCount + ", resumeStatus=" + resumeStatus + ", userId="
				+ userId + ", content=" + Arrays.toString(content) + "]";
	}
	
	

}
