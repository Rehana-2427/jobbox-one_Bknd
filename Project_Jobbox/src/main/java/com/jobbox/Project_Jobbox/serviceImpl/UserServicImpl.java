package com.jobbox.Project_Jobbox.serviceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jobbox.Project_Jobbox.entity.Company;
import com.jobbox.Project_Jobbox.entity.User;
import com.jobbox.Project_Jobbox.repository.CompanyRepository;
import com.jobbox.Project_Jobbox.repository.UserRepository;
import com.jobbox.Project_Jobbox.service.UserService;

@Service

public class UserServicImpl implements UserService, UserDetailsService {

	Logger logger = LoggerFactory.getLogger(UserServicImpl.class);
	@Autowired
	public UserRepository repository;
	@Autowired
	  @Lazy
	public BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	public CompanyRepository companyRepository;

	@Autowired
	private CompanyServiceImpl companyServiceImpl;
	@Autowired
	private EmailService emailService;

	@Override
	public User saveUser(User user) {
		logger.info("class:: UserServicImpl -> method saveUser :: user {} "+user );
		String email = user.getUserEmail();
		User userByEmail = repository.findUserByEmail(email);
		System.out.println(userByEmail);
		if (userByEmail != null)
			return null;

		else {
			String plainTextPassword = user.getPassword(); // Get the plain text password
			// Hash the password using BCryptPasswordEncoder
			String hashedPassword = bCryptPasswordEncoder.encode(plainTextPassword);
			// Set the hashed password back to the user object
			user.setPassword(hashedPassword);
			// Save the user with the hashed password

			if (user.getCompanyWebsite() != null) {
				String companyName = extractCompanyName(user.getCompanyWebsite());
				System.out.println(companyName + "       comapny Name");
				user.setCompanyName(companyName);

				repository.save(user);
				System.out.println(user.getCompanyWebsite());
				Company company = new Company();
				company.setCompanyName(companyName);
				company.setWebsiteLink(user.getCompanyWebsite());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date appliedOn = null;
				try {
					appliedOn = sdf.parse(user.getAppliedDate());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				company.setDate(appliedOn);
				companyServiceImpl.saveCompany(company);

			} else
				repository.save(user);

			String message = "You have register to JobDB..";
			String subject = "Register Seccessful";
			emailService.sendEmail(email, message, subject);
			return user;
		}
	}

	public String extractCompanyName(String domain) {
		
		System.out.println(domain);
		Company company = companyRepository.findCompany(domain);
		if (company != null) {
			return company.getCompanyName();
		} else {
			// Find the position of the first dot
			int firstDotIndex = domain.indexOf('.');
			int secondDotIndex = -1;
			// Find the position of the second dot
			if (domain.contains(".com")) {
				secondDotIndex = domain.indexOf('.', firstDotIndex + 1);
			}
			// Extract the company name based on the positions of the dots
			if (firstDotIndex != -1) {
				if (secondDotIndex != -1) {
					// Extract the part between the dots
					System.out.println(secondDotIndex);
					return domain.substring(firstDotIndex + 1, secondDotIndex).toUpperCase();
				} else {
					// No second dot found, return the part after the first dot
					System.out.println(firstDotIndex);
					return domain.substring(firstDotIndex + 1).toUpperCase();
				}
			}
		}

		// Return an empty string if no dots are found
		return "";
	}

	@Override
	public User updatePassword(String userEmail, String newPassword) {
		// TODO Auto-generated method stub
		logger.info("class:: UserServicImpl -> method updatePassword :: userEmail {}, newpasswrod {} " + userEmail +" :: " +  newPassword);
		User user = repository.findUserByEmail(userEmail);
		String hashedPassword = bCryptPasswordEncoder.encode(newPassword);
		user.setPassword(hashedPassword);
		repository.save(user);
		return user;
	}

	@Override
	public Page<User> getHRList(String userRole, int page, int size) {
		logger.info("class:: UserServicImpl -> method getHRList :: userRole {} " +userRole);
		PageRequest pageRequest = PageRequest.of(page, size);
		return repository.findHr(userRole, pageRequest);

	}

	@Override
	public void updateUserStatus(String userEmail, Date approvedOn, String userStatus) {
		logger.info("class:: UserServicImpl -> method updateUserStatus :: userEmail {} " + userEmail +" Approved on {}"
				+approvedOn+ " Status {} "+userStatus);
		User u = repository.findUserByEmail(userEmail);
		u.setApprovedOn(approvedOn);
		u.setUserStatus(userStatus);
		repository.save(u);
		String subject = "Status Update";
		String body;
		if (userStatus.equals("Approved")) {
			body = "Welcome , Your application to join as " + u.getCompanyName() + " HR is approved on" + " "
					+ approvedOn + "\n \n Now You Can login as HR";
		} else {
			body = "Sorry, Your application to join as " + u.getCompanyName() + " HR is not approved on " + approvedOn
					+ "\n\n please verify with your company ";
		}
		emailService.sendEmail(userEmail, body, subject);

	}

	@Override
	public void deleteUser(String userEmail) {
		User u = repository.findUserByEmail(userEmail);
		repository.delete(u);
	}

	@Override
	public Page<User> getUserList(int page, int size, String sortBy, String sortOrder) {
		
		logger.info("class:: UserServicImpl -> method getUserList :: userEmail {}, newpasswrod {} + admin " );
		PageRequest pageRequest;

		if (sortBy == null || sortBy.isEmpty()) {
			pageRequest = PageRequest.of(page, size); // No sorting
		} else {
			Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
					: Sort.by(sortBy).descending();
			pageRequest = PageRequest.of(page, size, sort);
		}
		Page<User> users = repository.findAll(pageRequest);
		return users;
	}

	@Override
	public User getHrByEmail(String userEmail) {

		return repository.getHrByEmail(userEmail);
	}

	public Page<User> getHRListEachCompany(String userEmail, int page, int size, String sortBy, String sortOrder) {
//		String companyName = repository.findUserByEmail(userEmail).getCompanyName();
		logger.info("class:: UserServicImpl -> method getHRListEachCompany :: userEmail {} " + userEmail );
		PageRequest pageRequest;

		if (sortBy == null || sortBy.isEmpty()) {
			pageRequest = PageRequest.of(page, size); // No sorting
		} else {
			Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
					: Sort.by(sortBy).descending();
			pageRequest = PageRequest.of(page, size, sort);
		}

		System.out.println(repository.findHrBycomapnyName(userEmail, pageRequest));
		return repository.findHrBycomapnyName(userEmail, pageRequest);
	}

	@Override
	public User getUserByEmail(String userEmail, String password) {
		logger.info("class:: UserServicImpl -> method getUserByEmail :: userEmail {}, passwrod {} " + userEmail +" :: " +  password);
		User user = repository.findUserByEmail(userEmail);
		if (user != null) {
			String existingPassword = user.getPassword();
			boolean isValid = bCryptPasswordEncoder.matches(password, existingPassword);
			if (isValid) {

				return user;
			}
		}
		return null;
	}

	@Override
	public int getuserIdByEmail(String userEmail) {

		return repository.getUserIdByEmail(userEmail);
	}

	@Override
	public String getCompanyNameById(int id) {
		// TODO Auto-generated method stub
		return repository.getCompanyById(id);
	}

	@Override
	public User getUser(int userId) {
		// TODO Auto-generated method stub
		return repository.findUserById(userId);
	}

	@Override
	public int getCountOfHRsByCompany(int companyId) {
		String companyName = companyRepository.getCompanyName(companyId);
		return repository.getCountOfHRByCompany(companyName);
	}

	@Override
	public int getCountOfHRSInCompany(String companyName) {
		// TODO Auto-generated method stub
		return repository.getCountOfHRByCompany(companyName);

	}

	@Override
	public User getHrNameByEmail(String userEmail) {

		return repository.findUserByEmail(userEmail);
	}

	@Override
	public User getUserNameByID(int userId) {
		// TODO Auto-generated method stub
		return repository.getUserNameById(userId);
	}

	@Override
	public Page<User> findHrSeacrh(String search, int page, int size) {
		// TODO Auto-generated method stub
		PageRequest pageRequest = PageRequest.of(page, size);
		return repository.findHrBySearch(search, pageRequest);
	}

	@Override
	public Integer countTotalUsers() {
		// TODO Auto-generated method stub
		return repository.countUsers();
	}

	@Override
	public Integer countTotalHrs() {
		// TODO Auto-generated method stub
		return repository.countHrs();
	}

	@Override
	public User updateUserData(User u) {
		// TODO Auto-generated method stub
		logger.info("class:: UserServicImpl -> method updateUserData :: user"
				+ " "+u );

		User user = repository.findUserByEmail(u.getUserEmail());
		user.setAppliedDate(u.getAppliedDate());
		user.setUserName(u.getUserName());
		user.setPhone(u.getPhone());
		user.setUserRole(u.getUserRole());
		String hashedPassword = bCryptPasswordEncoder.encode(u.getPassword());
		user.setPassword(hashedPassword);
		user = repository.save(user);

		return user;
	}

	 @Override
	    public User updateUserDetails(User user) {
	        logger.info("class:: UserServiceImpl -> method updateUserData :: Received user data for update: " + " " + user);

	        // Find the user by email
	        User existingUser = repository.findUserByEmail(user.getUserEmail());

	        // Verify that the current password matches the existing password
	        if (!bCryptPasswordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
	            throw new RuntimeException("Incorrect current password");
	        }

	        // Update the password with the new encoded password if match is successful
	        existingUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword())); // Update with new password
	        existingUser.setUserName(user.getUserName());

	        // Save updated user data
	        repository.save(existingUser);

	        return existingUser;
	    }


	
	@Override
	public Map<Integer, Double> getCountValidateUserByEachMonth() {
		logger.info("class:: UserServicImpl -> method getCountValidateUserByEachMonth :: userEmail {}, newpasswrod {} + Admin" );
		List<Object[]> countValidateUserByEachMonth = repository.getCountValidateUserByEachMonth();

		int totalUser = countValidateUserByEachMonth.stream().mapToInt(objects -> ((Number) objects[1]).intValue())
				.sum();

		Map<Integer, Double> monthlyUserPercentages = new HashMap<>();
		for (Object[] result : countValidateUserByEachMonth) {
			int month = (int) result[0];
			int userCount = ((Number) result[1]).intValue();
			double percentage = (userCount / (double) totalUser) * 100.0;
			monthlyUserPercentages.put(month, percentage);
		}

		return monthlyUserPercentages;
	}

	@Override
	public Page<User> getRejectedHrs(int page, int size, String sortBy, String sortOrder) {
		// TODO Auto-generated method stub
		logger.info("class:: UserServicImpl -> method  getRejectedHrs :: userEmail {}, newpasswrod {} + Admin" );
		PageRequest pageRequest;

		if (sortBy == null || sortBy.isEmpty()) {
			pageRequest = PageRequest.of(page, size); // No sorting
		} else {
			Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
					: Sort.by(sortBy).descending();
			pageRequest = PageRequest.of(page, size, sort);
		}
		Page<User> users = repository.getRejectedHrs("Hr", pageRequest);
		return users;
	}

	@Override
	public User updateUserDetails(int userId, User updateUserDetails) {
	  
		logger.info("class:: UserServicImpl -> method   updateUserDetails :: userId {}"+userId +" updateUserDetails{} "+updateUserDetails );
	    // Retrieve the existing user from the repository
	    User u = repository.findUserById(userId);
	   
	    // Update other details
	    u.setSkills(updateUserDetails.getSkills());
	    u.setEducationDetails(updateUserDetails.getEducationDetails());
	    u.setExperience(updateUserDetails.getExperience());

	    // Save and return the updated user entity
	    return repository.save(u);
	}

	
	 @Override
	    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	        User user = repository.findUserByEmail(email);
	        if (user == null) {
	            throw new UsernameNotFoundException("User not found with email: " + email);
	        }
	        return org.springframework.security.core.userdetails.User
	                .withUsername(user.getUserEmail())
	                .password(user.getPassword())
	                .roles("USER") // Add roles as per your requirement
	                .build();
	    }

	@Override
	public User checkUserByEmail(String userEmail) {
		// TODO Auto-generated method stub
		return repository.findUserByEmail(userEmail);
	}

	@Override
	public User updateCandidate(int userId, String newName) {
		// TODO Auto-generated method stub
		logger.info("class:: UserServicImpl -> method   updateCandidate :: userId {}"+userId +" newName{} "+newName );
		User user=repository.getById(userId);
		user.setUserName(newName);
		repository.save(user);
		System.out.println(user);
		return user;
	}


}
