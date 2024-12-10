package com.jobbox.Project_Jobbox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobbox.Project_Jobbox.service.OtpService;


@CrossOrigin(origins = { "http://51.79.18.21:3000", "http://localhost:3000","http://jobbox.one" })
@Controller
@RequestMapping("/api/jobbox")
@RestController
public class OtpController {

	@Autowired
	public OtpService otpService;

	@GetMapping("/generateOTP")
	public ResponseEntity<Integer> otpSend(@RequestParam String userEmail) {

		return new ResponseEntity<Integer>(otpService.sendOtp(userEmail), HttpStatus.OK);
	}

	@GetMapping("/validateUserEmail")
	public ResponseEntity<?> sendOTP(@RequestParam String userEmail) {
		int otpResult = otpService.sendingOtp(userEmail);
		return new ResponseEntity<>(otpResult, HttpStatus.OK);
	}

}
