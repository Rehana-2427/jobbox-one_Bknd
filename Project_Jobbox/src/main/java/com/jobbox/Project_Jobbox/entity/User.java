package com.jobbox.Project_Jobbox.entity;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	private String userName;
	private String userRole;
	private String userEmail;
	private String userStatus;
	@Temporal(TemporalType.DATE)
	private Date approvedOn;

	private String appliedDate; // This field to track when the user was created
	public String companyName;
	public String phone;
	private String password;
	private String companyWebsite;

    private String skills; 
	private String experience;


	@Embedded
	private EducationDetails educationDetails;

	public User(int userId, String userName, String userRole, String userEmail, String userStatus, Date approvedOn,
			String appliedDate, String companyName, String phone, String password, String companyWebsite, String skills,
			String experience, EducationDetails educationDetails) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userRole = userRole;
		this.userEmail = userEmail;
		this.userStatus = userStatus;
		this.approvedOn = approvedOn;
		this.appliedDate = appliedDate;
		this.companyName = companyName;
		this.phone = phone;
		this.password = password;
		this.companyWebsite = companyWebsite;
		this.skills = skills;
		this.experience = experience;
		this.educationDetails = educationDetails;
	}

	public User() {
		super();
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}
	public EducationDetails getEducationDetails() {
		return educationDetails;
	}

	public void setEducationDetails(EducationDetails educationDetails) {
		this.educationDetails = educationDetails;
	}



	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getCompanyWebsite() {
		return companyWebsite;
	}

	public void setCompanyWebsite(String companyWebsite) {
		this.companyWebsite = companyWebsite;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAppliedDate() {
		return appliedDate;
	}

	public void setAppliedDate(String appliedDate) {
		this.appliedDate = appliedDate;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public Date getApprovedOn() {
		return approvedOn;
	}

	public void setApprovedOn(Date approvedOn) {
		this.approvedOn = approvedOn;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", userRole=" + userRole + ", userEmail="
				+ userEmail + ", userStatus=" + userStatus + ", approvedOn=" + approvedOn + ", appliedDate="
				+ appliedDate + ", companyName=" + companyName + ", phone=" + phone + ", password=" + password
				+ ", companyWebsite=" + companyWebsite + ", skills=" + skills + ",  experience=" + experience + "]";
	}
	public String getEducation() {
        return educationDetails != null ? educationDetails.getFormattedEducationDetails() : "N/A";
    }
	
	 @Override
	    public Collection<? extends GrantedAuthority> getAuthorities() {
	        // Return roles or authorities if you have them
	        return Collections.emptyList(); // Replace with actual authorities if you have roles
	    }

	    @Override
	    public boolean isAccountNonExpired() {
	        return true; // Implement as per your requirement
	    }

	    @Override
	    public boolean isAccountNonLocked() {
	        return true; // Implement as per your requirement
	    }

	    @Override
	    public boolean isCredentialsNonExpired() {
	        return true; // Implement as per your requirement
	    }

	    @Override
	    public boolean isEnabled() {
	        return true; // Implement as per your requirement
	    }

		@Override
		public String getUsername() {
			// TODO Auto-generated method stub
			return userEmail;
		}


}
