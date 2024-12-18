package com.jobbox.Project_Jobbox.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobbox.Project_Jobbox.entity.Application;
import com.jobbox.Project_Jobbox.repository.UserRepository;
import com.jobbox.Project_Jobbox.service.ApplicationService;
import com.jobbox.Project_Jobbox.service.JobService;
import com.jobbox.Project_Jobbox.service.UserService;

@CrossOrigin(origins = {"https://jobbox.one", "http://localhost:3000"})
@Controller
@RequestMapping("/api/jobbox")
@RestController
public class ApplicationController {

	@Autowired
	public ApplicationService applicationService;

	@Autowired
	public UserService userService;

	@Autowired
	public JobService jobService;

	@Autowired
	private UserRepository userRepository;

	@PutMapping("/applyJob")
	public ResponseEntity<Application> applyJob(@RequestParam int jobId, @RequestParam int userId,
			@RequestParam String formattedDate, @RequestParam long resumeId) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date appliedOn = null;
		try {
			appliedOn = sdf.parse(formattedDate);
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println(jobId + userId);
		return new ResponseEntity<Application>(applicationService.applyJob(jobId, userId, appliedOn, resumeId),
				HttpStatus.OK);

	}

	@PutMapping("/applyDreamCompany")
	public ResponseEntity<Application> applyDreamCompany(@RequestParam int userId, @RequestParam String companyName,@RequestParam(required = false) String jobRole, 
			@RequestParam String formattedDate, @RequestParam long resumeId) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date appliedOn = null;
		try {
			appliedOn = sdf.parse(formattedDate);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return new ResponseEntity<Application>(
				applicationService.applyDreamCompany(userId, companyName,jobRole, appliedOn, resumeId), HttpStatus.OK);
	}

	// update application status
	@PutMapping("/updateApplicationStatus")
	public ResponseEntity<Application> updateApplicatioStatus(@RequestParam int applicationId,
			@RequestParam String newStatus, @RequestParam String hrEmail) {
		Application app = applicationService.updateApplicationStatus(applicationId, newStatus, hrEmail);
		return new ResponseEntity<Application>(app, HttpStatus.OK);
	}

	@GetMapping("/getApplication")
	public ResponseEntity<Application> getApplication(@RequestParam int applicationId) {
		Application app = applicationService.getApplication(applicationId);
		return new ResponseEntity<Application>(app, HttpStatus.OK);

	}

	// List of Applications pagination and sorting according to job
	@GetMapping("/getApplicationsByJobIdWithPagination")
	public ResponseEntity<Page<Application>> getApplicationsByJobIdWithPagination(@RequestParam int jobId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			@RequestParam(required = false) String sortBy, @RequestParam(required = false) String sortOrder) {
		Page<Application> applications = null;
		try {
			applications = applicationService.getApplicationsByJobIdWithPagination(jobId, page, size, sortBy,
					sortOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<Page<Application>>(applications, HttpStatus.OK);

	}

	// getting count of companies applied b candidates
	@GetMapping("/getCountOfAppliedCompany")
	public ResponseEntity<Integer> getCountOfAppliedCompany(@RequestParam int userId) {
		int companies = applicationService.getCountOfAppliedCompany(userId);
		return new ResponseEntity<Integer>(companies, HttpStatus.OK);

	}

	// getting count of shortlisted applications of a candidiate
	@GetMapping("/getCountOfTotalShortlistedApplication")
	public ResponseEntity<Integer> getCountOfShortListedApplications(@RequestParam int userId) {
		int shortlistedApplications = applicationService.getCountOfShortListedApplications(userId);
		return new ResponseEntity<Integer>(shortlistedApplications, HttpStatus.OK);

	}

	// getting count of shortlisted applications of a candidiate
	@GetMapping("/getCountOfTotalShortlistedApplicationCompany")
	public ResponseEntity<Integer> getCountOfShortListedApplicationsByCompany(@RequestParam int userId,
			String companyName) {
		int shortlistedApplications = applicationService.getCountOfTotalShortlistedApplicationCompany(userId,
				companyName);
		return new ResponseEntity<Integer>(shortlistedApplications, HttpStatus.OK);

	}

	// count of applications got by each company
	@GetMapping("/countOfApplicationsByCompany")
	public ResponseEntity<Integer> getCountOfApplicationByCompany(@RequestParam String companyName) {
		int applications = applicationService.getCountOfApplicationsCompany(companyName);
		return new ResponseEntity<Integer>(applications, HttpStatus.OK);
	}

	// count of under review candidates related to a particular hr
	@GetMapping("/CountOfUnderReviewCandidateBYHRJob")
	public ResponseEntity<Integer> getCountOfUnderPreviewCandidatesByHrJob(@RequestParam String userEmail) {
		int applications = applicationService.getCountUnderReviewCandidateByHrJob(userEmail);
		return new ResponseEntity<Integer>(applications, HttpStatus.OK);
	}

	@GetMapping("/getFilterApplicationsByJobIdWithpagination")
	public ResponseEntity<Page<Application>> getFilterApplications(@RequestParam int jobId,
			@RequestParam String filterStatus, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {

		Page<Application> applications = applicationService.getFilterApplicationsWithPagination(jobId, filterStatus,
				page, size);
		return new ResponseEntity<Page<Application>>(applications, HttpStatus.OK);

	}

	@GetMapping("/getFilterApplicationsWithDateByJobIdWithpagination")
	public ResponseEntity<Page<Application>> getFilterApplicationsWithDateByJobIdWithpagination(@RequestParam int jobId,
			@RequestParam(required = false) String filterStatus,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {

		Page<Application> applications = applicationService.getFilterApplicationsWithDateByJobIdWithpagination(jobId,
				filterStatus, fromDate, toDate, page, size);
		return new ResponseEntity<>(applications, HttpStatus.OK);
	}

	// count of shortlisted candidtes in a company
	@GetMapping("/CountOfShortlistedCandidatesByEachCompany")
	public ResponseEntity<Integer> getCountOfShortlistedCandidateEachCompany(@RequestParam String userEmail) {
		int applications = applicationService.getCountOfShortlistedCandidateByEachCompany(userEmail);
		return new ResponseEntity<>(applications, HttpStatus.OK);
	}

	// count of applications for a company
	@GetMapping("/CountOfApplicationByEachCompany")
	public ResponseEntity<Integer> getCountOfApplicationByEachCompany(@RequestParam String userEmail) {

		return new ResponseEntity<Integer>(applicationService.getCountOfApplicationsByEachCompany(userEmail),
				HttpStatus.OK);
		
	}

	// dream application for a company
	@GetMapping("/getDreamApplicationsByCompany")
	public ResponseEntity<Page<Application>> getDreamApplicationsByCompany(@RequestParam String userEmail,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
		Page<Application> applications = applicationService.getDreamApplicationsByCompany(userEmail, page, size);
		return new ResponseEntity<Page<Application>>(applications, HttpStatus.OK);

	}
	
	// dream application for a company
		@GetMapping("/getDreamApplicationsByCompanyBySkills")
		public ResponseEntity<Page<Application>> getDreamApplicationsByCompanyBySkills(@RequestParam String userEmail,
				@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,@RequestParam(defaultValue = "") String search) {
			Page<Application> applications = applicationService.getDreamApplicationsByCompanyBySkills(userEmail,search, page, size);
			return new ResponseEntity<Page<Application>>(applications, HttpStatus.OK);

		}

	@GetMapping("/countOfDreamApplications")
	public ResponseEntity<Integer> getCountOfDreamApplications(@RequestParam String userEmail) {
		String companyName = userRepository.getCompanyByEmail(userEmail);
		int jobId = 0; // Assuming jobId needs to be retrieved or set appropriately

		int dreamApplications = applicationService.getCountOfDreamApplications(companyName, jobId);
		return new ResponseEntity<>(dreamApplications, HttpStatus.OK);
	}

	// to filter dream applications(underpreview/shortlisted/rejected)
	@GetMapping("/getFilterDreamApplicationsByCompany")
	public ResponseEntity<Page<Application>> getFilterDreamApplicationsByCompany(@RequestParam int jobId,
			@RequestParam String filterStatus, @RequestParam String userEmail,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,@RequestParam(defaultValue = "") String search) {
		Page<Application> applications = applicationService.getFilterDreamApplications(jobId, filterStatus, userEmail,
				page, size,search);
		return new ResponseEntity<>(applications, HttpStatus.OK);
	}

	@GetMapping("/getFilterDreamApplicationsWithDateByCompany")
	public ResponseEntity<Page<Application>> getFilterDreamApplicationsWithDateByCompany(@RequestParam int jobId,
			@RequestParam String userEmail, @RequestParam(required = false) String filterStatus,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,@RequestParam(defaultValue = "") String search) {
		Page<Application> applications = applicationService.getFilterDreamApplicationsWithDateByCompany(jobId,
				userEmail, filterStatus, fromDate, toDate, page, size,search);
		return new ResponseEntity<>(applications, HttpStatus.OK);
	}

	@GetMapping("/applicationsPagination")
	public ResponseEntity<Page<Application>> getPaginationapplication(@RequestParam int userId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int pageSize,
			@RequestParam(required = false) String sortBy, @RequestParam(required = false) String sortOrder,@RequestParam(defaultValue = "all") String filter) {
		Page<Application> applications = null;
		try {
			applications = applicationService.getPaginationApplicationsByCandidateId(userId, page, pageSize, sortBy,
					sortOrder,filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Page<Application>>(applications, HttpStatus.OK);

	}

	// getting application by searching functionality
	@GetMapping("/applicationsBySearch")
	public ResponseEntity<Page<Application>> getApplicationsByStatus(@RequestParam String searchStatus,
			@RequestParam int userId, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int pageSize,@RequestParam(required = false) String sortBy, @RequestParam(required = false) String sortOrder,@RequestParam(defaultValue = "all") String filter) {
		Page<Application> applications = applicationService.getApplicationsByStatus(searchStatus, userId, page,
				pageSize,sortBy,sortOrder,filter);
		return new ResponseEntity<Page<Application>>(applications, HttpStatus.OK);
	}

	@GetMapping("/applicationApplied")
	public ResponseEntity<Boolean> applicationApplied(@RequestParam int jobId, @RequestParam int userId) {
		boolean application = applicationService.getApplicationByCandidateId(jobId, userId);
		return new ResponseEntity<Boolean>(application, HttpStatus.OK);

	}

	@GetMapping("/applicationDreamAplied")
	public ResponseEntity<Boolean> applicationDreamApplied(@RequestParam int userId, @RequestParam String companyName) {
		boolean application = applicationService.getDreamApplicationByCandidateId(userId, companyName);
		return new ResponseEntity<Boolean>(application, HttpStatus.OK);

	}

	@GetMapping("/countByDate")
	public ResponseEntity<List<Map<String, Object>>> getCountByDate(@RequestParam int userId) {
		List<Map<String, Object>> responseData = applicationService.getCountByDate(userId);
		return ResponseEntity.ok(responseData);
	}


	@DeleteMapping("/deleteApplicationByApplicationId")
	public ResponseEntity<Application> deleteApplicationByApplicationId(@RequestParam int applicationId) {
		return new ResponseEntity<Application>(applicationService.deleteApplicationByApplicationId(applicationId),
				HttpStatus.OK);
	}

	
	  @GetMapping("/getEvergreenApplication")
	    public ResponseEntity<Page<Application>> getEvergreenApplication(
	            @RequestParam String email,
	            @RequestParam(required = false) String selectedRole,
	            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int pageSize,
				@RequestParam(required = false) String sortOrder, @RequestParam(required = false) String sortedColumn) {
	        
	        return new ResponseEntity<Page<Application>>( applicationService.getEvergreenApplications(email, selectedRole, page, pageSize, sortOrder, sortedColumn),HttpStatus.OK);
	    }
	  
	 
	  
	  @GetMapping("/checkAppliedCompanies")
		public ResponseEntity<List<String>>  checkAppliedCompanies(@RequestParam int userId,@RequestParam String[] companies,
				@RequestParam String jobRole) {
			List<String> responseData = applicationService.checkAppliedCompanies(userId,companies,jobRole);
			return ResponseEntity.ok(responseData);
		}
	  
	  
	  @GetMapping("/getResumeDetails")
		public ResponseEntity<List<Application>>  getResumeDetails(@RequestParam long resumeId) {
			List<Application> responseData = applicationService.getResumeDetails(resumeId);
			return ResponseEntity.ok(responseData);
		}
	 
	  
	  @GetMapping("/isJobApplied")
		public ResponseEntity<Boolean> isJobApplied(@RequestParam int jobId, @RequestParam int userId) {
			
			return new ResponseEntity<Boolean>(applicationService.isJobApplied(jobId,userId),HttpStatus.OK);
		}
}
























