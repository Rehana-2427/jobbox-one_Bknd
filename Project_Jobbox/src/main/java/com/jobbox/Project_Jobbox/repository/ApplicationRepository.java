package com.jobbox.Project_Jobbox.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jobbox.Project_Jobbox.entity.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

	@Query("SELECT COUNT(a) FROM Application a WHERE a.candidateId =?1")
	int getCountOfApplication(int userId);

	@Query("SELECT COUNT(DISTINCT a.companyName) FROM Application a WHERE a.candidateId =?1")
	int countDistinctCompaniesByUserId(int userId);

	@Query("SELECT COUNT(a.applicationId) FROM Application a WHERE a.candidateId = ?1 AND a.applicationStatus =?2")
	int getCountOfShortlistedApplication(int userId, String shortList);

	@Query("SELECT COUNT(a.applicationId) FROM Application a WHERE a.companyName = ?1 AND a.applicationStatus = ?2")
	int getCountofShortlistedApplicationByEachCompany(String companyName, String shortlist);

	@Query("SELECT COUNT(a.applicationId) FROM Application a WHERE a.companyName = ?1")
	int getCountOfApplicationsCompany(String companyName);

	@Query("SELECT COUNT( a.candidateId) FROM Application a WHERE a.hrId = ?1 AND a.applicationStatus=?2")
	int getCountOfUnderPreviewCandidates(Integer userIdByEmail, String string);
//////////////////////////////////////////////////////////////////////////////
	@Query("select app from Application app where app.companyName=?1 AND app.jobId=?2 AND app.candidateId=?3")
	List<Application> getApplicationsByCompany(String companyName, int jobId, int candidateId);
	
	@Query("select app from Application app where app.companyName=?1 AND app.jobId=?2 AND app.candidateId=?3 AND app.applicationStatus=?4")
	List<Application> getApplicationsByCompany(String companyName, int jobId, Integer candidateId, String filterStatus);

	@Query("SELECT COUNT(a.candidateId) FROM Application a WHERE a.companyName = :companyName AND a.jobId = :jobId")
	int getCountOfDreamApplications(@Param("companyName") String companyName, @Param("jobId") int jobId);
///
	@Query("select app from Application app where app.companyName=?1 AND app.jobId=?2  AND app.appliedOn BETWEEN ?3 AND ?4 AND app.candidateId=?5")
	List<Application> getDreamApplicationsWithDateByCompany(String companyName, int jobId, Date fromDate, Date toDate,
			Integer candidateId);
	@Query("select app from Application app where (app.jobId=?1 AND app.applicationStatus=?2 AND app.companyName=?3) AND app.appliedOn BETWEEN ?4 AND ?5 AND app.candidateId=?6")
	List<Application> getFilterDreamApplicationsWithDateByCompany(int jobId, String filterStatus, String companyName,
			Date fromDate, Date toDate, Integer candidateId);
	
	
	@Query("SELECT a FROM Application a WHERE a.candidateId = ?1")
	Page<Application> findAll(int userId, PageRequest pageRequest);

	@Query("SELECT app FROM Application app WHERE (app.applicationStatus LIKE %?1% OR app.companyName LIKE %?1% OR app.jobRole LIKE %?1%) AND app.candidateId = ?2")
	Page<Application> getApplicationByStatus(String searchStatus, int userId, PageRequest pageRequest);

	@Query("SELECT app FROM Application app WHERE (app.applicationStatus LIKE %?1% OR app.companyName LIKE %?1% OR app.jobRole LIKE %?1%) AND app.candidateId = ?2")
	Page<Application> getApplicationByApplicationStatus(String searchStatus, int userId, PageRequest pageRequest);

	@Query("select app from Application app where app.jobId=?1 AND app.applicationStatus=?2")
	Page<Application> getFilterApplicationsWithPagination(int jobId, String filterStatus, PageRequest pageRequest);

	@Query("select app from Application app where app.jobId=?1")
	Page<Application> getApplicationsByJobIdWithPagination(int jobId, PageRequest pageRequest);

	@Query("SELECT a FROM Application a WHERE a.jobId = ?1 AND a.candidateId = ?2")
	Application getApplicationByJobIdAndCandidateId(int jobId, int candidateId);

	@Query("SELECT NEW map(a.appliedOn as date, COUNT(a.applicationId) as count) " + "FROM Application a "
			+ "WHERE a.candidateId = ?1 " + "GROUP BY a.appliedOn")
	List<Map<String, Object>> getCountByDate(int userId);

	@Query("select app from Application app where app.jobId=?1 AND app.applicationStatus=?2 AND app.appliedOn BETWEEN ?3 AND ?4")
	Page<Application> findByJobIdAndApplicationStatusAndAppliedOnBetween(int jobId, String filterStatus, Date fromDate,
			Date toDate, PageRequest pageRequest);

	@Query("SELECT app FROM Application app WHERE app.jobId = ?1  AND app.appliedOn BETWEEN ?2 AND ?3")
	Page<Application> findByJobIdAndAppliedOnBetween(int jobId, Date fromDate, Date toDate, PageRequest pageRequest);

	@Query("select app from Application app where app.jobId=?1 AND app.applicationStatus=?2 AND app.companyName=?3")
	Page<Application> getFilterDreamApplications(int jobId, String filterStatus, String companyName,
			PageRequest pageRequest);

	

	@Query("SELECT app.jobId FROM Application app WHERE app.candidateId = ?1")
	List<Integer> getJobIdsByUserId(int userId);

	@Query("SELECT COUNT(a.applicationId) FROM Application a WHERE a.candidateId = ?1 AND a.companyName=?2 AND a.applicationStatus =?3")
	int getCountOfTotalShortlistedApplicationCompany(int userId, String companyName, String shortList);

	@Query("SELECT a FROM Application a WHERE a.jobId = ?1 AND a.candidateId = ?2 AND a.companyName=?3")
	List<Application> getDreampplicationByJobIdAndCandidateId(int jodId, int userId, String companyName);

	@Query("SELECT a FROM Application a WHERE a.companyName=?1 And a.jobId = ?2")
	Page<Application> getApplicationsByCompany(String companyName, int jobId, PageRequest pageRequest);
	
	
	@Query("SELECT a FROM Application a WHERE a.companyName=?1 And a.jobId = ?2 AND a.candidateId = ?3")
	List<Application> getDreamApplicationsByCompanyBySkills(String companyName, int jobId, Integer candidateId);
	
	
	
	@Query("select app from Application app where app.companyName=?1 AND app.jobId=?2  AND app.appliedOn BETWEEN ?3 AND ?4")
	Page<Application> getDreamApplicationsWithDateByCompany(String companyName, int jobId, Date fromDate, Date toDate,
			PageRequest pageRequest);


	@Query("select app from Application app where (app.jobId=?1 AND app.applicationStatus=?2 AND app.companyName=?3) AND app.appliedOn BETWEEN ?4 AND ?5")
	Page<Application> getFilterDreamApplicationsWithDateByCompany(int jobId, String filterStatus, String companyName, 
			Date fromDate, Date toDate, PageRequest pageRequest);
	
	@Query("SELECT DISTINCT a.companyName FROM Application a WHERE a.candidateId =?1")
	String[] appliedCompanyByUser(int userId);

	@Query("SELECT a FROM Application a WHERE  a.jobId = ?1")
	List<Application> getEvergreenApplicationByCompany(int jobId);

	@Query("SELECT a FROM Application a WHERE a.candidateId = ?1 AND a.hrId != ?2 AND a.jobId != ?2")
	Page<Application> findRegularJobsApplications(int userId, int hrId, PageRequest pageRequest);

	@Query("SELECT a FROM Application a WHERE a.candidateId = ?1 AND a.jobId = 0")
	Page<Application> findDreamJobsApplications(int userId, PageRequest pageRequest);

	@Query("SELECT a FROM Application a WHERE a.candidateId = ?1")
	Page<Application> findEvergreenJobsApplications(int userId, PageRequest pageRequest);

	@Query("SELECT COUNT(a) > 0 FROM Application a WHERE a.candidateId = ?1 AND a.companyName = ?2 AND (?3 IS NULL OR a.jobRole = ?3)")
	boolean findIsAppliedDreamJob(int userId, String companyName, @Param("jobRole") String jobRole);

	
	@Query("SELECT app FROM Application app WHERE app.candidateId = ?1 AND app.jobId = 0 AND (app.applicationStatus LIKE %?2% OR app.companyName LIKE %?2% OR app.jobRole LIKE %?2%)")
	Page<Application> findDreamJobsApplicationsBySearchStatus(int userId, String searchStatus, PageRequest pageRequest);

	@Query("SELECT app FROM Application app WHERE app.jobId = ?1 AND app.candidateId = ?2 AND (app.applicationStatus LIKE %?3% OR app.companyName LIKE %?3% OR app.jobRole LIKE %?3%)")
	Application getApplicationByJobIdAndCandidateIdAndSearchStatus(int jobId, int userId, String searchStatus);


	

}
