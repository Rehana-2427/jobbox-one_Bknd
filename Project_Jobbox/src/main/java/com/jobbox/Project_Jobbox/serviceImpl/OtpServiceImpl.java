package com.jobbox.Project_Jobbox.serviceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobbox.Project_Jobbox.entity.User;
import com.jobbox.Project_Jobbox.repository.UserRepository;
import com.jobbox.Project_Jobbox.service.OtpService;

@Service
public class OtpServiceImpl implements OtpService {
	@Autowired
	public UserRepository userRepository;

	@Autowired
	private EmailService emailService;

	// Map to store OTPs temporarily (ideally, use a cache or session-based storage)
	private Map<String, Integer> otpStorage = new HashMap<>();

	@Override
	public Integer sendOtp(String userEmail) {
		// TODO Auto-generated method stub
		System.out.println(userEmail);
		User u = userRepository.findUserByEmail(userEmail);
		System.out.println(u);
		if (u == null) {
			return null;
		}
		Random random = new Random();
		int otp = 100000 + random.nextInt(900000);
		System.out.println(otp);
		String body = "Hello " + u.getUserName() + ",\n\nOTP:." + otp;
		String subject = "Otp";
		// emailService.sendConfirmationEmail(user.getUserEmail(),body,subject);
		emailService.sendEmail(userEmail, subject, body);
		return otp;
	}
	
	@Override
	public int sendingOtp(String userEmail) {
		// Check if the user already exists
		try {
			User user = userRepository.findUserByEmail(userEmail);

			// Generate OTP
			Random random = new Random();
			int otp = 100000 + random.nextInt(900000);
			System.out.println("Generated OTP for " + userEmail + ": " + otp);

			// Store OTP in temporary storage
			otpStorage.put(userEmail, otp);

			// Send OTP via email
			String body = "Hello,\n\nYour OTP for registration is: " + otp;
			String subject = "OTP for Registration";

			emailService.sendEmail(userEmail, subject, body);
			return otp;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return 0;

		//		return otp; // Return OTP to frontend for verification
	}

}
