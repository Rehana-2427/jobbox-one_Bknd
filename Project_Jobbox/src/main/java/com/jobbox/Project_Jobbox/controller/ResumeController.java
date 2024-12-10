package com.jobbox.Project_Jobbox.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jobbox.Project_Jobbox.entity.Resume;
import com.jobbox.Project_Jobbox.repository.ResumeViewRepository;
import com.jobbox.Project_Jobbox.service.ResumeService;

@CrossOrigin(origins = { "http://51.79.18.21:3000", "http://localhost:3000","http://jobbox.one" })
@Controller
@RequestMapping("/api/jobbox")
@RestController
public class ResumeController {

	@Autowired
	public ResumeService resumeService;

	@PostMapping("/uploadResume")
	public ResponseEntity<String> uploadResume(@RequestParam("userId") int userId,
			@RequestParam("fileType") String fileType,
			@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "link", required = false) String link,
			@RequestParam(value = "briefMessage", required = false) String briefMessage,
			@RequestParam(value = "message", required = false) String message) {
		// Handle the uploaded file and other form data here
		// For demonstration purposes, let's just print the received data
		System.out.println("User ID: " + userId);
		System.out.println("File Type: " + fileType);
		if (file != null) {
			System.out.println("File Name: " + file.getOriginalFilename());
		}
		if (link != null) {
			System.out.println("Link: " + link);
		}
		if (briefMessage != null) {
			System.out.println("Brief Message: " + briefMessage);
		}
		if (message != null) {
			System.out.println("Message: " + message);
		}
		// Return a success message to the client
		ResponseEntity<String> res = resumeService.uploadResume(file, message, fileType, userId, link, briefMessage);
		return res;

	}

	@GetMapping("/getResume")
	public ResponseEntity<List<Resume>> downloadResume(@RequestParam int userId) {
		List<Resume> downloadResumes = resumeService.downloadResume(userId);
		return new ResponseEntity<List<Resume>>(downloadResumes, HttpStatus.OK);
	}

	@GetMapping("/getCountOfResumes")
	public ResponseEntity<Integer> getCountOfresumeEachUser(@RequestParam int userId) {
		return new ResponseEntity<Integer>(resumeService.getCountOfResumeEachUser(userId), HttpStatus.OK);

	}

	@GetMapping("/getApplicantResume")
	public ResponseEntity<Resume> getApplicantResume(@RequestParam String resumeUrl, @RequestParam int candidateId) {
		return new ResponseEntity<Resume>(resumeService.getResume(resumeUrl, candidateId), HttpStatus.OK);
	}

	@GetMapping("/downloadResume")
	public ResponseEntity<Resource> downloadResume(@RequestParam Long resumeId) {
		return resumeService.downloadResumeByresumeId(resumeId);
	}

	@GetMapping("/getResumeMessageById")
	public ResponseEntity<String> getResumeMessageById(@RequestParam long resumeId) {
		return new ResponseEntity<String>(resumeService.getResumeMessageById(resumeId), HttpStatus.OK);

	}

	@GetMapping("/getBriefResume")
	public ResponseEntity<String> getBriefResume(@RequestParam long resumeId) {

		return new ResponseEntity<String>(resumeService.getBriefResume(resumeId), HttpStatus.OK);
	}

	@PutMapping("/deleteResume")
	public ResponseEntity<Void> deleteResume(@RequestParam long resumeId) {
		resumeService.deletedResumeById(resumeId);
		return ResponseEntity.noContent().build();

	}

	@GetMapping("/getResumeByApplicationId")
	public ResponseEntity<Resume> getResumeById(@RequestParam long resumeId) {
		return new ResponseEntity<Resume>(resumeService.getResumeById(resumeId), HttpStatus.OK);

	}

	@PostMapping("/resume-increment-view")
	public ResponseEntity<Resume> incrementResumeView(@RequestParam Long resumeId, @RequestParam String action) {
		if (!List.of("brief", "link", "download").contains(action)) {
			return ResponseEntity.badRequest().body(null);
		}

		// Call the service to update the view count
		Resume updatedResume = resumeService.incrementViewCount(resumeId, action);
		return ResponseEntity.ok(updatedResume);
	}
	
	 @GetMapping("/resume-view-count")
    public ResponseEntity<Integer> getTotalViewCount(@RequestParam int userId) {
    	
        return resumeService.getTotalViewCountByUserId(userId);

    }

}
