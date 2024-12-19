package com.jobbox.Project_Jobbox.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jobbox.Project_Jobbox.entity.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {

	@Query("select job from Job job where job.jobStatus=?1")
	Page<Job> findAll(Pageable pageable, boolean jobStatus);

	@Query("SELECT job FROM Job job WHERE job.jobStatus = ?1 AND job.companyName = ?2 ORDER BY job.postingDate DESC")
	Page<Job> findAllByStatusAndCompanyName(Pageable pageable, boolean jobStatus, String companyName);

	@Query("select job.companyName from Job job where job.userName=?1")
	String getCompamyName(String userName);

	@Query("SELECT job FROM Job job WHERE job.userEmail = ?1 AND job.jobStatus = ?2")
	Page<Job> getJobsByHrEmailAndStatus(String userEmail, boolean status, PageRequest pageRequest);

	@Query("SELECT job FROM Job job WHERE job.userEmail = ?1") //   AND job.jobCategory NOT LIKE 'evergreen%'
	Page<Job>getJobsByHrEmailApplication(String userEmail, PageRequest pageRequest);
	
	@Query("select job from Job job where job.companyName=?1  AND job.jobStatus=?2 ORDER BY job.postingDate DESC")
	Page<Job> getJobsByCompany(String companyName, boolean status, PageRequest pageRequest);

	@Query("select job from Job job where job.jobStatus = ?1")
	List<Job> findAll(boolean jobStatus);

	@Query("select job from Job job where job.jobId=?1")
	Job getJobByJobId(int jobId);

	@Query("select job.companyName from Job job where job.jobId=?1")
	String getCompamyName(int jobId);

	@Query("select job.userId from Job job where job.jobId=?1")
	int getHrIdbyJobId(int jobId);

	@Query("select job.jobTitle from Job job where job.jobId=?1")
	String getJobTitleByJobId(int jobId);

	@Query("SELECT COUNT(job.jobId) FROM Job job WHERE job.companyName = ?1 and job.jobStatus = ?2 ")
	Integer getCountJobsByEachCompany(String company, boolean jobStatus);

	@Query("SELECT COUNT(job.jobId) from Job job where job.userId=?1")
	int getCountOfJobsByHr(int userId);

	@Query("SELECT job FROM Job job WHERE (job.jobTitle LIKE %?1% OR job.companyName LIKE %?1% OR job.skills LIKE %?1% OR job.jobType LIKE %?1%) AND job.userEmail = ?2 and job.jobStatus = ?3")
	Page<Job> findjobsByHR(String search, String userEmail, boolean jobStatus, PageRequest pageRequest);

	@Query("SELECT job FROM Job job WHERE (job.jobTitle LIKE %?1% OR job.userName LIKE %?1% OR job.companyName LIKE %?1% OR job.skills LIKE %?1% OR job.jobType LIKE %?1%) AND job.companyName = ?2 and job.jobStatus = ?3")
	Page<Job> findjobsByCompany(String search, String company, boolean jobStatus, PageRequest pageRequest);

	@Modifying
	@Transactional
	@Query("update Job job set job.jobStatus = false where job.jobId = ?1")
	void deleteJobByJobId(int jobId);

	@Query("SELECT r.fileName FROM Resume r WHERE r.id = ?1")
	String getBriefResume(long resumeId);

	@Query("SELECT job FROM Job job WHERE job.jobStatus = ?1")
	Page<Job> findAllByJobStatus(Pageable pageable, boolean jobStatus);

	@Query("SELECT job FROM Job job WHERE job.jobTitle LIKE %?1% OR job.companyName LIKE %?1% OR job.skills LIKE %?1% OR job.jobType LIKE %?1% OR job.location LIKE %?1%")
	Page<Job> findjobs(String search, PageRequest pageRequest);

	@Query("SELECT MONTH(job.postingDate) as month, COUNT(job.jobId) as jobCount " + "FROM Job job "
			+ "WHERE job.companyName = ?1 " + "GROUP BY MONTH(job.postingDate)")
	List<Object[]> getCountJobsByEachMonth(String company);

	@Query("SELECT j FROM Job j WHERE j.jobId NOT IN :userJobIds AND j.jobStatus = :isActive")
	Page<Job> findJobsNotAssociatedWithUser(@Param("userJobIds") List<Integer> userJobIds,
			@Param("isActive") Boolean isActive, PageRequest pageRequest);

	@Query("select job from Job job where job.jobId=?1 AND job.jobStatus=?2")
	Job getJobByJobId(int jobId, boolean b);

	@Query("SELECT job FROM Job job WHERE job.jobStatus = ?1 ")
	Page<Job> findAllJobs(boolean b, PageRequest pageRequest);

	@Query("SELECT COUNT(job.jobId) FROM Job job WHERE job.companyName = ?1")
	Integer totalJobsofCompany(String company);

	@Query("SELECT COUNT(job.jobId) FROM Job job WHERE job.companyName = ?1 ")
	Integer getcountOfTotalJobByCompany(String companyName);

	@Query("SELECT job FROM Job job WHERE job.companyName = ?1 AND job.jobStatus = ?2")
	Page<Job> findByCompanyNameAndStatus(String companyName, boolean status, PageRequest pageRequest);

	@Query("SELECT j FROM Job j WHERE j.postingDate >= :startDate AND j.jobStatus = :status ORDER BY j.postingDate DESC")
	Page<Job> findJobsFromLast7Days(@Param("startDate") Date startDate, @Param("status") boolean status, PageRequest pageRequest);

	@Query("SELECT job FROM Job job WHERE job.jobStatus = ?1 AND job.companyName = ?2 ORDER BY job.postingDate DESC")
	List<Job> findLatest5JobsByCompany(boolean jobStatus, String companyName, Pageable pageable);


	@Query("SELECT job FROM Job job WHERE (job.jobTitle LIKE %?1% OR job.skills LIKE %?1% OR job.jobType LIKE %?1% OR job.location LIKE %?1%) AND job.companyName LIKE %?2% ORDER BY job.postingDate DESC")
	Page<Job> findJobsInCompany(String search, String companyName, PageRequest pageRequest);

	@Query("SELECT job FROM Job job WHERE job.companyName = ?1 AND job.jobStatus = ?2 AND job.jobCategory = 'evergreen'")
	Page<Job> getEverGreenJobsByCompany(String company, boolean status, PageRequest pageRequest);

	@Query("select job from Job job where job.jobStatus=?1 AND job.jobCategory = 'evergreen'")
	Page<Job> candiEvergreenJobs(PageRequest pageRequest, boolean status);

	@Query("select job from Job job where job.jobStatus=?1 AND job.jobCategory = ?2")
	Page<Job> candiRegularJobs(PageRequest pageRequest, boolean status, String jobCategory);

	@Query("SELECT job FROM Job job WHERE job.jobStatus = ?1  AND job.jobCategory = 'evergreen'")
	Page<Job> findEvergreenAllJobs(boolean b, PageRequest pageRequest);

	@Query("SELECT j FROM Job j WHERE j.jobId NOT IN :userJobIds AND j.jobStatus = :isActive AND j.jobCategory = 'evergreen'")
	List<Job> findEvergreenJobsNotAssociatedWithUser(@Param("userJobIds") List<Integer> userJobIds,
			@Param("isActive") Boolean isActive);

	@Query("select job from Job job where job.jobId=?1 AND job.jobStatus=?2 AND job.jobCategory = 'evergreen'")
	Job getEvergreenJobByJobId(int jobId, boolean b);

	@Query("select job.jobId from Job job where job.companyName=?1 AND job.jobStatus=?2 AND job.jobCategory = 'evergreen'")
	Integer[] getEvergreenJobsIdsbyCompany(String companyByEmail, boolean status);

	@Query("select job.jobCategory from Job job where job.jobId=?1 ")
	String getJobCategory(int jobId);

	@Query("select job.jobId from Job job where job.companyName=?1 AND job.jobTitle=?2 AND job.jobStatus=?3 AND job.jobCategory = 'evergreen'")
	Integer[] getFilteredEvergreenJobsIdsbyCompany(String companyByEmail, String selectedRole, boolean b);

	@Query("SELECT job FROM Job job WHERE job.companyName = ?1 AND job.jobStatus = ?2 AND (job.jobCategory IS NULL OR job.jobCategory NOT LIKE 'evergreen%')")
	Page<Job> getRegularJobsByAllHrsInCompany(String company, boolean status, PageRequest pageRequest);

	@Query("SELECT job.jobId FROM Job job WHERE job.jobCategory IS NULL OR job.jobCategory NOT LIKE 'evergreen%'")
	int[] findNotEvergreenJobIds();

	@Query("SELECT job.jobId FROM Job job WHERE job.jobCategory = 'evergreen'")
	int[] findEvergreenJobIds();

	@Query("SELECT job FROM Job job WHERE  job.jobStatus=?1 ORDER BY job.postingDate DESC")
	Page<Job> findLatestJobs(boolean status, PageRequest pageRequest);

	
	@Query("SELECT job FROM Job job WHERE (job.jobTitle LIKE %?1% OR job.companyName LIKE %?1% OR job.skills LIKE %?1% OR job.jobType LIKE %?1% OR job.location LIKE %?1%) AND job.jobStatus = ?2")
	Page<Job> searchAllJobs(String search, boolean status, PageRequest pageRequest);

	@Query("SELECT job FROM Job job WHERE (job.jobTitle LIKE %:search% OR job.companyName LIKE %:search% OR job.skills LIKE %:search% OR job.jobType LIKE %:search% OR job.location LIKE %:search%) AND job.jobId NOT IN :userJobIds AND job.jobStatus = :isActive")
	Page<Job> searchJobsNotAssociatedWithUser(@Param("search") String search, @Param("userJobIds") List<Integer> userJobIds, @Param("isActive") boolean isActive, PageRequest pageRequest);

	@Query("SELECT job FROM Job job WHERE job.jobId = :jobId AND (job.jobTitle LIKE %:search% OR job.companyName LIKE %:search% OR job.skills LIKE %:search% OR job.jobType LIKE %:search% OR job.location LIKE %:search%) AND job.jobStatus = :isActive")
	Job getJobByJobIdAndSearch(@Param("jobId") int jobId, @Param("search") String search, @Param("isActive") boolean isActive);

	////////////////////////
//	@Query("SELECT job FROM Job job WHERE job.jobId NOT IN :jobIds AND job.jobStatus = :isActive AND (job.jobTitle LIKE %:search% OR job.companyName LIKE %:search% OR job.skills LIKE %:search% OR job.jobType LIKE %:search%)")
//	Page<Job> findEverApplyWithUser(List<Integer> jobIds, boolean isActive, String search, PageRequest pageRequest);
//
//
//	@Query("SELECT job FROM Job job WHERE job.jobId IN :jobIds AND job.jobStatus = :isActive AND (job.jobTitle LIKE %:search% OR job.companyName LIKE %:search% OR job.skills LIKE %:search% OR job.jobType LIKE %:search%)")
//	Page<Job> findEverAppliedWithUser(List<Integer> jobIds, boolean isActive, String search, PageRequest pageRequest);

}
