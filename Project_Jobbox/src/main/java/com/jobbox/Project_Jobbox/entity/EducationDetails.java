package com.jobbox.Project_Jobbox.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class EducationDetails {
	private String degree;
	private String branch;
	private String percentage;
	private String college;
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getPercentage() {
		return percentage;
	}
	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
	public String getCollege() {
		return college;
	}
	public void setCollege(String college) {
		this.college = college;
	}
	@Override
	public String toString() {
		return "EducationDetails [degree=" + degree + ", branch=" + branch + ", percentage=" + percentage + ", college="
				+ college + "]";
	}
	
	 public String getFormattedEducationDetails() {
	        return degree + " in " + branch + " from " + college + " with " + percentage + "%";
	    }
}
