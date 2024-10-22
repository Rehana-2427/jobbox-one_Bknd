package com.jobbox.Project_Jobbox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobbox.Project_Jobbox.entity.ResumeView;

public interface ResumeViewRepository extends JpaRepository<ResumeView, Integer> {
 
	List<ResumeView> findByCandidateId(Long candidateId); // To track views for a candidate

	int countByResumeId(int resumeId);
	
}
