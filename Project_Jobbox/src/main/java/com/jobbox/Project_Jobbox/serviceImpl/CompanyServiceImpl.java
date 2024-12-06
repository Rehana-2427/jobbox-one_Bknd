package com.jobbox.Project_Jobbox.serviceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jobbox.Project_Jobbox.entity.Application;
import com.jobbox.Project_Jobbox.entity.Company;
import com.jobbox.Project_Jobbox.entity.HiringPolicy;
import com.jobbox.Project_Jobbox.entity.User;
import com.jobbox.Project_Jobbox.repository.ApplicationRepository;
import com.jobbox.Project_Jobbox.repository.CompanyRepository;
import com.jobbox.Project_Jobbox.repository.UserRepository;
import com.jobbox.Project_Jobbox.service.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {

	Logger logger = LoggerFactory.getLogger(CompanyServiceImpl.class);

	@Autowired
	public CompanyRepository repository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ApplicationRepository applicationRepository;

	@Override
	public String saveCompany(Company c) {

		logger.info("class:: CompanyServiceImpl -> method saveCompany ::{ Company : " + c + " }");
		Company com = repository.findByName(c.getCompanyName());
		if (com != null) {
			if (com.getWebsiteLink() == null) {
				com.setWebsiteLink(c.getWebsiteLink());
				repository.save(com);
			}

			return null;
		} else {
			String companyName = c.getCompanyName();
			String jobboxEmail = companyName.toLowerCase().replaceAll(" ", "") + "@jobbox.com";
			c.setJobboxEmail(jobboxEmail);

			repository.save(c);

			return "company saved";
		}

	}

	@Override
	public Company findCompanyById(int id) {

		logger.info("class:: CompanyServiceImpl -> method findCompanyById ::{ id }" + id);

		Optional<Company> optional = repository.findById(id);
		if (optional.isPresent()) {
			Company c = optional.get();
			return c;
		}

		return null;
	}

	@Override
	public Company findCompanyByName(String companyName) {

		return repository.findCompanyByName(companyName);
	}

	@Override
	public Page<Company> getCompanies(int page, int size, String sortBy, String sortOrder) {

		logger.info("class:: CompanyServiceImpl -> method getCompanies ::{ Companies }");
		try {
			PageRequest pageRequest;
			if (sortBy == null || sortBy.isEmpty()) {
				pageRequest = PageRequest.of(page, size); // No sorting
			} else {
				Sort.Direction direction = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.Direction.ASC
						: Sort.Direction.DESC;
				Sort sort = Sort.by(direction, sortBy);
				pageRequest = PageRequest.of(page, size, sort);
			}

			return repository.displayComapnies(pageRequest);
		} catch (Exception e) {
			e.printStackTrace();
			// Handle the exception or return a default page
			return Page.empty();
		}
	}

	@Override
	public Page<Company> companiesList(int page, int size, String sortBy, String sortOrder) {

		logger.info("class:: CompanyServiceImpl -> method companiesList ::{ Companies }");
		try {
			PageRequest pageRequest;
			if (sortBy == null || sortBy.isEmpty()) {
				pageRequest = PageRequest.of(page, size); // No sorting
			} else {
				Sort.Direction direction = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.Direction.ASC
						: Sort.Direction.DESC;
				Sort sort = Sort.by(direction, sortBy);
				pageRequest = PageRequest.of(page, size, sort);
			}

			return repository.companiesList(pageRequest);
		} catch (Exception e) {
			e.printStackTrace();
			// Handle the exception or return a default page
			return Page.empty();
		}
	}

	@Override
	public void updateCompany(Company c) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCompanyStatus(String companyName, Date actionDate, String companyStatus) {
		logger.info("class:: CompanyServiceImpl -> method updateCompanyStatus ::{ companyName : " + companyName
				+ " ActionDate : " + actionDate + " companyStatus : " + companyStatus + " }");
		Company com = repository.findCompanyByName(companyName);
		com.setActionDate(actionDate);
		com.setCompanyStatus(companyStatus);

		repository.save(com);

	}

	@Override
	public void updateCompanyDetails(String companyName, String location1, String description) {
		logger.info("class:: CompanyServiceImpl -> method updateCompanyDetails ::{ companyName : " + companyName
				+ " location : " + location1 + " companyStatus : " + description + " }");
		Company company = repository.findCompanyByName(companyName);
		company.setLocation(location1);
		repository.save(company);
	}

	@Override
	public Integer countTotalCompanies() {

		logger.info("class:: CompanyServiceImpl -> method countTotalCompanies ::{ companyName }");
		return repository.countApprovedCompanies();
	}

	@Override
	public Page<Company> findCompanyBySearch(String search, int page, int size,String sortBy, String sortOrder) {
		// TODO Auto-generated method stub
<<<<<<< HEAD
		logger.info("class:: CompanyServiceImpl -> method  findCompanyBySearch ::{ search : " + search + " }");
		PageRequest pageRequest = PageRequest.of(page, size);
		return repository.findCompanyBySearch(search, pageRequest);
=======
		logger.info("class:: CompanyServiceImpl -> method  findCompanyBySearch ::{ search : "+search+" }");
		try {
			PageRequest pageRequest;
			if (sortBy == null || sortBy.isEmpty()) {
				pageRequest = PageRequest.of(page, size); // No sorting
			} else {
				Sort.Direction direction = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.Direction.ASC
						: Sort.Direction.DESC;
				Sort sort = Sort.by(direction, sortBy);
				pageRequest = PageRequest.of(page, size, sort);
			}

			return repository.findCompanyBySearch(search, pageRequest);
		} catch (Exception e) {
			e.printStackTrace();
			// Handle the exception or return a default page
			return Page.empty();
		}
		
>>>>>>> b99803d84568fb30bdd3765e0003358f7e3dc60d
	}

	@Override
	public Integer getCountOfTotalCompany() {
		// TODO Auto-generated method stub
		return repository.getCountOfTotalCompany();
	}

	@Override
	public Company updateCompanyDetails(Company company) {
		// TODO Auto-generated method stub
		String location = company.getLocation();
		company.setLocation(location);
		return repository.save(company);
	}

	@Override
	public Company updateCompanyDetailsByHR(Company companyDetails, String companyName) {
		// TODO Auto-generated method stub
		logger.info("class:: CompanyServiceImpl -> method updateCompanyDetailsByHR ::{ companyName : " + companyName
				+ " CompanyDetails" + companyDetails + " }");
		Company company = repository.findCompanyByName(companyName);
		company.setOverView(companyDetails.getOverView());
		company.setWebsiteLink(companyDetails.getWebsiteLink());
		company.setCompanySize(companyDetails.getCompanySize());
		company.setIndustryService(companyDetails.getIndustryService());
		company.setHeadquaters(companyDetails.getHeadquaters());
		company.setYear(companyDetails.getYear());
		company.setSpecialties(companyDetails.getSpecialties());
		company.setCompanyType(companyDetails.getCompanyType());

		List<User> hrs = userRepository.getHrBycomapnyName(companyName);
		System.out.println("list size : " + hrs.size() + hrs.toString());
		for (User hr : hrs) {
			hr.setCompanyWebsite(companyDetails.getWebsiteLink());
			System.out.println(companyDetails.getWebsiteLink() + "   " + hr);
			userRepository.save(hr);
		}
		return repository.save(company);
	}

	@Override
	public Company updateCompanyDetailsByAdmin(Company companyDetails, String companyName) {
		logger.info("class:: CompanyServiceImpl -> method updateCompanyDetailsByAdmin ::{ companyName : " + companyName
				+ " CompanyDetails" + companyDetails + " }");
		Company company = repository.findCompanyByName(companyName);
		company.setOverView(companyDetails.getOverView());
		company.setWebsiteLink(companyDetails.getWebsiteLink());
		company.setCompanySize(companyDetails.getCompanySize());
		company.setIndustryService(companyDetails.getIndustryService());
		company.setHeadquaters(companyDetails.getHeadquaters());
		company.setYear(companyDetails.getYear());
		company.setSpecialties(companyDetails.getSpecialties());
		company.setLocation(companyDetails.getLocation());

		List<User> hrs = userRepository.getHrBycomapnyName(companyName);
		System.out.println("list size : " + hrs.size() + hrs.toString());
		for (User hr : hrs) {
			hr.setCompanyWebsite(companyDetails.getWebsiteLink());
			System.out.println(companyDetails.getWebsiteLink() + "   " + hr);
			userRepository.save(hr);
		}
		return repository.save(company);
	}

	@Override
	public Company updateSocialMediaLinks(Company companyDetails, String companyName) {
		logger.info("class:: CompanyServiceImpl -> method updateSocialMediaLinks ::{ companyName : " + companyName
				+ " CompanyDetails" + companyDetails + " }");
		Company company = repository.findCompanyByName(companyName);
		company.setLinkedinLink(companyDetails.getLinkedinLink());
		company.setInstagramLink(companyDetails.getInstagramLink());
		company.setFacebookLink(companyDetails.getFacebookLink());
		company.setTwitterLink(companyDetails.getTwitterLink());
		return repository.save(company);

	}

	@Override
	public Integer countOfCompanies() {
		// TODO Auto-generated method stub
		return repository.countOfCompanies();
	}

	@Override
	public Map<Integer, Double> getCountValidateCompanyByEachMonth() {
		logger.info("class:: CompanyServiceImpl -> method getCountValidateCompanyByEachMonth ::{ }");
		List<Object[]> countValidateCompanyByEachMonth = repository.getCountValidateCompanyByEachMonth();

		int totalCompany = countValidateCompanyByEachMonth.stream()
				.mapToInt(objects -> ((Number) objects[1]).intValue()).sum();

		Map<Integer, Double> monthlyCompanyPercentages = new HashMap<>();
		for (Object[] result : countValidateCompanyByEachMonth) {
			int month = (int) result[0];
			int companyCount = ((Number) result[1]).intValue();
			double percentage = (companyCount / (double) totalCompany) * 100.0;
			monthlyCompanyPercentages.put(month, percentage);
		}

		return monthlyCompanyPercentages;
	}

	public ResponseEntity<String> uploadCompanyLogo(String companyName, MultipartFile file) {
		logger.info("class:: CompanyServiceImpl -> method uploadCompanyLogo ::{ companyName : " + companyName + "}");
		try {
			// Find company by name
			Company company = repository.findByName(companyName);

			if (company == null) {
				return new ResponseEntity<>("Company not found", HttpStatus.NOT_FOUND);
			}

			// Check if the uploaded file is empty
			if (file.isEmpty()) {
				return new ResponseEntity<>("Please select a file to upload", HttpStatus.BAD_REQUEST);
			}

			// Check if the file type is valid
			if (!file.getContentType().startsWith("image")) {
				return new ResponseEntity<>("Only images are allowed", HttpStatus.BAD_REQUEST);
			}

			// Save logo as byte array
			company.setLogo(file.getBytes());
			repository.save(company);

			return new ResponseEntity<>("Logo uploaded successfully", HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<>("Failed to upload logo", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<byte[]> getCompanyLogo(String companyName) {

		logger.info("class:: CompanyServiceImpl -> method  getCompanyLogo ::{ companyName : " + companyName + "}");
		Company company = repository.findByName(companyName);
		if (company != null && company.getLogo() != null) {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG);
			return new ResponseEntity<>(company.getLogo(), headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public ResponseEntity<String> uploadCompanyBanner(String companyName, MultipartFile file) {
		// TODO Auto-generated method stub
		logger.info("class:: CompanyServiceImpl -> method  uploadCompanyBanner ::{ companyName : " + companyName + "}");
		try {
			// Find company by name
			Company company = repository.findByName(companyName);

			if (company == null) {
				return new ResponseEntity<>("Company not found", HttpStatus.NOT_FOUND);
			}

			// Check if the uploaded file is empty
			if (file.isEmpty()) {
				return new ResponseEntity<>("Please select a file to upload", HttpStatus.BAD_REQUEST);
			}

			// Check if the file type is valid
			if (!file.getContentType().startsWith("image")) {
				return new ResponseEntity<>("Only images are allowed", HttpStatus.BAD_REQUEST);
			}

			// Save logo as byte array
			company.setBanner(file.getBytes());
			repository.save(company);

			return new ResponseEntity<>("Banner uploaded successfully", HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<>("Failed to upload Banner", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<byte[]> getCompanyBanner(String companyName) {

		// TODO Auto-generated method stub
		logger.info("class:: CompanyServiceImpl -> method  getCompanyBanner ::{ companyName : " + companyName + "}");
		Company company = repository.findByName(companyName);
		if (company != null && company.getBanner() != null) {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG);
			return new ResponseEntity<>(company.getBanner(), headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public Map<Integer, byte[]> getCompanyLogos() {

		logger.info("class:: CompanyServiceImpl -> method  getCompanyLogos ::{}");
		List<byte[]> companyLogos = repository.findCompanyLogos();
		Map<Integer, byte[]> logosMap = new HashMap<>();
		for (byte[] companyLogo : companyLogos) {
			int companyId = repository.fetchCompanyIdByLogo(companyLogo);
			logosMap.put(companyId, companyLogo);
		}
		return logosMap;
	}

	@Override
	public Company getSocialMediaLinks(String companyName) {

		logger.info("class:: CompanyServiceImpl -> method  getSocialMediaLinks ::{ companyName : " + companyName + "}");
		return repository.findByName(companyName);

	}

	@Override
	public Integer countOfAppliedCompanies(int userId) {
		// TODO Auto-generated method stub
		logger.info("class:: CompanyServiceImpl -> method  countOfAppliedCompanies ::{ userId : " + userId + "}");
		return applicationRepository.countDistinctCompaniesByUserId(userId);
	}

	@Override
	public Page<Company> findAppliedCompanyByUser(int userId, int page, int size) {
		// TODO Auto-generated method stub
		logger.info("class:: CompanyServiceImpl -> method  findAppliedCompanyByUser ::{ userId : " + userId + "}");
		String[] appliedCompanies = applicationRepository.appliedCompanyByUser(userId);
		System.out.println(Arrays.toString(appliedCompanies));
		List<Company> companies = new ArrayList<>();
		for (String appliedCompany : appliedCompanies) {
			Company company = repository.findByName(appliedCompany);
			companies.add(company);
		}
		// Create a Pageable object for pagination
		Pageable pageable = PageRequest.of(page, size);

		// Determine the start and end indices for the sublist
		int start = (int) pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), companies.size());

		// Create a sublist based on the current page and size
		List<Company> pagedCompanies = companies.subList(start, end);

		// Create a PageImpl object to wrap the sublist
		return new PageImpl<>(pagedCompanies, pageable, companies.size());
	}

	@Override
	public List<String> getAllCompanyTypes() {
		// TODO Auto-generated method stub
		return repository.findDistinctCompanyTypes();
	}

	@Override
	public List<String> getAllIndustryTypes() {
		// TODO Auto-generated method stub
		return repository.findDistinctIndustryTypes();
	}

	@Override
	public List<String> getAllLocations() {
		// TODO Auto-generated method stub
		return repository.findDistinctLocations();
	}

	@Override
	public Page<Company> getCompaniesByFilters(String companyType, String industryType, String location, int page,
			int size) {
		// TODO Auto-generated method stub
		System.out.println(companyType + industryType + location);
		Pageable pageable = PageRequest.of(page, size);
		return repository.findByFilters(companyType, industryType, location, pageable);
	}

	@Override
<<<<<<< HEAD
	public Company updateHiringPolicy(HiringPolicy hiringPolicy, String companyName) {
		// TODO Auto-generated method stub
		Company company = repository.findCompanyByName(companyName);
=======
	public List<Company> searchCompanies(String companyName) {
		// TODO Auto-generated method stub
		 return repository.findByNameContainingIgnoreCase(companyName);
	}

	@Override
	public Company mergeCompany(String mergeWithCompanyName, int companyId) {
		// TODO Auto-generated method stub

		String companyName = repository.getCompanyName(companyId);
		applicationRepository.mergeCompany(mergeWithCompanyName, companyName);
		Date date = new Date();
		updateCompanyStatus(companyName,date,"Rejected");
		
			return repository.findCompanyByName(companyName);
		
//		List<Application> applications=applicationRepository.getApplicationByCompanyId(companyId);
//		for(Application application :applications) {
//			application.setcom
//		}
	}

>>>>>>> b99803d84568fb30bdd3765e0003358f7e3dc60d

		// Update the hiring policy
		company.setHiringPolicy(hiringPolicy);

		// If reapply is not allowed, set default value for reapplyMonths (12)
		if (!hiringPolicy.isAllowReapply()) {
			hiringPolicy.setReapplyMonths(12); // Default value
		}

		// Save the updated company entity with the new hiring policy
		return repository.save(company);
	}

	@Override
	public HiringPolicy getHiringPolicy(String companyName) {
		// Fetch the company using the company name
		Company company = repository.findCompanyByName(companyName);

		if (company != null && company.getHiringPolicy() != null) {
			// If company and its hiring policy are found, return the hiring policy
			return company.getHiringPolicy();
		}

		// Return null if no company or policy found
		return null;
	}
}