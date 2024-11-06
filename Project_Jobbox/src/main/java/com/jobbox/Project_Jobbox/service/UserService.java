package com.jobbox.Project_Jobbox.service;

import java.util.Date;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.jobbox.Project_Jobbox.entity.User;

public interface UserService {

	User saveUser(User u);

	Page<User> getHRList(String string, int page, int size);

	void updateUserStatus(String userEmail, Date appliedOn, String userStatus);

	void deleteUser(String userEmail);

	Page<User> getUserList(int page, int size, String sortBy, String sortOrder);

	User getHrNameByEmail(String userEmail);

	User getHrByEmail(String userEmail);

//	Page<User> getHRListEachCompany(String userEmail,int page , int pageSize);

	User getUserByEmail(String userEmail, String password);

	int getuserIdByEmail(String userEmail);

	String getCompanyNameById(int id);

	User getUser(int userId);

	int getCountOfHRsByCompany(int companyId);

	User getUserNameByID(int userId);

	Page<User> getHRListEachCompany(String userEmail, int page, int size, String sortBy, String sortOrder);

	Page<User> findHrSeacrh(String search, int page, int size);

	Integer countTotalUsers();

	User updatePassword(String userEmail, String newPassword);

	User updateUserData(User u);

	Integer countTotalHrs();

	Map<Integer, Double> getCountValidateUserByEachMonth();

	int getCountOfHRSInCompany(String companyName);

	Page<User> getRejectedHrs(int page, int size, String sortBy, String sortOrder);

	User updateUserDetails(int userId, User updateUserDetails);

	User checkUserByEmail(String userEmail);

	User updateCandidate(int userId, String newName);

//	String findUserPasswordByName(String email);
//

//
//	List<User> getHRList(String userRole);
//
//	String findUserRole(String email);
//
//	User loginUser(String userEmail, String password);
}
