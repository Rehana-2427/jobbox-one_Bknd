package com.jobbox.Project_Jobbox.controller;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springdoc.core.service.GenericResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobbox.Project_Jobbox.entity.User;
import com.jobbox.Project_Jobbox.jwttoken.JwtUtil;
import com.jobbox.Project_Jobbox.response.LoginResponse;
import com.jobbox.Project_Jobbox.service.UserService;

@CrossOrigin(origins = { "http://51.79.18.21:3000", "http://localhost:3000" })
@Controller
@RequestMapping("/api/jobbox")
@RestController
public class UserController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	public UserService service;

	@PostMapping("/saveUser")
	public ResponseEntity<?> saveUser(@RequestBody User u) {
	    User user = service.saveUser(u);
	    if (user == null) {
	        // If user already exists, return 200 OK with a message
	        return new ResponseEntity<>("User with this email already exists",HttpStatus.ALREADY_REPORTED);
	    }
	    // If user is successfully registered, return 201 Created
	    return new ResponseEntity<>(user, HttpStatus.CREATED);
	}

	
	
    @GetMapping("/checkUser")
    public ResponseEntity<?> checkUser(@RequestParam String userEmail) {
        User user = service.checkUserByEmail(userEmail);

        if (user != null) {
            return ResponseEntity.ok().body(user); // Return the user details if found
        } else {
            return ResponseEntity.status(204).body("User not found"); // Return not found if no user exists
        }
    }

	@PutMapping("/updatePassword")
	public ResponseEntity<User> updatePassword(@RequestParam String userEmail, @RequestParam String newPassword) {
		return new ResponseEntity<User>(service.updatePassword(userEmail, newPassword), HttpStatus.OK);
	}

//	@GetMapping("/login")
//	public ResponseEntity<User> userLogin(@RequestParam String userEmail, @RequestParam String password) {
//		User user = service.getUserByEmail(userEmail, password);
//		return new ResponseEntity<User>(user, HttpStatus.OK);
//	}
	@GetMapping("/login")
	public ResponseEntity<LoginResponse> userLogin(@RequestParam String userEmail, @RequestParam String password) {
	    try {
	        // Authenticate user
	        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userEmail, password));

	        // If authentication is successful, find the user details
	        final User userDetails = service.getUserByEmail(userEmail, password);

	        // Generate JWT token using the user's email (or any unique identifier)
	        final String jwt = jwtUtil.generateToken(userDetails.getUserEmail());

	        // Return the user object and token wrapped in a custom response object
	        LoginResponse response = new LoginResponse(userDetails, jwt);

	        return new ResponseEntity<LoginResponse>(response, HttpStatus.OK);
	    } catch (BadCredentialsException e) {
	        // Return 401 if authentication fails due to invalid credentials
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body(new LoginResponse(null, "Invalid email or password"));
	    } catch (Exception e) {
	        // Return 500 if there is a server error
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new LoginResponse(null, "Login failed due to server error"));
	    }
	}

//	@PostMapping("/auth/token")
//	public ResponseEntity<LoginResponse> userLogin(@RequestParam String userEmail, @RequestParam String password) {
//	    try {
//	        // Authenticate user
//	        authenticationManager.authenticate(
//	            new UsernamePasswordAuthenticationToken(userEmail, password)
//	        );
//
//	        // If authentication is successful, find the user details
//	        final User userDetails = service.getUserByEmail(userEmail, password);
//
//	        // Generate JWT token using the user's email (or any unique identifier)
//	        final String jwt = jwtUtil.generateToken(userDetails.getUserEmail());
//
//	        // Return the user object and token wrapped in a custom response object
//	        LoginResponse response = new LoginResponse(userDetails, jwt);
//
//	        return ResponseEntity.ok(response);
//	    } catch (BadCredentialsException e) {
//	        // Return 401 if authentication fails due to invalid credentials
//	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//	                .body(new LoginResponse(null, "Invalid email or password"));
//	    } catch (Exception e) {
//	        // Return 500 if there is a server error
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//	                .body(new LoginResponse(null, "Login failed due to server error"));
//	    }
//	}
	@GetMapping("/displayUsers")
	public ResponseEntity<Page<User>> getUserPage(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String sortOrder) {
		Page<User> usersList = null;
		try {
			usersList = service.getUserList(page, size, sortBy, sortOrder);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ResponseEntity<Page<User>>(usersList, HttpStatus.OK);
	}

	@GetMapping("/getHr")
	public ResponseEntity<Page<User>> getHRPage(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {
		Page<User> usersList = null;
		try {
			usersList = service.getHRList("HR", page, size);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<Page<User>>(usersList, HttpStatus.OK);
	}

	@GetMapping("/countofHrs")
	public ResponseEntity<Integer> countOfHrs() {
		return new ResponseEntity<Integer>(service.countTotalHrs(), HttpStatus.OK);
	}

	@GetMapping("/countValidatedUsers")
	public ResponseEntity<Integer> countTotalUsers() {
		return new ResponseEntity<Integer>(service.countTotalUsers(), HttpStatus.OK);
	}

	@PutMapping("/updateApprove")
	public ResponseEntity<String> updateUser(@RequestParam String userEmail, @RequestParam String approvedOn,
			@RequestParam String userStatus) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date appliedOn = null;
		try {
			appliedOn = sdf.parse(approvedOn);
		} catch (Exception e) {
			// TODO: handle exception
		}
		service.updateUserStatus(userEmail, appliedOn, userStatus);
		return new ResponseEntity<String>("update successFull", HttpStatus.OK);
	}

	@DeleteMapping("/deleteUser")
	public ResponseEntity<String> deleteUser(@RequestParam String userEmail) {
		service.deleteUser(userEmail);
		return new ResponseEntity<String>("update successFull", HttpStatus.OK);
	}

	@GetMapping("/getUserName")
	public ResponseEntity<User> getHrNameByID(@RequestParam int userId) {
		if (userId != 0) {
			User u = service.getUserNameByID(userId);
			System.out.println(u.getUserName());
			return new ResponseEntity<User>(u, HttpStatus.OK);
		} else
			return null;
	}

	@GetMapping("/getHRName")
	public ResponseEntity<User> getHrByEmail(@RequestParam String userEmail) {
		User u = service.getHrByEmail(userEmail);
		return new ResponseEntity<User>(u, HttpStatus.OK);
	}

	@GetMapping("/getCandidate")
	public ResponseEntity<User> getCandidateEmail(@RequestParam int userId) {
		User u = service.getUser(userId);
		return new ResponseEntity<User>(u, HttpStatus.OK);
	}

	@GetMapping("/getHrEachCompany")
	public ResponseEntity<Page<User>> getHR(@RequestParam String userEmail, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String sortOrder) {
		Page<User> usersList = null;
		try {
			usersList = service.getHRListEachCompany(userEmail, page, size, sortBy, sortOrder);
			System.out.println(usersList.toString() + usersList.getSize());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Page<User>>(usersList, HttpStatus.OK);
	}

	@GetMapping("/getUser")
	public ResponseEntity<User> getUser(@RequestParam int userId) {
		return new ResponseEntity<User>(service.getUser(userId), HttpStatus.OK);
	}

	@GetMapping("/countOfHRByCompany")
	public ResponseEntity<Integer> getCountOfHRByCompany(@RequestParam int companyId) {
		int hrs = service.getCountOfHRsByCompany(companyId);
		return new ResponseEntity<Integer>(hrs, HttpStatus.OK);
	}

	@GetMapping("/countOfHRSInCompany")
	public ResponseEntity<Integer> getCountOfHRByCompany(@RequestParam String companyName) {
		int hrs = service.getCountOfHRSInCompany(companyName);
		return new ResponseEntity<Integer>(hrs, HttpStatus.OK);
	}

	@GetMapping("/searchHr")
	public ResponseEntity<Page<User>> displayHrBySearch(@RequestParam String search,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
		return new ResponseEntity<Page<User>>(service.findHrSeacrh(search, page, size), HttpStatus.OK);
	}

	@PutMapping("/updateUserData")
	public ResponseEntity<User> updateUserData(@RequestBody User user) {
		User u = service.updateUserData(user);
		return new ResponseEntity<User>(u, HttpStatus.OK);
	}
	
	
	  @PutMapping("/updateUserDetails")
	    public ResponseEntity<User> updateUserDetails(@RequestBody User user) {
	        try {
	            User updateResult = service.updateUserDetails(user);
	            return new ResponseEntity<>(updateResult, HttpStatus.OK);
	        } catch (RuntimeException e) {
	            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	        }
	    }

	@PutMapping("/updateCandidateDetails")
	public ResponseEntity<User> updateCandidateDetails(@RequestParam int userId, @RequestBody User updateUserDetails) {
		User updateCandidate = service.updateUserDetails(userId, updateUserDetails);
		return ResponseEntity.ok(updateCandidate);
	}

	@GetMapping("/countValidateUsersByMonth")
	public ResponseEntity<Map<Integer, Double>> getCountValidateUserByEachMonth() {
		Map<Integer, Double> countValidateUserByEachMonth = service.getCountValidateUserByEachMonth();
		return new ResponseEntity<Map<Integer, Double>>(countValidateUserByEachMonth, HttpStatus.OK);
	}

	@GetMapping("/rejectedHrs")
	public ResponseEntity<Page<User>> getRejectedHrs(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String sortOrder) {
		Page<User> usersList = null;
		try {
			usersList = service.getRejectedHrs(page, size, sortBy, sortOrder);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ResponseEntity<Page<User>>(usersList, HttpStatus.OK);
	}

	@GetMapping("/fetchCandidateDetails")
	public ResponseEntity<User> fetchCandidateDetails(@RequestParam int candidateId) {
		User user = service.getUser(candidateId);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	
	@PutMapping("/updateCandidate")
	public ResponseEntity<User> updateCandidate(@RequestParam int userId, @RequestParam String newName) {
		User updateCandidate = service.updateCandidate(userId, newName);
		return ResponseEntity.ok(updateCandidate);
	}

}
