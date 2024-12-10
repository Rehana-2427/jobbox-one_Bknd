package com.jobbox.Project_Jobbox.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.jobbox.Project_Jobbox.entity.Job;

public interface JobService {

	Job postJob(Job job);

//	List<Job> getAllJobs();

	// Page<Job> getJobsByHrEmail(String userEmail,int page,int size);
	//List<Job> getJobsByJobId(int jobId);

	Page<Job> getJobsByHrEmailEachCompany(String userEmail, boolean status, int page, int size, String sortBy,
			String sortOrder);

	String getCompanyNameById(int jobId);

	int getCountOfJobByHr(String userEmail);

	int getHrIdbyJobId(int jobId);

	String getJobTitleByJobId(int jobId);

	Job getJobByJobId(int jobId);

	Integer getCountJobByEachCompany(String userEmail);

	Page<Job> getJobs(PageRequest pageable);

	Integer getCountJobByCompany(String companyName);

	Page<Job> findJobsByHR(String search, String userEmail, int page, int size);

	Page<Job> findJobsByCompany(String search, String userEmail, int page, int size);

	void deletedJobbyJobId(int jobId);

	Page<Job> getJobsPagination(int page, int size, String sortBy, String sortOrder);

	Page<Job> findJobs(String search, int page, int size, String sortBy, String sortOrder);

	Page<Job> getJobsByHrEmail(String userEmail, boolean status, int page, int size, String sortBy, String sortOrder);

	Map<Integer, Double> getMonthlyJobPercentagesByCompany(String userEmail);

	Page<Job> getJobsFilterPagination(int page, int size, String sortBy, String sortOrder, int userId,
			String filterStatus);

	Integer totalJobsofCompany(String userEmail);

	Page<Job> getJobsByCompany(String companyName, int page, int size, String sortBy, String sortOrder);

	Integer getcountOfTotalJobByCompany(String companyName);

	Page<Job> getJobsPaginationByCompany(String companyName, int page, int size, String sortBy, String sortOrder);

	Page<Job> getJobsByCompany(int companyId, int page, int size, String sortBy, String sortOrder);

	Page<Job> getJobsFromLast7Days(int page, int size);

	Page<Job> getJobsByCompany(int page, int size, String sortBy, String sortOrder, String companyName);

	List<Job> getLatest5JobsByCompany(String companyName);

	Page<Job> searchJobsByCompany(String search, int page, int size, String sortBy, String sortOrder,
			String companyName);

	Page<Job> getEverGreenJobsByCompany(String userEmail, boolean status, int page, int size, String sortBy,
			String sortOrder);

	Page<Job> candiEvergreenJobs(int page, int size, String sortBy, String sortOrder);

	Page<Job> candiRegularJobs(String jobCategory, int page, int size, String sortBy, String sortOrder);

	Page<Job> getCandiEvergreenJobsByFiltering(int page, int size, String sortBy, String sortOrder, int userId,
			String filterStatus);

	Page<Job> getRegularJobsByAllHrsInCompany(String userEmail, boolean b, int page, int size, String sortBy,
			String sortOrder);

	Page<Job> getJobsByHrEmailForApplication(String userEmail, int page, int size, String sortBy, String sortOrder);

	Page<Job> findJobswithfilter(String search, int page, int size, String sortBy, String sortOrder, int userId,
			String filterStatus);

	// Page<Job> findJobs(String search, int page, int size, String sortBy, String sortOrder, int userId, String filter);
}
