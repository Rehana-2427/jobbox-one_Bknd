package com.jobbox.Project_Jobbox.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobbox.Project_Jobbox.entity.Job;
import com.jobbox.Project_Jobbox.service.JobService;

import ch.qos.logback.classic.Logger;

@CrossOrigin(origins = {"http://51.79.18.21:3000", "http://localhost:3000"}) // Include all necessary origins
@Controller
@RequestMapping("/api/jobbox")
@RestController
public class JobController {
	@Autowired
	public JobService jobService;

	@PostMapping("/postingJob")
	public ResponseEntity<Job> postJob(@RequestBody Job job) {
	    System.out.println("Job Category: " + job.getJobCategory()); // Logging jobCategory

		return new ResponseEntity<Job>(jobService.postJob(job), HttpStatus.CREATED);
	}

	@GetMapping("/paginationJobs")
	public ResponseEntity<Page<Job>> getJobsPagination(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String sortOrder) {
		Page<Job> jobsPagination = null;
		try {
			jobsPagination = jobService.getJobsPagination(page, size, sortBy, sortOrder);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<Page<Job>>(jobsPagination, HttpStatus.OK);
	}

	

	@GetMapping("/getJobsPaginationByCompany")
	public ResponseEntity<Page<Job>> getJobsPaginationByCompany(@RequestParam String companyName,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			@RequestParam(required = false) String sortBy, @RequestParam(required = false) String sortOrder) {

		Page<Job> jobsPagination = null;
		try {
			jobsPagination = jobService.getJobsPaginationByCompany(companyName, page, size, sortBy, sortOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(jobsPagination, HttpStatus.OK);
	}

	@PutMapping("/updateJob")
	public ResponseEntity<String> updateJobDetails(@RequestParam int jobId, @RequestBody Job jobDetails) {

	    Job existingJob = jobService.getJobByJobId(jobId);
	    
	    existingJob.setJobTitle(jobDetails.getJobTitle());
	    existingJob.setJobType(jobDetails.getJobType());
	    existingJob.setPostingDate(jobDetails.getPostingDate());
	    existingJob.setSkills(jobDetails.getSkills());
	    existingJob.setApplicationDeadline(jobDetails.getApplicationDeadline());
	    existingJob.setNumberOfPosition(jobDetails.getNumberOfPosition());
	    existingJob.setSalary(jobDetails.getSalary());
	    existingJob.setLocation(jobDetails.getLocation());
	    existingJob.setJobsummary(jobDetails.getJobsummary());
	    existingJob.setJobCategory(jobDetails.getJobCategory());  // Update jobCategory

	    jobService.postJob(existingJob);
	    
	    return new ResponseEntity<String>("Updated Successfully", HttpStatus.OK);
	}


	@GetMapping("/jobsPostedCompany")
	public ResponseEntity<Page<Job>> getJobByCompany(@RequestParam int companyId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			@RequestParam(required = false) String sortBy, @RequestParam(required = false) String sortOrder) {
		Page<Job> jobs = jobService.getJobsByCompany(companyId, page, size, sortBy, sortOrder);
		return new ResponseEntity<Page<Job>>(jobs, HttpStatus.OK);
	}

	//	@GetMapping("/getJobsByjobId")
	//	public ResponseEntity<List<Job>> showJobbyJobId(@RequestParam int jobId) {
	//		List<Job> jobs = jobService.getJobsByJobId(jobId);
	//		return new ResponseEntity<List<Job>>(jobs, HttpStatus.OK);
	//	}

	@GetMapping("/getJob")
	public ResponseEntity<Job> getJob(@RequestParam int jobId) {
		return new ResponseEntity<Job>(jobService.getJobByJobId(jobId), HttpStatus.OK);
	}

	@GetMapping("/CountOfJobsPostedByEachCompany")
	public ResponseEntity<Integer> countJobByEachCompany(@RequestParam String userEmail) {
		return new ResponseEntity<Integer>(jobService.getCountJobByEachCompany(userEmail), HttpStatus.OK);
	}

	@GetMapping("/totalJobsofCompany")
	public ResponseEntity<Integer> totalJobsofCompany(@RequestParam String userEmail) {
		return new ResponseEntity<Integer>(jobService.totalJobsofCompany(userEmail), HttpStatus.OK);
	}

	@GetMapping("/countOfJobsByCompany")
	public ResponseEntity<Integer> getcountJobByCompany(@RequestParam int companyId) {
		return new ResponseEntity<Integer>(jobService.getCountJobByCompany(companyId), HttpStatus.OK);
	}

	@GetMapping("/countOfTotalJobsByCompany")
	public ResponseEntity<Integer> getcountOfTotalJobByCompany(@RequestParam int companyId) {
		return new ResponseEntity<Integer>(jobService.getcountOfTotalJobByCompany(companyId), HttpStatus.OK);
	}

	@GetMapping("/countofjobsbyhr")
	public ResponseEntity<Integer> getCountOfJobsByHrCompany(@RequestParam String userEmail) {
		return new ResponseEntity<Integer>(jobService.getCountOfJobByHr(userEmail), HttpStatus.OK);
	}

	@DeleteMapping("/deleteJob")
	public ResponseEntity<Void> deleteJob(@RequestParam int jobId) {
		jobService.deletedJobbyJobId(jobId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/jobsPostedByHrEmail")
	public ResponseEntity<Page<Job>> showJobbyHrEmail(@RequestParam String userEmail,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			@RequestParam(required = false) String sortBy, @RequestParam(required = false) String sortOrder) {
		Page<Job> jobs = null;
		try {
			jobs = jobService.getJobsByHrEmail(userEmail, true, page, size, sortBy, sortOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<Page<Job>>(jobs, HttpStatus.OK);
	}
	
	
	
	@GetMapping("/jobsByHrEmail")
	public ResponseEntity<Page<Job>> showJobbyHrEmail1(@RequestParam String userEmail,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			@RequestParam(required = false) String sortBy, @RequestParam(required = false) String sortOrder) {
		Page<Job> jobs = null;
		try {
			jobs = jobService.getJobsByHrEmailForApplication(userEmail, page, size, sortBy, sortOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<Page<Job>>(jobs, HttpStatus.OK);
	}

	@GetMapping("/jobsPostedByHrEmaileachCompany")
	public ResponseEntity<Page<Job>> getJobbyHrEmailEchCompany(@RequestParam String userEmail,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			@RequestParam(required = false) String sortBy, @RequestParam(required = false) String sortOrder) {
		Page<Job> jobs = null;
		try {
			jobs = jobService.getJobsByHrEmailEachCompany(userEmail, true, page, size, sortBy, sortOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Page<Job>>(jobs, HttpStatus.OK);
	}
	@GetMapping("/getRegularJobsByAllHrsInCompany")
	public ResponseEntity<Page<Job>> getRegularJobsByAllHrsInCompany(@RequestParam String userEmail,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			@RequestParam(required = false) String sortBy, @RequestParam(required = false) String sortOrder) {
		Page<Job> jobs = null;
		try {
			jobs = jobService.getRegularJobsByAllHrsInCompany(userEmail, true, page, size, sortBy, sortOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Page<Job>>(jobs, HttpStatus.OK);
	}

	
	@GetMapping("/getEverGreenJobsByCompany")
	public ResponseEntity<Page<Job>> getEverGreenJobsByCompany(@RequestParam String userEmail,
	                                                           @RequestParam(defaultValue = "0") int page,
	                                                           @RequestParam(defaultValue = "5") int size,
	                                                           @RequestParam(required = false) String sortBy,
	                                                           @RequestParam(required = false) String sortOrder) {
	    Page<Job> jobs = null;
	    try {
	        jobs = jobService.getEverGreenJobsByCompany(userEmail, true, page, size, sortBy, sortOrder);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return new ResponseEntity<Page<Job>>(jobs, HttpStatus.OK);
	}

	
	@GetMapping("/jobsPostedByCompany")
	public ResponseEntity<Page<Job>> getJobsByCompany(@RequestParam String companyName,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			@RequestParam(required = false) String sortBy, @RequestParam(required = false) String sortOrder) {
		Page<Job> jobs = null;
		try {
			jobs = jobService.getJobsByCompany(companyName, page, size, sortBy, sortOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(jobs, HttpStatus.OK);
	}

	@GetMapping("/searchJobsByHR")
	public ResponseEntity<Page<Job>> searchJobsByHR(@RequestParam String search, @RequestParam String userEmail,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
		Page<Job> jobs = jobService.findJobsByHR(search, userEmail, page, size);
		return new ResponseEntity<Page<Job>>(jobs, HttpStatus.OK);

	}

	@GetMapping("/searchJobsByCompany")
	public ResponseEntity<Page<Job>> searchJobsByCompany(@RequestParam String search, @RequestParam String userEmail,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
		Page<Job> jobs = jobService.findJobsByCompany(search, userEmail, page, size);
		return new ResponseEntity<Page<Job>>(jobs, HttpStatus.OK);

	}

	@GetMapping("/searchJobs")
	public ResponseEntity<Page<Job>> searchJobs(@RequestParam String search, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String sortOrder) {   // ,@RequestParam(defaultValue = "0") int userId,@RequestParam(required = false) String filterStatus
		Page<Job> jobs = jobService.findJobs(search, page, size, sortBy, sortOrder);
//		Page<Job> jobs = jobService.findJobs(search, page, size, sortBy, sortOrder,userId,filterStatus);
		return new ResponseEntity<Page<Job>>(jobs, HttpStatus.OK);
	}

	@GetMapping("/searchJobsInCompany")
	public ResponseEntity<Page<Job>> searchJobsByCompany(@RequestParam String search,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			@RequestParam(required = false) String sortBy, @RequestParam(required = false) String sortOrder,
			@RequestParam String companyName) {

		Page<Job> jobs = jobService.searchJobsByCompany(search, page, size, sortBy, sortOrder, companyName);
		return new ResponseEntity<>(jobs, HttpStatus.OK);
	}

	@GetMapping("/monthlyJobPercentagesByCompany")
	public ResponseEntity<Map<Integer, Double>> getMonthlyJobPercentagesByCompany(@RequestParam String userEmail) {
		Map<Integer, Double> monthlyPercentages = jobService.getMonthlyJobPercentagesByCompany(userEmail);
		return new ResponseEntity<>(monthlyPercentages, HttpStatus.OK);
	}

	@GetMapping("/paginationFilterJobs")
	public ResponseEntity<Page<Job>> getJobsFilterPagination(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String sortOrder, @RequestParam(defaultValue = "0") int userId,
			@RequestParam String filterStatus) {
		Page<Job> jobsPagination = jobService.getJobsFilterPagination(page, size, sortBy, sortOrder, userId,
				filterStatus);

		return new ResponseEntity<Page<Job>>(jobsPagination, HttpStatus.OK);
	}
	
	
	
	@GetMapping("/searchJobsWithFilter")
	public ResponseEntity<Page<Job>> searchJobsWithFilter(
	        @RequestParam String search,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "5") int size,
	        @RequestParam(required = false) String sortBy,
	        @RequestParam(required = false) String sortOrder,
	        @RequestParam(defaultValue = "0") int userId,
	        @RequestParam(required = false, defaultValue = "all") String filterStatus) {

	    Page<Job> jobs = jobService.findJobswithfilter(search, page, size, sortBy, sortOrder, userId, filterStatus);
	    return new ResponseEntity<>(jobs, HttpStatus.OK);
	}

	
	@GetMapping("/getCandiEvergreenJobsByFiltering")
	public ResponseEntity<Page<Job>> getCandiEvergreenJobsByFiltering(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String sortOrder, @RequestParam(defaultValue = "0") int userId,
			@RequestParam String filterStatus) {
		Page<Job> jobsPagination = jobService.getCandiEvergreenJobsByFiltering(page, size, sortBy, sortOrder, userId,
				filterStatus);

		return new ResponseEntity<Page<Job>>(jobsPagination, HttpStatus.OK);
	}

	@GetMapping("/latestJobs")
	public ResponseEntity<Page<Job>> getJobsFromLast7Days(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {
	    Page<Job> jobs = jobService.getJobsFromLast7Days(page, size);
	    return new ResponseEntity<>(jobs, HttpStatus.OK);
	}

	@GetMapping("/getJobsByCompany")
	public ResponseEntity<Page<Job>> getJobsByCompany(@RequestParam(defaultValue = "0", required = false) int page,
			@RequestParam(defaultValue = "5", required = false) int size, @RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String sortOrder, @RequestParam(required = false) String companyName) {

		Page<Job> jobsPagination = null;
		try {
			jobsPagination = jobService.getJobsByCompany(page, size, sortBy, sortOrder, companyName);
			System.out.println(jobsPagination.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(jobsPagination, HttpStatus.OK);
	}

	@GetMapping("/getLatest5JobsByCompany")
	public ResponseEntity<List<Job>> getLatest5JobsByCompany(@RequestParam String companyName) {

		List<Job> jobs = null;
		try {
			jobs = jobService.getLatest5JobsByCompany(companyName);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(jobs, HttpStatus.OK);

	}

}
