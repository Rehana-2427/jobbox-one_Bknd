package com.jobbox.Project_Jobbox.serviceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.jobbox.Project_Jobbox.entity.Job;
import com.jobbox.Project_Jobbox.repository.ApplicationRepository;
import com.jobbox.Project_Jobbox.repository.CompanyRepository;
import com.jobbox.Project_Jobbox.repository.JobRepository;
import com.jobbox.Project_Jobbox.repository.UserRepository;
import com.jobbox.Project_Jobbox.service.JobService;

@Service
public class JobServiceImpl implements JobService {

	Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

	@Autowired
	public JobRepository repository;
	@Autowired
	public UserRepository userRepository;

	@Autowired
	public CompanyRepository companyRepository;

	@Autowired
	public ApplicationRepository applicationRepository;

	@Override
	public Job postJob(Job job) {
		logger.info("class:: JobServicImpl -> method PostJob() :: Job {} " + job);
		String email = job.getUserEmail();
		System.out.println(email);
		String company = userRepository.getCompanyByEmail(email);
		int hrId = userRepository.getUserIdByEmail(email);
		String hrName = userRepository.getHrNameEmail(email);
		job.setCompanyName(company);
		job.setUserId(hrId);
		job.setUserName(hrName);
		// Ensure that job category (temp or evergreen) is passed correctly
		return repository.save(job);

	}

	// @Override
	// public List<Job> getAllJobs() {
	// // TODO Auto-generated method stub
	// boolean status = true;
	// return repository.findAll(status);
	// }
	//
	// @Override
	// public List<Job> getJobsByJobId(int jobId) {
	// // TODO Auto-generated method stub
	// return repository.getJobByJobId(jobId);
	// }

	@Override
	public String getCompanyNameById(int jobId) {
		// TODO Auto-generated method stub
		return repository.getCompamyName(jobId);
	}

	@Override
	public int getHrIdbyJobId(int jobId) {
		// TODO Auto-generated method stub
		return repository.getHrIdbyJobId(jobId);
	}

	@Override
	public String getJobTitleByJobId(int jobId) {

		return repository.getJobTitleByJobId(jobId);
	}

	@Override
	public Job getJobByJobId(int jobId) {

		return repository.getById(jobId);
	}

	@Override
	public Integer getCountJobByEachCompany(String userEmail) {
		String company = userRepository.getCompanyByEmail(userEmail);
		boolean status = true;
		return repository.getCountJobsByEachCompany(company, status);
	}

	@Override
	public Integer totalJobsofCompany(String userEmail) {
		// TODO Auto-generated method stub
		String company = userRepository.getCompanyByEmail(userEmail);
		return repository.totalJobsofCompany(company);

	}

	@Override
	public Map<Integer, Double> getMonthlyJobPercentagesByCompany(String userEmail) {

		logger.info(
				"class:: JobServicImpl -> method  getMonthlyJobPercentagesByCompany() :: userEmail {} " + userEmail);
		String companyName = userRepository.getCompanyByEmail(userEmail);
		List<Object[]> monthlyJobCounts = repository.getCountJobsByEachMonth(companyName);

		// Calculate total jobs for all months
		int totalJobs = monthlyJobCounts.stream().mapToInt(objects -> ((Number) objects[1]).intValue()).sum();

		// Calculate percentages
		Map<Integer, Double> monthlyPercentages = new HashMap<>();
		for (Object[] result : monthlyJobCounts) {
			int month = (int) result[0];
			int jobCount = ((Number) result[1]).intValue();
			double percentage = (jobCount / (double) totalJobs) * 100.0;
			monthlyPercentages.put(month, percentage);
		}

		return monthlyPercentages;
	}

	@Override
	public Page<Job> getJobs(PageRequest pageable) {

		return repository.findAll(pageable);
	}

	@Override
	public Integer getCountJobByCompany(String companyName) {
		boolean status = true;
		return repository.getCountJobsByEachCompany(companyName, status);
	}

	@Override
	public Integer getcountOfTotalJobByCompany(String companyName) {
		// TODO Auto-generated method stub
		return repository.getcountOfTotalJobByCompany(companyName);

	}

	@Override
	public Page<Job> findJobsByHR(String search, String userEmail, int page, int size) {
		// TODO Auto-generated method stub
		PageRequest pageRequest = PageRequest.of(page, size);
		boolean status = true;
		return repository.findjobsByHR(search, userEmail, status, pageRequest);
	}

	@Override
	public Page<Job> findJobsByCompany(String search, String userEmail, int page, int size) {
		String company = userRepository.getCompanyByEmail(userEmail);
		PageRequest pageRequest = PageRequest.of(page, size);
		boolean status = true;
		return repository.findjobsByCompany(search, company, status, pageRequest);
	}

	@Override
	public int getCountOfJobByHr(String userEmail) {
		// TODO Auto-generated method stu
		int userId = userRepository.getUserIdByEmail(userEmail);
		return repository.getCountOfJobsByHr(userId);
	}

	@Override
	public void deletedJobbyJobId(int jobId) {
		// TODO Auto-generated method stub

		repository.deleteJobByJobId(jobId);

	}

	@Override
	public Page<Job> getJobsPagination(int page, int size, String sortBy, String sortOrder) {

		logger.info("class:: JobServicImpl -> method  getJobsPagination() :: Candidate {} ");
		PageRequest pageRequest;

		if (sortBy == null || sortBy.isEmpty()) {
			pageRequest = PageRequest.of(page, size); // No sorting
		} else {
			Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
					: Sort.by(sortBy).descending();
			pageRequest = PageRequest.of(page, size, sort);
		}
		boolean status = true;
		return repository.findAll(pageRequest, status);
	}

	@Override
	public Page<Job> findJobs(String search, int page, int size, String sortBy, String sortOrder) {

		logger.info("class:: JobServicImpl -> method  findJobs() :: search {} " + search);
		PageRequest pageRequest;
		if (sortBy == null || sortBy.isEmpty()) {
			pageRequest = PageRequest.of(page, size); // No sorting
		} else {
			Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
					: Sort.by(sortBy).descending();
			pageRequest = PageRequest.of(page, size, sort);
		}
		return repository.findjobs(search, pageRequest);

	}

	@Override
	public Page<Job> getJobsByHrEmail(String userEmail, boolean status, int page, int size, String sortBy,
			String sortOrder) {

		logger.info("class:: JobServicImpl -> method  getJobsByHrEmail() :: userEmail {} " + userEmail);
		PageRequest pageRequest;
		if (sortBy == null || sortBy.isEmpty()) {
			pageRequest = PageRequest.of(page, size); // No sorting
		} else {
			Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
					: Sort.by(sortBy).descending();
			pageRequest = PageRequest.of(page, size, sort);
		}
		return repository.getJobsByHrEmailAndStatus(userEmail, status, pageRequest);

	}

	@Override
	public Page<Job> getJobsByHrEmailEachCompany(String userEmail, boolean status, int page, int size, String sortBy,
			String sortOrder) {

		logger.info("class:: JobServicImpl -> method  getJobsByHrEmailEachCompany() :: userEmail {} " + userEmail);
		PageRequest pageRequest;
		String company = userRepository.getCompanyByEmail(userEmail);
		if (sortBy == null || sortBy.isEmpty()) {
			pageRequest = PageRequest.of(page, size); // No sorting
		} else {
			Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
					: Sort.by(sortBy).descending();
			pageRequest = PageRequest.of(page, size, sort);
		}

		return repository.getJobsByCompany(company, status, pageRequest);
	}

	@Override
	public Page<Job> getJobsByCompany(String companyName, int page, int size, String sortedColumn, String sortOrder) {

		logger.info("class:: JobServicImpl -> method  getJobsByCompany() :: companyName {} " + companyName);
		PageRequest pageRequest;
		if (sortedColumn == null || sortedColumn.isEmpty()) {

			pageRequest = PageRequest.of(page, size); // No sorting
		} else {
			Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortedColumn).ascending()
					: Sort.by(sortedColumn).descending();
			pageRequest = PageRequest.of(page, size, sort);
		}

		return repository.getJobsByCompany(companyName, true, pageRequest);
	}

	@Override
	public Page<Job> getJobsFilterPagination(int page, int size, String sortBy, String sortOrder, int userId,
			String filterStatus) {
		logger.info("class:: JobServicImpl -> method getJobsFilterPagination() :: userId {} " + userId);
		System.out.println("user Id ++++++ " + userId);
		PageRequest pageRequest = null;
		if (sortBy == null || sortBy.isEmpty()) {
			pageRequest = PageRequest.of(page, size); // No sorting
		} else {
			Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
					: Sort.by(sortBy).descending();
			pageRequest = PageRequest.of(page, size, sort);
		}
		List<Job> jobs = new ArrayList<>();
		List<Integer> jobIdsByUserId = null;
		if (filterStatus.equals("all")) {
			Page<Job> allJobs = repository.findAllJobs(true, pageRequest);
			System.out.println("Fetch all jobs   +++");
			return allJobs;
		} else if (filterStatus.equals("Apply")) {
			System.out.println("helloo Apply  ++++> " + filterStatus);
			jobIdsByUserId = applicationRepository.getJobIdsByUserId(userId);
			for (int jobId : jobIdsByUserId) {
				System.out.println("jobId     ---> " + jobId);
			}

			Page<Job> jobsPage = repository.findJobsNotAssociatedWithUser(jobIdsByUserId, true, pageRequest);
			return jobsPage;

		} else {
			System.out.println("helloo  applied ++++> " + filterStatus);
			jobIdsByUserId = applicationRepository.getJobIdsByUserId(userId);
			for (int jobId : jobIdsByUserId) {
				System.out.println("jobId     ---> " + jobId);
			}
			for (Integer jobId : jobIdsByUserId) {
				Job job = repository.getJobByJobId(jobId, true);
				System.out.println(job);
				if (job != null) {
					jobs.add(job);

				}
			}
		}
		// Iterate through each jobId and fetch corresponding Job

		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()), jobs.size());

		Page<Job> jobPage = new PageImpl<>(jobs.subList(start, end), pageRequest, jobs.size());
		return jobPage;

	}

	@Override
	public Page<Job> getJobsPaginationByCompany(String companyName, int page, int size, String sortBy,
			String sortOrder) {
		logger.info("class:: JobServicImpl -> method getJobsPaginationByCompany() :: companyName{} " + companyName);
		PageRequest pageRequest;

		if (sortBy == null || sortBy.isEmpty()) {
			pageRequest = PageRequest.of(page, size); // No sorting
		} else {
			Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
					: Sort.by(sortBy).descending();
			pageRequest = PageRequest.of(page, size, sort);
		}

		boolean status = true; // Assuming you're still filtering based on job status
		return repository.findByCompanyNameAndStatus(companyName, status, pageRequest);
	}

	@Override
	public Page<Job> getJobsByCompany(int companyId, int page, int size, String sortBy, String sortOrder) {

		logger.info("class:: JobServicImpl -> method getJobsByCompany() :: companyId{} " + companyId);
		String company = companyRepository.getCompanyName(companyId);
		System.out.println(company);
		PageRequest pageRequest = null;
		if (sortBy == null || sortBy.isEmpty()) {
			pageRequest = PageRequest.of(page, size); // No sorting
		} else {
			Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
					: Sort.by(sortBy).descending();
			pageRequest = PageRequest.of(page, size, sort);
		}

		return repository.findAllByStatusAndCompanyName(pageRequest, true, company);
	}

	@Override
	public Page<Job> getJobsFromLast7Days(int page, int size) {
		logger.info("class:: JobServiceImpl -> method getJobsFromLast7Days() :: latest Job posted in last 7 days");
		PageRequest pageRequest = PageRequest.of(0, 10); // No sorting, only pagination

		// Calendar calendar = Calendar.getInstance();
		// calendar.add(Calendar.DAY_OF_YEAR, -7); // Jobs from the last 7 days
		// Date startDate = calendar.getTime();
		boolean status = true; // Active jobs

		// repository.findJobsFromLast7Days(null, status, pageRequest)
		return repository.findLatestJobs(status, pageRequest);
	}

	@Override
	public Page<Job> getJobsByCompany(int page, int size, String sortBy, String sortOrder, String companyName) {
		// TODO Auto-generated method stub

		logger.info("class:: JobServicImpl -> method  getJobsByCompany() :: compamyName :  " + companyName);
		PageRequest pageRequest;

		if (sortBy == null || sortBy.isEmpty()) {
			pageRequest = PageRequest.of(page, size); // No sorting
		} else {
			Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
					: Sort.by(sortBy).descending();
			pageRequest = PageRequest.of(page, size, sort);
		}

		boolean status = true;
		return repository.findAllByStatusAndCompanyName(pageRequest, status, companyName);
	}

	@Override
	public List<Job> getLatest5JobsByCompany(String companyName) {
		logger.info("class:: JobServicImpl -> method  getLatest5JobsByCompany() :: compamyName :  " + companyName);
		boolean status = true; // Assuming you want to filter by active jobs
		Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("postingDate")));
		return repository.findLatest5JobsByCompany(status, companyName, pageable);
	}

	@Override
	public Page<Job> searchJobsByCompany(String search, int page, int size, String sortBy, String sortOrder,
			String companyName) {

		logger.info("class:: JobServicImpl -> method  searchJobsByCompany() :: compamyName :  " + companyName);

		PageRequest pageRequest;
		if (sortBy == null || sortBy.isEmpty()) {
			pageRequest = PageRequest.of(page, size); // No sorting
		} else {
			Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
					: Sort.by(sortBy).descending();
			pageRequest = PageRequest.of(page, size, sort);
		}

		return repository.findJobsInCompany(search, companyName, pageRequest);
	}

	@Override
	public Page<Job> getEverGreenJobsByCompany(String userEmail, boolean status, int page, int size, String sortBy,
			String sortOrder) {

		logger.info("class:: JobServicImpl -> method  getEverGreenJobsByCompany() :: userEmail :  " + userEmail);

		PageRequest pageRequest;
		String company = userRepository.getCompanyByEmail(userEmail);
		if (sortBy == null || sortBy.isEmpty()) {
			pageRequest = PageRequest.of(page, size); // No sorting
		} else {
			Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
					: Sort.by(sortBy).descending();
			pageRequest = PageRequest.of(page, size, sort);
		}

		return repository.getEverGreenJobsByCompany(company, status, pageRequest);
	}

	@Override
	public Page<Job> candiEvergreenJobs(int page, int size, String sortBy, String sortOrder) {

		logger.info("class:: JobServicImpl -> method candiEvergreenJobs() :: user : Candidate  ");
		PageRequest pageRequest;

		if (sortBy == null || sortBy.isEmpty()) {
			pageRequest = PageRequest.of(page, size); // No sorting
		} else {
			Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
					: Sort.by(sortBy).descending();
			pageRequest = PageRequest.of(page, size, sort);
		}
		boolean status = true;
		return repository.candiEvergreenJobs(pageRequest, status);
	}

	@Override
	public Page<Job> candiRegularJobs(String jobCategory, int page, int size, String sortBy, String sortOrder) {

		logger.info("class:: JobServicImpl -> method candiRegularJobs() :: jobCategory :  " + jobCategory);
		PageRequest pageRequest;

		if (sortBy == null || sortBy.isEmpty()) {
			pageRequest = PageRequest.of(page, size); // No sorting
		} else {
			Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
					: Sort.by(sortBy).descending();
			pageRequest = PageRequest.of(page, size, sort);
		}
		boolean status = true;
		return repository.candiRegularJobs(pageRequest, status, jobCategory);
	}

	@Override
	public Page<Job> getCandiEvergreenJobsByFiltering(int page, int size, String sortBy, String sortOrder, int userId,
			String filterStatus) {
		logger.info("class:: JobServicImpl -> method getCandiEvergreenJobsByFiltering() :: userId :  " + userId
				+ " status : " + filterStatus);
		System.out.println("user Id ++++++ " + userId);
		PageRequest pageRequest = null;
		if (sortBy == null || sortBy.isEmpty()) {
			pageRequest = PageRequest.of(page, size); // No sorting
		} else {
			Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
					: Sort.by(sortBy).descending();
			pageRequest = PageRequest.of(page, size, sort);
		}
		List<Job> jobs = new ArrayList<>();
		List<Integer> jobIdsByUserId = null;
		if (filterStatus.equals("all")) {
			Page<Job> allJobs = repository.findEvergreenAllJobs(true, pageRequest);
			System.out.println("Fetch all jobs   +++");
			return allJobs;
		} else if (filterStatus.equals("Apply")) {
			System.out.println("helloo Apply  ++++> " + filterStatus);
			jobIdsByUserId = applicationRepository.getJobIdsByUserId(userId);
			for (int jobId : jobIdsByUserId) {
				System.out.println("jobId     ---> " + jobId);
			}

			jobs = repository.findEvergreenJobsNotAssociatedWithUser(jobIdsByUserId, true);

		} else {
			System.out.println("helloo  applied ++++> " + filterStatus);
			jobIdsByUserId = applicationRepository.getJobIdsByUserId(userId);
			for (int jobId : jobIdsByUserId) {
				System.out.println("jobId     ---> " + jobId);
			}
			for (Integer jobId : jobIdsByUserId) {
				Job job = repository.getEvergreenJobByJobId(jobId, true);
				System.out.println(job);
				if (job != null) {
					jobs.add(job);
				}
			}
		}

		// Iterate through each jobId and fetch corresponding Job

		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()), jobs.size());

		Page<Job> jobPage = new PageImpl<>(jobs.subList(start, end), pageRequest, jobs.size());
		return jobPage;
	}

	@Override
	public Page<Job> getRegularJobsByAllHrsInCompany(String userEmail, boolean status, int page, int size,
			String sortBy, String sortOrder) {

		logger.info("class:: JobServicImpl -> method getRegularJobsByAllHrsInCompany() :: userEmail :   " + userEmail);
		PageRequest pageRequest;
		String company = userRepository.getCompanyByEmail(userEmail);
		if (sortBy == null || sortBy.isEmpty()) {
			pageRequest = PageRequest.of(page, size); // No sorting
		} else {
			Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
					: Sort.by(sortBy).descending();
			pageRequest = PageRequest.of(page, size, sort);
		}

		return repository.getRegularJobsByAllHrsInCompany(company, status, pageRequest);
	}

	@Override
	public Page<Job> getJobsByHrEmailForApplication(String userEmail, int page, int size, String sortBy,
			String sortOrder) {
		// TODO Auto-generated method stub
		logger.info("class:: JobServicImpl -> method  getJobsByHrEmailForApplication() :: userEmail {} " + userEmail);
		PageRequest pageRequest;
		if (sortBy == null || sortBy.isEmpty()) {
			pageRequest = PageRequest.of(page, size); // No sorting
		} else {
			Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
					: Sort.by(sortBy).descending();
			pageRequest = PageRequest.of(page, size, sort);
		}
		return repository.getJobsByHrEmailApplication(userEmail, pageRequest);
	}

	@Override
	public Page<Job> findJobswithfilter(String search, int page, int size, String sortBy, String sortOrder, int userId,
			String filterStatus) {
		logger.info("class:: JobServicImpl -> method  findJobs() :: search {}", search);

		PageRequest pageRequest;
		if (sortBy == null || sortBy.isEmpty()) {
			pageRequest = PageRequest.of(page, size); // No sorting
		} else {
			Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
					: Sort.by(sortBy).descending();
			pageRequest = PageRequest.of(page, size, sort);
		}

		if ("all".equalsIgnoreCase(filterStatus)) {
			return repository.searchAllJobs(search, true, pageRequest);
		} else if ("Apply".equalsIgnoreCase(filterStatus)) {
			List<Integer> jobIdsByUserId = applicationRepository.getJobIdsByUserId(userId);
			return repository.searchJobsNotAssociatedWithUser(search, jobIdsByUserId, true, pageRequest);
		} else if ("Applied".equalsIgnoreCase(filterStatus)) {
			List<Integer> jobIdsByUserId = applicationRepository.getJobIdsByUserId(userId);

			List<Job> jobs = jobIdsByUserId.stream()
					.map(jobId -> repository.getJobByJobIdAndSearch(jobId, search, true)).filter(Objects::nonNull)
					.collect(Collectors.toList());

			int start = (int) pageRequest.getOffset();
			int end = Math.min((start + pageRequest.getPageSize()), jobs.size());
			return new PageImpl<>(jobs.subList(start, end), pageRequest, jobs.size());
		}

		return Page.empty();
	}

//	@Override
//	public Page<Job> findJobs(String search, int page, int size, String sortBy, String sortOrder, int userId,
//			String filter) {
//		// TODO Auto-generated method stub
//		logger.info("class:: JobServicImpl -> method  findJobs() :: search {} "+search);
//		PageRequest pageRequest;
//		if (sortBy == null || sortBy.isEmpty()) {
//			pageRequest = PageRequest.of(page, size); // No sorting
//		} else {
//			Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
//					: Sort.by(sortBy).descending();
//			pageRequest = PageRequest.of(page, size, sort);
//		}
//		if(filter ==null) {
//			return repository.findjobs(search, pageRequest);
//		}
//		else if(filter.equals("Apply")) {
//			System.out.println(userId);
//			return findApplyJobs(search,userId,pageRequest);
//		}else if(filter.equals("Applied")){
//			return findAppliedJobs(search,userId,pageRequest);
//		}
//		
//		return repository.findjobs(search, pageRequest);
//	}
//
//	private Page<Job> findAppliedJobs(String search, int userId, PageRequest pageRequest) {
//		// TODO Auto-generated method stub
//		List<Integer> jobIds=applicationRepository.getJobIdsByUserId(userId);
//		Page<Job> jobs=repository.findEverAppliedWithUser(jobIds, true,search,pageRequest);
//		return jobs;
//	}
//
//	private Page<Job> findApplyJobs(String search, int userId, PageRequest pageRequest) {
//		// TODO Auto-generated method stub
//		List<Integer> jobIds=applicationRepository.getJobIdsByUserId(userId);
//		Page<Job> jobs=repository.findEverApplyWithUser(jobIds, true,search,pageRequest);
//		return jobs;
//	}

}
