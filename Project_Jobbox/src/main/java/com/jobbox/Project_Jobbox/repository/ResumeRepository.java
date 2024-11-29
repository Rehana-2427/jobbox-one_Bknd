package com.jobbox.Project_Jobbox.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jobbox.Project_Jobbox.entity.Resume;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Integer> {


	@Query("SELECT r FROM Resume r WHERE r.userId = ?1 AND r.resumeStatus=?2")
	List<Resume> getResumeByuserId(int userId, boolean resumeStatus);

	@Query("SELECT r FROM Resume r WHERE  r.userId =?1 AND r.resumeStatus = true")
	List<Resume> getResumeByuserId(int userId);


	@Query("SELECT COUNT(r) FROM Resume r WHERE r.userId = ?1 AND r.resumeStatus =?2")
	Integer getCountOfResumeEachUser(Integer userId, boolean status);

	@Query("SELECT r FROM Resume r WHERE r.fileName = ?1 AND r.userId=?2")
	Resume getResumeUrl(String resumeUrl, int candidateId);

	@Query("SELECT r FROM Resume r WHERE r.id = ?1")
	Optional<Resume> findById(Long resumeId);

	@Query("SELECT r.message FROM Resume r WHERE r.id = ?1")
	String getResumeMessageById(long resumeId);

	@Modifying
	@Transactional
	@Query("update Resume r set r.resumeStatus = false where r.id = ?1")
	void deleteResumeById(long id);

	@Query("SELECT r.fileName FROM Resume r WHERE r.id = ?1")
	String getBriefResume(long resumeId);
	
	@Query("SELECT r FROM Resume r WHERE r.id = ?1")
	Resume getResumeById(Long resumeId);

    @Query("SELECT SUM(r.viewCount) FROM Resume r WHERE r.userId = :userId")
	Integer getTotalViewCountByUserId(int userId);



}
