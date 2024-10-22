package com.jobbox.Project_Jobbox.serviceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.jobbox.Project_Jobbox.entity.Application;
import com.jobbox.Project_Jobbox.entity.Company;
import com.jobbox.Project_Jobbox.entity.Job;
import com.jobbox.Project_Jobbox.entity.User;
import com.jobbox.Project_Jobbox.repository.ApplicationRepository;
import com.jobbox.Project_Jobbox.repository.CompanyRepository;
import com.jobbox.Project_Jobbox.repository.JobRepository;
import com.jobbox.Project_Jobbox.repository.UserRepository;
import com.jobbox.Project_Jobbox.service.ApplicationService;
import com.jobbox.Project_Jobbox.service.JobService;
import com.jobbox.Project_Jobbox.service.NotificationService;
import com.jobbox.Project_Jobbox.service.UserService;

@Service
public class ApplicationServiceImpl implements ApplicationService {

	Logger logger =LoggerFactory.getLogger(ApplicationServiceImpl.class);


	@Autowired
	public ApplicationRepository applicationRepository;

	@Autowired
	public UserRepository userRepository;

	@Autowired
	public UserService userService;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	public JobService jobService;

	@Autowired
	public CompanyRepository companyRepository;

	@Autowired
	public EmailService emailService;

	@Autowired
	private NotificationService notificationService;

	@Override
	public Application applyJob(int jobId, int userId, Date appliedOn, long resumeId) {

		logger.info("class:: ApplicationServiceImpl -> method applyJob ::{ jobId : "+jobId+" userId : "+userId+" Date : "+appliedOn+" resumeId : "+resumeId+" }");
		Application app = new Application();
		app.setJobId(jobId);
		app.setCompanyName(jobService.getCompanyNameById(jobId));
		app.setCandidateId(userId);
		app.setJobRole(jobService.getJobTitleByJobId(jobId));
		app.setHrId(jobService.getHrIdbyJobId(jobId));
		app.setAppliedOn(appliedOn);
		app.setResumeId(resumeId);
		app.setApplicationStatus("Not Seen");

		sendJobNotification(jobId, app);
		Application savedApp = applicationRepository.save(app);

		// Fetch candidate details for sending email
		User candidate = userRepository.findById(userId).orElse(null);
		if (candidate != null) {
			String toEmail = candidate.getUserEmail();
			String subject = "Job Application Successful";
			String body = "Hello " + candidate.getUserName() + ",\n\n" + "You have successfully applied for the "
					+ app.getJobRole() + " position at " + app.getCompanyName() + ".\n\n" + "Application Details:\n"
					+ "Job Role: " + app.getJobRole() + "\n" + "Company: " + app.getCompanyName() + "\n"
					+ "Applied On: " + appliedOn + "\n\n" + "We wish you all the best.\n\n"
					+ "Thank you for using JobDB.\n\n" + "Note: This is an auto-generated email. Please do not reply.";
			emailService.sendEmail(toEmail, subject, body);
		}
		return savedApp;
	}

	public void sendJobNotification(int jobId, Application app) {
		int hrId = jobRepository.getHrIdbyJobId(jobId);
		String hrEmail = userRepository.getUserEmailById(hrId);
		String message = "You get Application To the Job " + app.getJobRole() + " From "
				+ userRepository.getUserNameById(app.getCandidateId());
		String subject = "Application Recieved";
		emailService.sendEmail(hrEmail, message, subject);

	}

	@Override
	public Application updateApplicationStatus(int applicationId, String newStatus, String hrEmail) {

		logger.info("class:: ApplicationServiceImpl -> method updateApplicationStatus ::{ applicationId : "+applicationId+" hrEmail : "+hrEmail+" newStatus : "+newStatus+" }");
		Application app = applicationRepository.getById(applicationId);
		if (app != null) {
			app.setApplicationStatus(newStatus);
			if (app.getHrId() != 0 && app.getJobId() !=0 && !(jobRepository.getJobCategory(app.getJobId()).equals("evergreen"))) {
				// Save the updated application status
				app = applicationRepository.save(app);
			} else 	if (app.getHrId() != 0 && (newStatus.equals("Not Shortlisted"))|| newStatus.equals("Not Seen")) {
				// Save the updated application status
				if(app.getJobId()==0 || (jobRepository.getJobCategory(app.getJobId()).equals("evergreen"))) {
					app.setHrId(0);
				}
				app = applicationRepository.save(app);
			}
			else if(app.getHrId() == 0 && newStatus.equals("Shortlisted")){

				int hrId = userRepository.getUserIdByEmail(hrEmail);
				System.out.println("hr Email---> "+hrEmail);
				System.out.println("hr Id---> "+hrId);
				app.setHrId(hrId);
				// Save the updated application status
				app = applicationRepository.save(app);
			}else if (app.getHrId() == 0 && (newStatus.equals("Not Shortlisted"))|| newStatus.equals("Not Seen")) {
				// Save the updated application status
				app = applicationRepository.save(app);
			}

			// Fetch candidate details for sending email
			User candidate = userRepository.findById(app.getCandidateId()).orElse(null);
			int jobId = app.getJobId();
			if (candidate != null) {
				String toEmail = candidate.getUserEmail();
				String subject = "Application Status Update";
				String body = "Hello " + candidate.getUserName() + ",\n\n"
						+ "Your application status has been updated:\n\n";
				if (newStatus.equals("Shortlisted")) {
					if (jobId == 0) {
						body += "Congratulations! You have been shortlisted at " + app.getCompanyName() + ".\n\n";
					} else {
						body += "Congratulations! You have been shortlisted for the " + app.getJobRole()
						+ " position at "

								+ app.getCompanyName() + ".\n\n";
					}
					body += "You will soon be contacted by our HR team for further steps in the interview process.\n\n";
					// Create and save notification for shortlisted application
					notificationService.sendShortlistNotification(app);
				} else if (newStatus.equals("Not Shortlisted")) {
					if (jobId == 0) {
						body += "We regret to inform you that you have not been shortlisted  at " + app.getCompanyName()
						+ ".\n\n";

					} else {
						body += "We regret to inform you that you have not been shortlisted for the " + app.getJobRole()
						+ " position at " + app.getCompanyName() + ".\n\n";
					}
					body += "We appreciate your interest in our company. Unfortunately, we had a large number of qualified applicants and had to make a difficult decision.\n\n"
							+ "We encourage you to continue applying for other positions with us in the future.\n\n";
				}

				body += "Thank you for using JobDB.\n\n"
						+ "Note: This is an auto-generated email. Please do not reply.";
				emailService.sendEmail(toEmail, subject, body);
			}

		}
		return app;
	}

	@Override
	public Application getApplication(int applicationId) {
		// TODO Auto-generated method stub
		return applicationRepository.getById(applicationId);
	}

	@Override
	public int getCountOfAppliedCompany(int userId) {

		logger.info("class:: ApplicationServiceImpl -> method getCountOfAppliedCompany ::{ userId : "+userId+" }");
		return applicationRepository.countDistinctCompaniesByUserId(userId);
	}

	@Override
	public int getCountOfShortListedApplications(int userId) {
		// TODO Auto-generated method stub

		logger.info("class:: ApplicationServiceImpl -> method getCountOfShortListedApplications ::{ userId : "+userId+" }");
		String shortList = "Shortlisted";
		return applicationRepository.getCountOfShortlistedApplication(userId, shortList);
	}

	@Override
	public int getCountOfApplicationsCompany(int companyId) {

		logger.info("class:: ApplicationServiceImpl -> method getCountOfApplicationsCompany ::{  companyId : "+ companyId+" }");
		String companyName = companyRepository.getCompanyName(companyId);
		return applicationRepository.getCountOfApplicationsCompany(companyName);
	}

	public int getCountUnderReviewCandidateByHrJob(String userEmail) {

		logger.info("class:: ApplicationServiceImpl -> method getCountUnderReviewCandidateByHrJob ::{ userEmail : "+ userEmail+" }");
		return applicationRepository.getCountOfUnderPreviewCandidates(userRepository.getUserIdByEmail(userEmail),
				"Not Seen");
	}

	@Override
	public int getCountOfApplicationsByEachCompany(String userEmail) {

		logger.info("class:: ApplicationServiceImpl -> method getCountOfApplicationsByEachCompany ::{ userEmail : "+ userEmail+" }");
		String companyName = userService.getCompanyNameById(userService.getuserIdByEmail(userEmail));
		return applicationRepository.getCountOfApplicationsCompany(companyName);

	}

	@Override
	public int getCountOfShortlistedCandidateByEachCompany(String userEmail) {

		logger.info("class:: ApplicationServiceImpl -> method getCountOfShortlistedCandidateByEachCompany ::{ userEmail : "+ userEmail+" }");
		String companyName = userService.getCompanyNameById(userService.getuserIdByEmail(userEmail));
		// Ensure the status matches what is in your database (e.g., "Shortlisted")
		String shortlistStatus = "Shortlisted";
		return applicationRepository.getCountofShortlistedApplicationByEachCompany(companyName, shortlistStatus);
	}

	@Override
	public Page<Application> getDreamApplicationsByCompany(String userEmail, int page, int pageSize) {
		logger.info("class:: ApplicationServiceImpl -> method getDreamApplicationsByCompany ::{ userEmail : "+ userEmail+" }");
		String companyName = userRepository.getCompanyByEmail(userEmail);
		PageRequest pageRequest = PageRequest.of(page, pageSize);
		int jobId = 0;
		return applicationRepository.getApplicationsByCompany(companyName, jobId, pageRequest);

	}

	@Override
	public Page<Application> getFilterDreamApplications(int jobId, String filterStatus, String userEmail, int page,
			int size, String search) {

		logger.info("class:: ApplicationServiceImpl -> method getFilterDreamApplications ::{ userEmail : "+ userEmail+ " jobId : "+jobId+" filterstatus : "+filterStatus+" search : "+search+" }");
		String companyName = userRepository.getCompanyByEmail(userEmail);
		if (!search.equals("")) {
			List<Integer> candidateIds = userRepository.findCandidateIdBasedOnSkills(search);
			Pageable pageable = PageRequest.of(page, size);
			List<Application> applicationsByCompany = new ArrayList<>();
			int totalElements = 0; // Total number of elements, this may require another query to get accurate
			// count
			if (filterStatus.equals("all")) {

				for (Integer candidateId : candidateIds) {
					List<Application> applications = applicationRepository.getApplicationsByCompany(companyName, jobId,
							candidateId);
					applicationsByCompany.addAll(applications);
				}

				// Calculate total number of elements if needed
				totalElements = applicationsByCompany.size(); // Example, should ideally be fetched separately

				// Create a PageImpl with the list, total elements, and pageable
				return new PageImpl<>(applicationsByCompany, pageable, totalElements);
			} else {
				// return applicationRepository.getFilterDreamApplications(jobId, filterStatus,
				// companyName, pageable);
				for (Integer candidateId : candidateIds) {
					List<Application> applications = applicationRepository.getApplicationsByCompany(companyName, jobId,
							candidateId, filterStatus);
					applicationsByCompany.addAll(applications);
				}

				// Calculate total number of elements if needed
				totalElements = applicationsByCompany.size(); // Example, should ideally be fetched separately

				// Create a PageImpl with the list, total elements, and pageable
				return new PageImpl<>(applicationsByCompany, pageable, totalElements);
			}
		} else {
			PageRequest pageRequest = PageRequest.of(page, size);
			if (filterStatus.equals("all"))
				return applicationRepository.getApplicationsByCompany(companyName, jobId, pageRequest);
			else
				return applicationRepository.getFilterDreamApplications(jobId, filterStatus, companyName, pageRequest);
		}

	}

	@Override
	public Page<Application> getFilterDreamApplicationsWithDateByCompany(int jobId, String userEmail,
			String filterStatus, Date fromDate, Date toDate, int page, int size, String search) {


		logger.info("class:: ApplicationServiceImpl -> method getFilterDreamApplicationsWithDateByCompany ::{ userEmail : "+ userEmail+ " jobId : "+jobId+" filterstatus : "+filterStatus+" search : "+search+" fromeDate : "+fromDate+" toDate : "+toDate+" }");
		String companyName = userRepository.getCompanyByEmail(userEmail);
		if (!search.equals("")) {
			List<Integer> candidateIds = userRepository.findCandidateIdBasedOnSkills(search);
			Pageable pageable = PageRequest.of(page, size);
			List<Application> applicationsByCompany = new ArrayList<>();
			int totalElements = 0; // Total number of elements, this may require another query to get accurate
			// count
			jobId = 0;
			if (filterStatus == null || filterStatus.equals("all")) {

				for (Integer candidateId : candidateIds) {
					List<Application> applications = applicationRepository
							.getDreamApplicationsWithDateByCompany(companyName, jobId, fromDate, toDate, candidateId);

					applicationsByCompany.addAll(applications);
				}

				// Calculate total number of elements if needed
				totalElements = applicationsByCompany.size(); // Example, should ideally be fetched separately

				// Create a PageImpl with the list, total elements, and pageable
				return new PageImpl<>(applicationsByCompany, pageable, totalElements);
			} else {

				for (Integer candidateId : candidateIds) {
					List<Application> applications = applicationRepository.getFilterDreamApplicationsWithDateByCompany(
							jobId, filterStatus, companyName, fromDate, toDate, candidateId);

					applicationsByCompany.addAll(applications);
				}

				// Calculate total number of elements if needed
				totalElements = applicationsByCompany.size(); // Example, should ideally be fetched separately

				// Create a PageImpl with the list, total elements, and pageable
				return new PageImpl<>(applicationsByCompany, pageable, totalElements);
			}
		} else {

			PageRequest pageRequest = PageRequest.of(page, size);
			jobId = 0;
			if (filterStatus == null || filterStatus.equals("all")) {
				return applicationRepository.getDreamApplicationsWithDateByCompany(companyName, jobId, fromDate, toDate,
						pageRequest);
			} else {
				return applicationRepository.getFilterDreamApplicationsWithDateByCompany(jobId, filterStatus,
						companyName, fromDate, toDate, pageRequest);
			}
		}

	}

	@Override
	public Application applyDreamCompany(int userId, String companyName,String jobRole, Date appliedOn, long resumeId) {

		logger.info("class:: ApplicationServiceImpl -> method applyDreamCompany ::{ userId : "+ userId+" companyName : "+companyName+" jobRole : "+jobRole+" Date : "+appliedOn+" resumeId : "+resumeId+" }");
		Application app = new Application();
		app.setCandidateId(userId);
		app.setCompanyName(companyName);
		app.setAppliedOn(appliedOn);
		app.setResumeId(resumeId);
		app.setApplicationStatus("Not Seen");
		app.setJobRole(jobRole);

		app = applicationRepository.save(app);
		if (companyRepository.findByName(companyName) == null) {
			Company company = new Company();
			company.setCompanyName(companyName.toUpperCase());
			company.setDate(appliedOn);
			String jobboxEmail = companyName.toLowerCase().replaceAll(" ", "") + "@jobbox.com";
			company.setJobboxEmail(jobboxEmail);
			companyRepository.save(company);
		}

		User candidate = userRepository.findById(userId).orElse(null);

		String toEmail = candidate.getUserEmail();
		String subject = "Dream Application";
		String body = "Hello " + candidate.getUserName() + ",\n\n"
				+ "You have successfully applied job in your dream company : " + app.getCompanyName() + ".\n\n"
				+ "Application Details:\n" + "Company: " + app.getCompanyName() + "\n" + "Applied On: " + appliedOn
				+ "\n\n" + "We wish you all the best.\n\n" + "Thank you for using JobDB.\n\n"
				+ "Note: This is an auto-generated email. Please do not reply.";
		emailService.sendEmail(toEmail, subject, body);

		return app;
	}

	@Override
	public Page<Application> getPaginationApplicationsByCandidateId(int userId, int page, int pageSize, String sortBy,
			String sortOrder, String filter) {

		logger.info("class:: ApplicationServiceImpl -> method getPaginationApplicationsByCandidateId ::{ userId : "+ userId+" }");
		PageRequest pageRequest=null;

		if (sortBy == null || sortBy.isEmpty()) {
			pageRequest = PageRequest.of(page, pageSize); // No sorting
		} else {
			Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
					: Sort.by(sortBy).descending();
			pageRequest = PageRequest.of(page, pageSize, sort);
		}

		return applicationsByCandidate(userId,filter,pageRequest);

	}

	private Page<Application> applicationsByCandidate(int userId, String filter, PageRequest pageRequest) {
	    if (filter.equals("Regular Jobs")) {
	        // Get job IDs that are not evergreen
	        int[] jobIds = jobRepository.findNotEvergreenJobIds();
	        System.out.println(Arrays.toString(jobIds)); 
	        return applicationsByCandidate(jobIds,userId,pageRequest);
	        
	    } else if (filter.equals("Dream Applications")) {
	        return applicationRepository.findDreamJobsApplications(userId, pageRequest);

	    } else if (filter.equals("EverGreen Jobs")) {
	    	 int[] jobIds = jobRepository.findEvergreenJobIds();
		        System.out.println(Arrays.toString(jobIds)); 
		        return applicationsByCandidate(jobIds,userId,pageRequest);

	    }

	    return applicationRepository.findAll(userId, pageRequest);
	}


	private Page<Application> applicationsByCandidate(int[] jobIds, int userId, PageRequest pageRequest) {
		// TODO Auto-generated method stub

        List<Application> applications = new ArrayList<>();
        
        // Retrieve applications for each job ID
        for (int jobId : jobIds) {
            Application application = applicationRepository.getApplicationByJobIdAndCandidateId(jobId, userId);
            if (application != null) {
                applications.add(application);
            }
        }

        // Convert List<Application> to Page<Application>
        int totalApplications = applications.size();
        int start = Math.min(pageRequest.getPageNumber() * pageRequest.getPageSize(), totalApplications);
        int end = Math.min(start + pageRequest.getPageSize(), totalApplications);
        
        List<Application> applicationsPage = applications.subList(start, end);
        return new PageImpl<>(applicationsPage, pageRequest, totalApplications);
	}

	
	
	
	
	
	
	@Override
	public Page<Application> getApplicationsByStatus(String searchStatus, int userId, int page, int pageSize) {
		// TODO Auto-generated method stub
		logger.info("class:: ApplicationServiceImpl -> method getApplicationsByStatus ::{ userId : "+ userId+" searchStatus"+searchStatus+" }");
		PageRequest pageRequest = PageRequest.of(page, pageSize);
		if (searchStatus.equals("Shortlisted"))
			return applicationRepository.getApplicationByApplicationStatus(searchStatus, userId, pageRequest);
		else
			return applicationRepository.getApplicationByStatus(searchStatus, userId, pageRequest);
	}

	@Override
	public Page<Application> getFilterApplicationsWithPagination(int jobId, String filterStatus, int page, int size) {

		logger.info("class:: ApplicationServiceImpl -> method  getFilterApplicationsWithPagination ::{ jobId : "+ jobId+" filterStatus : "+filterStatus +" }");
		PageRequest pageRequest = PageRequest.of(page, size);
		if (filterStatus.equals("all")) {
			return applicationRepository.getApplicationsByJobIdWithPagination(jobId, pageRequest);
		} else if (filterStatus.equals("Shortlisted")) {
			return applicationRepository.getFilterApplicationsWithPagination(jobId, "Shortlisted", pageRequest);
		} else if (filterStatus.equals("Not Seen")) {
			return applicationRepository.getFilterApplicationsWithPagination(jobId, "Not Seen", pageRequest);
		} else if (filterStatus.equals("Not Shortlisted")) {
			return applicationRepository.getFilterApplicationsWithPagination(jobId, "Not Shortlisted", pageRequest);
		} else
			return applicationRepository.getFilterApplicationsWithPagination(jobId, filterStatus, pageRequest);
	}

	@Override
	public Page<Application> getFilterApplicationsWithDateByJobIdWithpagination(int jobId, String filterStatus,
			Date fromDate, Date toDate, int page, int size) {

		logger.info("class:: ApplicationServiceImpl -> method  getFilterApplicationsWithDateByJobIdWithpagination ::{ jobId : "+ jobId+" filterStatus : "+filterStatus +" fromDate : "+fromDate+" toDate : "+toDate+" }");
		PageRequest pageRequest = PageRequest.of(page, size);

		if ((filterStatus != null && fromDate != null && toDate != null)
				|| (filterStatus != null || (fromDate != null && toDate != null))) {
			return applicationRepository.findByJobIdAndApplicationStatusAndAppliedOnBetween(jobId, filterStatus,
					fromDate, toDate, pageRequest);
		} else if (fromDate != null && toDate != null) {
			return applicationRepository.findByJobIdAndAppliedOnBetween(jobId, fromDate, toDate, pageRequest);
		} else {
			return applicationRepository.getApplicationsByJobIdWithPagination(jobId, pageRequest);
		}
	}

	@Override
	public Page<Application> getApplicationsByJobIdWithPagination(int jobId, int page, int size, String sortBy,
			String sortOrder) {

		logger.info("class:: ApplicationServiceImpl -> method  getApplicationsByJobIdWithPagination ::{ jobId : "+ jobId+" }");
		PageRequest pageRequest;
		if (sortBy == null || sortBy.isEmpty()) {
			pageRequest = PageRequest.of(page, size); // No sorting
		} else {
			Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
					: Sort.by(sortBy).descending();
			pageRequest = PageRequest.of(page, size, sort);
		}

		// TODO Auto-generated method stub
		return applicationRepository.getApplicationsByJobIdWithPagination(jobId, pageRequest);
	}

	@Override
	public boolean getApplicationByCandidateId(int jobId, int userId) {
		// TODO Auto-generated method stub

		logger.info("class:: ApplicationServiceImpl -> method  getApplicationsByJobIdWithPagination ::{ jobId : "+ jobId+" userId : "+userId+" }");
		Application applicationByJobIdAndCandidateId = applicationRepository.getApplicationByJobIdAndCandidateId(jobId,
				userId);
		if (applicationByJobIdAndCandidateId != null)
			return true;
		else
			return false;
	}

	@Override
	public List<Map<String, Object>> getCountByDate(int userId) {
		return applicationRepository.getCountByDate(userId);
	}

	@Override
	public int getCountOfDreamApplications(String companyName, int jobId) {

		logger.info("class:: ApplicationServiceImpl -> method  getCountOfDreamApplications ::{ jobId : "+ jobId+" companyName : "+companyName+" }");
		return applicationRepository.getCountOfDreamApplications(companyName, jobId);
	}

	@Override
	public int getCountOfTotalShortlistedApplicationCompany(int userId, String companyName) {
		// TODO Auto-generated method stub
		logger.info("class:: ApplicationServiceImpl -> method  getCountOfTotalShortlistedApplicationCompany ::{ userId : "+ userId+" companyName : "+companyName+" }");
		String shortList = "Shortlisted";
		return applicationRepository.getCountOfTotalShortlistedApplicationCompany(userId, companyName, shortList);
	}

	@Override
	public boolean getDreamApplicationByCandidateId(int userId, String companyName) {

		logger.info("class:: ApplicationServiceImpl -> method  getDreamApplicationByCandidateId ::{ userId : "+ userId+" companyName : "+companyName+" }");
		int jodId = 0;
		Application applicationByJobIdAndCandidateId = applicationRepository
				.getDreampplicationByJobIdAndCandidateId(jodId, userId, companyName);
		if (applicationByJobIdAndCandidateId != null)
			return true;
		else
			return false;
	}

	@Override
	public Application deleteApplicationByApplicationId(int applicationId) {
		// TODO Auto-generated method stub

		logger.info("class:: ApplicationServiceImpl -> method  deleteApplicationByApplicationId ::{ applicationId : "+ applicationId+" }");
		Application application = applicationRepository.getById(applicationId);
		if (application != null) {
			applicationRepository.delete(application);
			return application;
		} else
			return null;
	}

	@Override
	public List<Application> getShortlistedJobs(int candidateId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Application> getDreamApplicationsByCompanyBySkills(String userEmail, String search, int page,
			int size) {
		// TODO Auto-generated method stub

		logger.info("class:: ApplicationServiceImpl -> method  getDreamApplicationsByCompanyBySkills ::{ userEmail : "+ userEmail+" search : "+search+" }");
		String companyName = userRepository.getCompanyByEmail(userEmail);
		List<Integer> candidateIds = userRepository.findCandidateIdBasedOnSkills(search);
		Pageable pageable = PageRequest.of(page, size);
		List<Application> applicationsByCompany = new ArrayList<>();
		int totalElements = 0; // Total number of elements, this may require another query to get accurate
		// count
		int jobId = 0;

		for (Integer candidateId : candidateIds) {
			List<Application> applications = applicationRepository.getDreamApplicationsByCompanyBySkills(companyName,
					jobId, candidateId);

			applicationsByCompany.addAll(applications);
		}

		// Calculate total number of elements if needed
		totalElements = applicationsByCompany.size(); // Example, should ideally be fetched separately

		// Create a PageImpl with the list, total elements, and pageable
		return new PageImpl<>(applicationsByCompany, pageable, totalElements);

	}

	@Override
	public Page<Application> getEvergreenApplications(String email, String selectedRole, int page, int pageSize,
			String sortOrder, String sortedColumn) {
		// TODO Auto-generated method stub

		logger.info("class:: ApplicationServiceImpl -> method getEvergreenApplications ::{ userEmail : "+ email+" selectedRole : "+selectedRole+" }");
		System.out.println("  selectedRole  -------> "+selectedRole);
		Pageable pageable = PageRequest.of(page, pageSize);
		List<Application> evergreenApplicationsByCompany = new ArrayList<>();
		int totalElements = 0; // Total number of elements, this may require another query to get accurate
		// count
		Integer[] jobIds = null;
		if(selectedRole==null || selectedRole.equals("all")==true || selectedRole.isEmpty()) {
			jobIds=jobRepository.getEvergreenJobsIdsbyCompany(userRepository.getCompanyByEmail(email),true);
		}else {
			jobIds=jobRepository.getFilteredEvergreenJobsIdsbyCompany(userRepository.getCompanyByEmail(email),selectedRole,true);
		}
		for(int jobId:jobIds) {
			System.out.println("jobId --> "+jobId);
			List<Application> applications = applicationRepository.getEvergreenApplicationByCompany(jobId);
			evergreenApplicationsByCompany.addAll(applications);
		}
		// Calculate total number of elements if needed
		totalElements = evergreenApplicationsByCompany.size(); // Example, should ideally be fetched separately

		// Create a PageImpl with the list, total elements, and pageable
		return new PageImpl<>(evergreenApplicationsByCompany, pageable, totalElements);	}

}
