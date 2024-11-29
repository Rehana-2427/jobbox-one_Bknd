package com.jobbox.Project_Jobbox.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.jobbox.Project_Jobbox.entity.Resume;

public interface ResumeService {

	List<Resume> downloadResume(int userId);

	Integer getCountOfResumeEachUser(int userId);

	Resume getResume(String resumeUrl, int candidateId);

	ResponseEntity<Resource> downloadResumeByresumeId(Long resumeId);

	String getResumeMessageById(long resumeId);

	void deletedResumeById(Long resumeId);

	ResponseEntity<String> uploadResume(MultipartFile file, String message, String fileType, int userId, String link,
			String briefMessage);

	String getBriefResume(long resumeId);

	Resume getResumeById(long resumeId);

	Resume incrementViewCount(Long resumeId,String action);

	ResponseEntity<Integer> getTotalViewCountByUserId(int userId);

}
