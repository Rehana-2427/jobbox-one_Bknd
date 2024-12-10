package com.jobbox.Project_Jobbox.controller;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jobbox.Project_Jobbox.entity.Company;
import com.jobbox.Project_Jobbox.entity.HiringPolicy;
import com.jobbox.Project_Jobbox.service.CompanyService;

@CrossOrigin(origins = { "http://51.79.18.21:3000", "http://localhost:3000","http://jobbox.one" })
@Controller
@RequestMapping("/api/jobbox")
@RestController
public class CompanyController {

	@Autowired
	public CompanyService service;

	@PostMapping("/saveCompany")
	public ResponseEntity<String> saveCompany(@RequestBody Company c)

	{
		String s = service.saveCompany(c);
		if (s == null) {
			return new ResponseEntity<String>("company already present", HttpStatus.NOT_ACCEPTABLE);
		} else
			return new ResponseEntity<String>("company save succesfully", HttpStatus.CREATED);

	}

	// comapines list which are not verified by admin
	@GetMapping("/displayCompanies")
	public ResponseEntity<Page<Company>> displayCompany(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String sortOrder) {
		try {
			Page<Company> companies = service.getCompanies(page, size, sortBy, sortOrder);
			return new ResponseEntity<>(companies, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/companiesList")
	public ResponseEntity<Page<Company>> companiesList(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String sortOrder) {
		try {
			Page<Company> companies = service.companiesList(page, size, sortBy, sortOrder);
			// companies.getContent().forEach(company -> {
			// System.out.println("Company: " + company);
			// });
			return new ResponseEntity<Page<Company>>(companies, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/searchCompany")
	public ResponseEntity<Page<Company>> displayCompanyBySearch(@RequestParam String search,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size ,
			@RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String sortOrder 
			) {

		return new ResponseEntity<Page<Company>>(service.findCompanyBySearch(search, page, size, sortBy,sortOrder), HttpStatus.OK);
	}
	// update company approval status
	@PutMapping("/updateApproveCompany")
	public ResponseEntity<String> updateCompany(@RequestParam String companyName, @RequestParam String actionDate,
			@RequestParam String companyStatus) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date approveDate = null;
		try {
			approveDate = sdf.parse(actionDate);
		} catch (Exception e) {
			// TODO: handle exception
		}
		service.updateCompanyStatus(companyName, approveDate, companyStatus);
		return new ResponseEntity<String>("update successFull", HttpStatus.OK);
	}

	@GetMapping("/getCompanyByName/{companyName}")
	public ResponseEntity<Company> getCompanyByName(@PathVariable String companyName) {
	    Company company = service.findCompanyByName(companyName);
	    if (company != null) {
	        return new ResponseEntity<>(company, HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}


	@PutMapping("/updateCompanyByName")
	public ResponseEntity<String> updateCompanyDetails(@RequestParam String companyName,
			@RequestBody Company companyDetails) {
		Company company = service.findCompanyByName(companyName);
		company.setLocation(companyDetails.getLocation());
		company = service.updateCompanyDetails(company);
		return new ResponseEntity<>("Update successful", HttpStatus.OK);
	}

	@PutMapping("/updateCompanyDetailsByHR")
	public ResponseEntity<String> updateCompanyDetailsByHR(@RequestParam String companyName,
			@RequestBody Company companyDetails) {
		service.updateCompanyDetailsByHR(companyDetails, companyName);
		return new ResponseEntity<>("Update successful", HttpStatus.OK);
	}

	@PutMapping("/updateCompanyDetailsByAdmin")
	public ResponseEntity<String> updateCompanyDetailsByAdmin(@RequestParam String companyName,
			@RequestBody Company companyDetails) {
		service.updateCompanyDetailsByAdmin(companyDetails, companyName);
		return new ResponseEntity<>("Update successful", HttpStatus.OK);
	}

	@PutMapping("/updateSocialMediaLinks")
	public ResponseEntity<String> updateSocialMediaLinks(@RequestParam String companyName,
			@RequestBody Company companyDetails) {
		service.updateSocialMediaLinks(companyDetails, companyName);
		return new ResponseEntity<>("Update successful", HttpStatus.OK);
	}

	@GetMapping("/getSocialMediaLinks")
	public ResponseEntity<Company> getSocialMediaLinks(@RequestParam String companyName) {
		return new ResponseEntity<Company>(service.getSocialMediaLinks(companyName), HttpStatus.OK);

	}

	@GetMapping("/countValidatedCompanies")
	public ResponseEntity<Integer> countTotalCompanies() {
		return new ResponseEntity<Integer>(service.countTotalCompanies(), HttpStatus.OK);
	}

	@GetMapping("/getCountOfTotalCompany")
	public ResponseEntity<Integer> getCountOfTotalCompany() {
		return new ResponseEntity<Integer>(service.getCountOfTotalCompany(), HttpStatus.OK);
	}

	@GetMapping("/displayCompanyById")
	public ResponseEntity<Company> displayCompany(@RequestParam int companyId) {
		return new ResponseEntity<Company>(service.findCompanyById(companyId), HttpStatus.OK);
	}




	@GetMapping("/findCompany")
	public ResponseEntity<Company> displayCompanyByName(@RequestParam String companyName) {
		return new ResponseEntity<Company>(service.findCompanyByName(companyName), HttpStatus.OK);
	}

	@GetMapping("/countOfCompanies")
	public ResponseEntity<Integer> countOfCompanies() {
		return new ResponseEntity<Integer>(service.countOfCompanies(), HttpStatus.OK);
	}

	@GetMapping("/countValidateCompaniesByMonth")
	public ResponseEntity<Map<Integer, Double>> getCountValidateCompanyByEachMonth() {
		Map<Integer, Double> countValidateCompanyByEachMonth = service.getCountValidateCompanyByEachMonth();
		return new ResponseEntity<Map<Integer, Double>>(countValidateCompanyByEachMonth, HttpStatus.OK);
	}

	@PostMapping("/uploadLogo")
	public ResponseEntity<String> uploadCompanyLogo(@RequestParam String companyName,
			@RequestParam("file") MultipartFile file) {
		return service.uploadCompanyLogo(companyName, file);
	}

	@PostMapping("/uploadBanner")
	public ResponseEntity<String> uploadCompanyBanner(@RequestParam String companyName,
			@RequestParam("file") MultipartFile file) {
		return service.uploadCompanyBanner(companyName, file);
	}

	@GetMapping("/logo")
	public ResponseEntity<byte[]> getCompanyLogo(@RequestParam String companyName) {
		return service.getCompanyLogo(companyName);
	}

	@GetMapping("/banner")
	public ResponseEntity<byte[]> getCompanyBanner(@RequestParam String companyName) {
		return service.getCompanyBanner(companyName);
	}

	@GetMapping("/companylogos")
	public ResponseEntity<Map<Integer, String>> getCompanyLogos() {
		// Assuming the service method returns a Map<Integer, byte[]> where Integer is
		// the companyId
		Map<Integer, byte[]> logosMap = service.getCompanyLogos();

		// Convert the byte arrays to Base64 strings
		Map<Integer, String> base64LogosMap = logosMap.entrySet().stream().collect(
				Collectors.toMap(Map.Entry::getKey, entry -> Base64.getEncoder().encodeToString(entry.getValue())));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<>(base64LogosMap, headers, HttpStatus.OK);
	}

	@GetMapping("/countAppliedCompanies")
	public ResponseEntity<Integer> countOfAppliedCompanies(@RequestParam int userId) {
		return new ResponseEntity<Integer>(service.countOfAppliedCompanies(userId), HttpStatus.OK);
	}

	@GetMapping("/appliedCompanies")
	public ResponseEntity<Page<Company>> appliedCompanies(@RequestParam int userId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
		return new ResponseEntity<Page<Company>>(service.findAppliedCompanyByUser(userId, page, size), HttpStatus.OK);
	}


	@GetMapping("/companyTypes")
	public ResponseEntity<List<String>> getCompanyTypes() {
		return new ResponseEntity<List<String>>(service.getAllCompanyTypes(), HttpStatus.OK);
	}


	@GetMapping("/industryTypes")
	public ResponseEntity<List<String>> getIndustryTypes() {
		return new ResponseEntity<List<String>>(service.getAllIndustryTypes(), HttpStatus.OK);
	}

//	@GetMapping("/locations")
//	public ResponseEntity<List<String>> getLocations() {
//		return new ResponseEntity<List<String>>(service.getAllLocations(), HttpStatus.OK);
//	}

//	@GetMapping("/companiesByType")
//	public Page<Company> companiesByType(@RequestParam(required = false) String companyType,
//			@RequestParam(required = false) String industryType, @RequestParam(required = false) String location,
//			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
//		return service.getCompaniesByFilters(companyType, industryType, location, page, size);
//	}

	@PutMapping("/updateHiringPolicy")
	public ResponseEntity<String> updateHiringPolicy(@RequestParam String companyName,
	                                                 @RequestBody HiringPolicy hiringPolicyDetails) {
	    service.updateHiringPolicy(hiringPolicyDetails, companyName);
	    return new ResponseEntity<>("Hiring policy update successful", HttpStatus.OK);
	}

	
	@GetMapping("/getHiringPolicy")
    public ResponseEntity<HiringPolicy> getHiringPolicy(@RequestParam String companyName) {
        // Call the service to fetch the hiring policy
        HiringPolicy hiringPolicy = service.getHiringPolicy(companyName);

        if (hiringPolicy != null) {
            return new ResponseEntity<>(hiringPolicy, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Return 404 if the hiring policy or company is not found
    }


	@GetMapping("/locations")
	public ResponseEntity<List<String>> getLocations() {
		return  new ResponseEntity<List<String>>(service.getAllLocations(), HttpStatus.OK) ;		
	}

	@GetMapping("/companiesByType")
	public Page<Company> companiesByType(
			@RequestParam(required = false) String companyType,
			@RequestParam(required = false) String industryType,
			@RequestParam(required = false) String location,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size
			) {
		return service.getCompaniesByFilters(companyType, industryType, location, page,size);
	}

	// Endpoint to search companies by name
	@GetMapping("/searchCompanyNames")
	public ResponseEntity<List<Company>> searchCompany(@RequestParam String companyName) {
		return  new ResponseEntity<List<Company>>(service.searchCompanies(companyName),HttpStatus.OK);
	}

	@PutMapping("/mergeCompany")
	public ResponseEntity<Company> mergeCompany(@RequestParam String mergeWithCompanyName, @RequestParam int companyId ){
		return new ResponseEntity<Company>(service.mergeCompany(mergeWithCompanyName,companyId),HttpStatus.OK);

	}
	
	@GetMapping("/checkCompanyByName")
	public ResponseEntity<String> checkCompanyByName(@RequestParam String companyName) {
	    boolean companyExists = service.isCompanyExists(companyName);
	    if (companyExists) {
	        return new ResponseEntity<>("Company exists", HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>("Company not found", HttpStatus.NOT_FOUND);
	    }
	}


}




















