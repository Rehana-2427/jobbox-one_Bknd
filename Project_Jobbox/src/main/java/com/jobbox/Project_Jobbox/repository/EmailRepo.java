package com.jobbox.Project_Jobbox.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jobbox.Project_Jobbox.entity.EmailRequest;

public interface EmailRepo extends JpaRepository<EmailRequest, Integer> {
	@Query("select e from EmailRequest e where e.contactID = ?1")
	Page<EmailRequest> findEmail(int contactID, Pageable pageable);
}
