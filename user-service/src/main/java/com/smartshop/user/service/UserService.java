package com.smartshop.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.smartshop.user.dto.UserRequest;
import com.smartshop.user.dto.UserResponse;

public interface UserService {
	    UserResponse registerUser(UserRequest request);
	    UserResponse getUserById(Long id);
	    UserResponse getUserByEmail(String email);
	    List<UserResponse> getAllUsers();

}
