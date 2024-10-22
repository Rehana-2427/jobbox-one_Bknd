package com.jobbox.Project_Jobbox.service;

import org.springframework.stereotype.Service;

@Service
public interface OtpService {

	Integer sendOtp(String userEmail);

	int sendingOtp(String userEmail);
//	    boolean verifyOtp(String userEmail, int enteredOtp);

}
