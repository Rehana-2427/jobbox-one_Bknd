package com.jobbox.Project_Jobbox.entity;

import java.util.Base64;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int companyId;
	private String companyName;
	private String companyType;



	private String location;
	@Temporal(TemporalType.DATE)
	private Date date;
	@Temporal(TemporalType.DATE)
	private Date actionDate;
	private String companyStatus;
	private String jobboxEmail;

	private String overView;
	private String websiteLink;
	private String companySize;
	private String industryService;

	private String headquaters;
	private String year;
	private String specialties;

	@Lob
	@Column(name = "logo", columnDefinition = "LONGBLOB")
	private byte[] logo;

	@Lob
	@Column(name = "banner", columnDefinition = "LONGBLOB")
	private byte[] banner;

	private String linkedinLink;
	private String instagramLink;
	private String facebookLink;
	private String twitterLink;




	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}
	public String getLinkedinLink() {
		return linkedinLink;
	}

	public void setLinkedinLink(String linkedinLink) {
		this.linkedinLink = linkedinLink;
	}



	public String getInstagramLink() {
		return instagramLink;
	}

	public void setInstagramLink(String instagramLink) {
		this.instagramLink = instagramLink;
	}

	public String getFacebookLink() {
		return facebookLink;
	}

	public void setFacebookLink(String facebookLink) {
		this.facebookLink = facebookLink;
	}

	public String getTwitterLink() {
		return twitterLink;
	}

	public void setTwitterLink(String twitterLink) {
		this.twitterLink = twitterLink;
	}

	public Company() {
		// Default constructor
	}

	public Company(String companyName, byte[] logo) {
		super();
		this.companyName = companyName;
		this.logo = logo;
	}

	public byte[] getLogo() {
		return logo;
	}

	public void setLogo(byte[] logo) {
		this.logo = logo;
	}

	public byte[] getBanner() {
		return banner;
	}

	public void setBanner(byte[] banner) {
		this.banner = banner;
	}

	public String getLogoBase64() {
		return logo != null ? Base64.getEncoder().encodeToString(logo) : null;
	}

	public String getBannerBase64() {
		return banner != null ? Base64.getEncoder().encodeToString(banner) : null;
	}

	public String getIndustryService() {
		return industryService;
	}

	public void setIndustryService(String industryService) {
		this.industryService = industryService;
	}

	public String getOverView() {
		return overView;
	}

	public void setOverView(String overView) {
		this.overView = overView;
	}

	public String getWebsiteLink() {
		return websiteLink;
	}

	public void setWebsiteLink(String websiteLink) {
		this.websiteLink = websiteLink;
	}



	public String getHeadquaters() {
		return headquaters;
	}

	public void setHeadquaters(String headquaters) {
		this.headquaters = headquaters;
	}


	public String getSpecialties() {
		return specialties;
	}

	public void setSpecialties(String specialties) {
		this.specialties = specialties;
	}

	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}

	public String getCompanyStatus() {
		return companyStatus;
	}

	public void setCompanyStatus(String companyStatus) {
		this.companyStatus = companyStatus;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getJobboxEmail() {
		return jobboxEmail;
	}

	public void setJobboxEmail(String jobboxEmail) {
		this.jobboxEmail = jobboxEmail;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}



	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}


	public String getCompanySize() {
		return companySize;
	}


	public void setCompanySize(String companySize) {
		this.companySize = companySize;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	@Override
	public String toString() {
		return "Company [companyId=" + companyId + ", companyName=" + companyName
				+ ", location=" + location +", date=" + date + ", actionDate="
				+ actionDate + ", companyStatus=" + companyStatus + ", jobboxEmail=" + jobboxEmail + ", overView="
				+ overView + ", websiteLink=" + websiteLink + ", companySize=" + companySize + ", industryService="
				+ industryService + ", headquaters=" + headquaters + ", year=" + year + ", specialties=" + specialties
				+ ", linkedinLink=" + linkedinLink + ", instagramLink=" + instagramLink + ", facebookLink="
				+ facebookLink + ", twitterLink=" + twitterLink +", companyType="+ companyType+ "]";
	}


}
