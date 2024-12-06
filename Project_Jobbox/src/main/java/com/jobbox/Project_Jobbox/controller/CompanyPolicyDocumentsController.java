package com.jobbox.Project_Jobbox.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jobbox.Project_Jobbox.entity.CompanyPolicyDocuments;
import com.jobbox.Project_Jobbox.service.CompanyPolicyDocumentsService;

import jakarta.persistence.EntityNotFoundException;

@CrossOrigin(origins = { "http://51.79.18.21:3000", "http://localhost:3000" })
@Controller
@RequestMapping("/api/jobbox")
@RestController
public class CompanyPolicyDocumentsController {

	@Autowired
	private CompanyPolicyDocumentsService documentsService;

	@PostMapping("/addPolicyDocuments")
	public ResponseEntity<String> saveDocuments(@RequestParam("companyName") String companyName,
			@RequestParam("title") String documentTitle, @RequestParam("file") MultipartFile documentFile)
			throws IOException {

		try {
			String message = documentsService.saveDocument(companyName, documentTitle, documentFile);
			return ResponseEntity.ok(message);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@GetMapping("/getDocumentsByCompany")
	public ResponseEntity<List<CompanyPolicyDocuments>> getDocumentsByCompany(
			@RequestParam("companyName") String companyName) {
		try {
			List<CompanyPolicyDocuments> documents = documentsService.getDocumentsByCompanyName(companyName);
			if (documents.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			return ResponseEntity.ok(documents);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
}
