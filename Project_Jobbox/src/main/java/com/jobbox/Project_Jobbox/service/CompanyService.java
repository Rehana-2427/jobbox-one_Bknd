package com.jobbox.Project_Jobbox.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.jobbox.Project_Jobbox.entity.Company;
import com.jobbox.Project_Jobbox.entity.HiringPolicy;

public interface CompanyService {

	String saveCompany(Company c);

	Company findCompanyById(int id);

	Company findCompanyByName(String companyName);

	void updateCompany(Company c);

	Page<Company> getCompanies(int page, int size, String sortBy, String sortOrder);

	void updateCompanyStatus(String companyName, Date actionDate, String companyStatus);

	void updateCompanyDetails(String companyName, String location1, String description);

	Integer countTotalCompanies();

	Page<Company> findCompanyBySearch(String search, int page, int size, String sortBy, String sortOrder);

	Integer getCountOfTotalCompany();

	Company updateCompanyDetails(Company company);

	Integer countOfCompanies();

	Page<Company> companiesList(int page, int size, String sortBy, String sortOrder);

	Map<Integer, Double> getCountValidateCompanyByEachMonth();

	Company updateCompanyDetailsByHR(Company company, String companyName);

	ResponseEntity<String> uploadCompanyLogo(String companyName, MultipartFile file);

	ResponseEntity<byte[]> getCompanyLogo(String companyName);

	ResponseEntity<String> uploadCompanyBanner(String companyName, MultipartFile file);

	ResponseEntity<byte[]> getCompanyBanner(String companyName);

	public Map<Integer, byte[]> getCompanyLogos();

	Company updateCompanyDetailsByAdmin(Company companyDetails, String companyName);

	Company updateSocialMediaLinks(Company companyDetails, String companyName);

	Company getSocialMediaLinks(String companyName);

	Integer countOfAppliedCompanies(int userId);

	Page<Company> findAppliedCompanyByUser(int userId, int page, int size);

	List<String> getAllCompanyTypes();

	List<String> getAllIndustryTypes();

	List<String> getAllLocations();

	Page<Company> getCompaniesByFilters(String companyType, String industryType, String location, int page, int size);



	Company updateHiringPolicy(HiringPolicy hiringPolicy, String companyName);

	HiringPolicy getHiringPolicy(String companyName);

	List<Company> searchCompanies(String companyName);

	Company mergeCompany(String cmergeWithCompanyName, int companyId);


	boolean isCompanyExists(String companyName);


	

}
