package com.jobbox.Project_Jobbox.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.jobbox.Project_Jobbox.entity.CompanyPolicyDocuments;

public interface CompanyPolicyDocumentsService {

	String saveDocument(String companyName, String documentTitle, MultipartFile documentFile) throws IOException;

	List<CompanyPolicyDocuments> getDocumentsByCompanyName(String companyName);

}
