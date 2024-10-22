package com.jobbox.Project_Jobbox.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jobbox.Project_Jobbox.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("select u from User u where u.userRole=?1 And userStatus IS NULL")
	Page<User> findHr(String userRole, PageRequest pageRequest);

	@Query("UPDATE User u SET u.userStatus = ?3, u.approvedOn = ?2 WHERE u.userEmail = ?1")
	void updateUserStatus(String userEmail, String approvedOn, String userStatus);

	@Query("select u from User u where u.userEmail=?1")
	User findUserByEmail(String userEmail);

	@Query("select u from User u where u.userId=?1")
	User getHrNameByEmail(int userId);

	@Query("select u from User u where u.userEmail=?1")
	User getHrByEmail(String userEmail);

	@Query("select u.companyName from User u where u.userEmail=?1")
	String getCompanyByEmail(String email);

	@Query("select u.userId from User u where u.userEmail=?1")
	Integer getUserIdByEmail(String email);

	@Query("select u from User u where u.userEmail=?1")
	String findHrNameByEmail(String email);

	@Query("select u from User u where u.companyName = (select u2.companyName from User u2 where u2.userEmail = ?1)")
	Page<User> findHrBycomapnyName(String userEmail, PageRequest pageRequest);

	@Query("select u.companyName from User u where u.userId=?1")
	String getCompanyById(int id);

	@Query("select u from User u where u.userId=?1")
	User findUserById(int userId);

	@Query("select COUNT(u.userId) from User u where u.companyName=?1")
	int getCountOfHRByCompany(String companyName);

	@Query("select u from User u where u.userId=?1")
	User getUserNameById(int userId);

	@Query("select u.userEmail from User u where u.userId=?1")
	String getUserEmailById(int userId);

	@Query("select u.userName from User u where u.userEmail=?1")
	String getHrNameEmail(String email);

	@Query("SELECT u FROM User u WHERE u.userName LIKE %:search% OR u.phone LIKE %:search% OR u.userEmail LIKE %:search%")
	Page<User> findHrBySearch(String search, PageRequest pageRequest);

	@Query("SELECT COUNT(u.userId) FROM User u WHERE u.userStatus = 'Approved' AND u.userRole = 'HR'")
	Integer countUsers();

	@Query("SELECT COUNT(u) FROM User u WHERE u.userRole = 'HR' AND u.approvedOn > :timestamp")
	Integer countNewHrsSince(LocalDateTime timestamp);

	@Query("SELECT COUNT(u.userId) FROM User u WHERE u.userStatus IS NULL AND u.userRole = 'HR'")
	Integer countHrs();

	@Query("select u.userName from User u where u.userId=?1")
	User getUserNameByEmail(String userEmailById);

	@Query("SELECT MONTH(user.approvedOn) as month, COUNT(user.userId) as userCount " + "FROM User user "
			+ "WHERE user.userStatus = 'Approved' " + "GROUP BY MONTH(user.approvedOn)")
	List<Object[]> getCountValidateUserByEachMonth();

	@Query("select u from User u where u.userRole = ?1 and u.userStatus = 'Rejected'")
	Page<User> getRejectedHrs(String userRole, Pageable pageable);
	
	@Query("select u from User u where u.companyName=?1 AND u.companyWebsite IS NULL")
	List<User> getHrBycomapnyName(String companyName);
	
	@Query("select u.userId from User u where u.skills LIKE  %:search%")
	List<Integer> findCandidateIdBasedOnSkills(String search);

	

}
