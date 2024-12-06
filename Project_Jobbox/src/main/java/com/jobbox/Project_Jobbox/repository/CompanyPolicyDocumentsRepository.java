package com.jobbox.Project_Jobbox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jobbox.Project_Jobbox.entity.Company;
import com.jobbox.Project_Jobbox.entity.CompanyPolicyDocuments;

@Repository
public interface CompanyPolicyDocumentsRepository extends JpaRepository<CompanyPolicyDocuments, Integer> {

	// Fetch documents by companyId
	@Query
	List<CompanyPolicyDocuments> findByCompany(Company company);
}
