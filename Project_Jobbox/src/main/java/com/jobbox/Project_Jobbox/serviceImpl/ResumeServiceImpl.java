package com.jobbox.Project_Jobbox.serviceImpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jobbox.Project_Jobbox.entity.Resume;
import com.jobbox.Project_Jobbox.entity.ResumeView;
import com.jobbox.Project_Jobbox.repository.ResumeRepository;
import com.jobbox.Project_Jobbox.repository.ResumeViewRepository;
import com.jobbox.Project_Jobbox.repository.UserRepository;
import com.jobbox.Project_Jobbox.service.ResumeService;

import jakarta.transaction.Transactional;

@Service
public class ResumeServiceImpl implements ResumeService {

	@Autowired
	public ResumeRepository resumeRepository;
	@Autowired
	public UserRepository userRepository;

	@Transactional
	@Override
	public ResponseEntity<String> uploadResume(MultipartFile file, String message, String fileType, int userId,
			String link, String briefMessage) {

		Resume resume = new Resume();
		if (file != null) {
			try {
				resume.setContent(file.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			resume.setFileName(file.getOriginalFilename());
			System.out.println("File Name: " + file.getOriginalFilename());
		}
		if (link != null) {
			resume.setFileName(link);
			System.out.println("Link: " + link);
		}
		if (briefMessage != null) {
			resume.setFileName(briefMessage);
			System.out.println("Brief Message: " + briefMessage);
		}
		resume.setMessage(message);
		resume.setFileType(fileType);
		resume.setUserId(userId);
		resume.setResumeStatus(true);
		resumeRepository.save(resume);

		return ResponseEntity.ok("File uploaded successfully");
	}

	@Override
	public List<Resume> downloadResume(int userId) {
		List<Resume> resumes = resumeRepository.getResumeByuserId(userId, true);
		return resumes;
	}

	@Override
	public Integer getCountOfResumeEachUser(int userId) {
		return resumeRepository.getCountOfResumeEachUser(userId, true);
	}

	@Override
	public Resume getResume(String resumeUrl, int candidateId) {
		// TODO Auto-generated method stub
		return resumeRepository.getResumeUrl(resumeUrl, candidateId);
	}

	@Override
	public ResponseEntity<Resource> downloadResumeByresumeId(Long resumeId) {
		Optional<Resume> optionalResume = resumeRepository.findById(resumeId);
		if (optionalResume.isPresent()) {
			Resume resume = optionalResume.get();
			ByteArrayResource resource = new ByteArrayResource(resume.getContent());

			return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resume.getFileName() + "\"")
					.body(resource);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@Override
	public String getResumeMessageById(long resumeId) {

		return resumeRepository.getResumeMessageById(resumeId);
	}

	@Override
	public void deletedResumeById(Long resumeId) {
		// TODO Auto-generated method stub
		resumeRepository.deleteResumeById(resumeId);
	}

	@Override
	public String getBriefResume(long resumeId) {
		// TODO Auto-generated method stub
		return resumeRepository.getBriefResume(resumeId);
	}

	@Override
	public Resume getResumeById(long resumeId) {
		// TODO Auto-generated method stub
		return resumeRepository.getResumeById(resumeId);
	}

	@Override
	public Resume incrementViewCount(Long resumeId, String action) {
		// TODO Auto-generated method stub
		Resume resume = resumeRepository.getResumeById(resumeId);
		resume.setViewCount(resume.getViewCount() + 1);
		resume.setAction(action);
		return resumeRepository.save(resume);

	}

	@Override
	public ResponseEntity<Integer> getTotalViewCountByUserId(int userId) {
		// TODO Auto-generated method stub
		Integer resumeViewCount = resumeRepository.getTotalViewCountByUserId(userId);
		// Return the total view count or a 404 if not found
		if (resumeViewCount != null) {
			return ResponseEntity.ok(resumeViewCount);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}
