package com.jobbox.Project_Jobbox.serviceImpl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jobbox.Project_Jobbox.entity.Company;
import com.jobbox.Project_Jobbox.entity.CompanyPolicyDocuments;
import com.jobbox.Project_Jobbox.repository.CompanyPolicyDocumentsRepository;
import com.jobbox.Project_Jobbox.repository.CompanyRepository;
import com.jobbox.Project_Jobbox.service.CompanyPolicyDocumentsService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CompanyPolicyDocumentsServiceImpl implements CompanyPolicyDocumentsService {

	@Autowired
	private CompanyPolicyDocumentsRepository companyPolicyDocumentsRepository;
	@Autowired
	private CompanyRepository companyRepository;
	
	public String saveDocument(String companyName, String documentTitle, MultipartFile documentFile) throws IOException {
        // Fetch company by name
        Company company = companyRepository.findCompanyByName(companyName);
        if (company == null) {
            throw new EntityNotFoundException("Company with name " + companyName + " not found.");
        }
        // Save document
        CompanyPolicyDocuments document = new CompanyPolicyDocuments();
        document.setCompany(company); // Save company ID as a reference
        document.setDocumentTitle(documentTitle);
        document.setDocumentFile(documentFile.getBytes()); // Convert file to byte[]

        companyPolicyDocumentsRepository.save(document);

        return "Document saved successfully!";
    }
	
	@Override
	public List<CompanyPolicyDocuments> getDocumentsByCompanyName(String companyName) {
        // Fetch the company by name
        Company company = companyRepository.findCompanyByName(companyName);
        if (company == null) {
            throw new EntityNotFoundException("Company with name " + companyName + " not found.");
        }

        // Fetch and return documents associated with the companyId
        return companyPolicyDocumentsRepository.findByCompany(company);
    }
}
