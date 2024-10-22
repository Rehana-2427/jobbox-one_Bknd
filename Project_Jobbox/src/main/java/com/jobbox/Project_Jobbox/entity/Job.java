package com.jobbox.Project_Jobbox.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Job {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int jobId;

	private String jobTitle;
	private String jobType;

	@Temporal(TemporalType.DATE)
	private Date postingDate;

	private String skills;
	private int numberOfPosition;

	@Temporal(TemporalType.DATE)
	private Date applicationDeadline;

//    @Column(columnDefinition = "LONGTEXT")
	private String jobsummary;

	private String salary;
	private int userId; // Assuming userId is an integer in the User entity
	private String userName;
	private String userEmail;
	private String companyName;

	private Boolean jobStatus = true; // Default value
	private String location;
    @Column(name = "job_category")
	private String jobCategory;

	public String getJobCategory() {
	    return jobCategory;
	}

	public void setJobCategory(String jobCategory) {
	    this.jobCategory = jobCategory;
	}

	
	// Getter and Setter for jobStatus

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Boolean getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(Boolean jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}



	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public Date getPostingDate() {
		return postingDate;
	}

	public void setPostingDate(Date postingDate) {
		this.postingDate = postingDate;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public int getNumberOfPosition() {
		return numberOfPosition;
	}

	public void setNumberOfPosition(int numberOfPosition) {
		this.numberOfPosition = numberOfPosition;
	}

	public Date getApplicationDeadline() {
		return applicationDeadline;
	}

	public void setApplicationDeadline(Date applicationDeadline) {
		this.applicationDeadline = applicationDeadline;
	}

	public String getJobsummary() {
		return jobsummary;
	}

	public void setJobsummary(String jobsummary) {
		this.jobsummary = jobsummary;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Override
	public String toString() {
		return "Job [jobId=" + jobId + ", jobTitle=" + jobTitle + ", jobType=" + jobType + ", postingDate="
				+ postingDate + ", skills=" + skills + ", numberOfPosition=" + numberOfPosition
				+ ", applicationDeadline=" + applicationDeadline + ", jobsummary=" + jobsummary + ", salary=" + salary
				+ ", userId=" + userId + ", userName=" + userName + ", userEmail=" + userEmail + ", companyName="
				+ companyName + ", jobCategory=" + jobCategory + ", jobStatus=" + jobStatus + ", location=" + location
				+ "]";
	}


}
