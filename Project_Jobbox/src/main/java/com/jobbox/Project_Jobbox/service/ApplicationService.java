package com.jobbox.Project_Jobbox.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.jobbox.Project_Jobbox.entity.Application;

public interface ApplicationService {

	Application updateApplicationStatus(int applicationId, String newStatus, String hrEmail);

	Application getApplication(int applicationId);

	int getCountOfAppliedCompany(int userId);

	int getCountOfShortListedApplications(int userId);

	Application applyJob(int jobId, int userId, Date appliedOn, long resumeId);

	int getCountOfApplicationsByEachCompany(String userEmail);

	int getCountOfApplicationsCompany(String comapnyName);

	int getCountOfShortlistedCandidateByEachCompany(String companyName);

	int getCountUnderReviewCandidateByHrJob(String userEmail);

	Page<Application> getDreamApplicationsByCompany(String userEmail, int page, int pageSize);

	Page<Application> getFilterDreamApplications(int jobId, String filterStatus, String userEmail, int page, int size, String search);

	Application applyDreamCompany(int userId, String companyName, String jobRole, Date appliedOn, long resumeId);

	Page<Application> getPaginationApplicationsByCandidateId(int userId, int page, int pageSize, String sortBy,
			String sortOrder, String filter);

	Page<Application> getApplicationsByStatus(String searchStatus, int userId, int page, int pageSize, String sortBy, String sortOrder, String filter);

	Page<Application> getFilterApplicationsWithPagination(int jobId, String filterStatus, int page, int size);

	Page<Application> getApplicationsByJobIdWithPagination(int jobId, int page, int size, String sortBy,
			String sortOrder);

	boolean getApplicationByCandidateId(int jobId, int userId);

	List<Map<String, Object>> getCountByDate(int userId);

	Page<Application> getFilterApplicationsWithDateByJobIdWithpagination(int jobId, String filterStatus, Date fromDate,
			Date toDate, int page, int size);

	Page<Application> getFilterDreamApplicationsWithDateByCompany(int jobId, String userEmail, String filterStatus,
			Date fromDate, Date toDate, int page, int size, String search);

	int getCountOfDreamApplications(String companyName, int jobId);

	int getCountOfTotalShortlistedApplicationCompany(int userId, String companyName);

	boolean getDreamApplicationByCandidateId(int jobId, String companyName);

	Application deleteApplicationByApplicationId(int applicationId);

	List<Application> getShortlistedJobs(int candidateId);

	Page<Application> getDreamApplicationsByCompanyBySkills(String userEmail, String search, int page, int size);

	Page<Application> getEvergreenApplications(String email, String selectedRole, int page, int pageSize, String sortOrder,
			String sortedColumn);

	List<String> checkAppliedCompanies(int userId, String[] companies, String jobRole);

	List<Application> getResumeDetails(long resumeId);
}
