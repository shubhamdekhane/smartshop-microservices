package com.smartshop.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.smartshop.user.dto.userRequest;
import com.smartshop.user.dto.userResponse;

public interface userService {
	    userResponse registerUser(userRequest request);
	    userResponse getUserById(Long id);
	    userResponse getUserByEmail(String email);
	    List<userResponse> getAllUsers();

}
