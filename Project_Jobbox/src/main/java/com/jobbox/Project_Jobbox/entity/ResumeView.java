package com.jobbox.Project_Jobbox.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ResumeView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int rid;
    @ManyToOne
    @JoinColumn(name = "resume_id", nullable = false)
	private Resume resume;
	private int hrId; // HR who viewed the resume
	private int candidateId; // Candidate whose resume is viewed
	private LocalDateTime viewedOn; // Timestamp when viewed
	
	
	public int getRid() {
		return rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}
	public Resume getResume() {
		return resume;
	}
	public void setResume(Resume resume) {
		this.resume = resume;
	}
	public int getHrId() {
		return hrId;
	}
	public void setHrId(int hrId) {
		this.hrId = hrId;
	}
	public int getCandidateId() {
		return candidateId;
	}
	public void setCandidateId(int candidateId) {
		this.candidateId = candidateId;
	}
	public LocalDateTime getViewedOn() {
		return viewedOn;
	}
	public void setViewedOn(LocalDateTime viewedOn) {
		this.viewedOn = viewedOn;
	}
	
	
	public ResumeView() {
    }
	
	public ResumeView(Resume resume, int hrId, int candidateId, LocalDateTime viewedOn) {
		super();
		this.rid = rid;
		this.resume = resume;
		this.hrId = hrId;
		this.candidateId = candidateId;
		this.viewedOn = viewedOn;
	}
	
	@Override
	public String toString() {
		return "ResumeView [rid=" + rid + ", resume=" + resume + ", hrId=" + hrId + ", candidateId=" + candidateId
				+ ", viewedOn=" + viewedOn + "]";
	}
	
	
	
}
