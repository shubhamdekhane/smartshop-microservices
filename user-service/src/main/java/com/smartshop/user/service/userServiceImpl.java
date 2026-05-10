package com.smartshop.user.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smartshop.user.dto.userRequest;
import com.smartshop.user.dto.userResponse;
import com.smartshop.user.entity.User;
import com.smartshop.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;



@Service 
@RequiredArgsConstructor
public class userServiceImpl implements userService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public userResponse registerUser(userRequest request) {
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .createdAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }

    @Override
    public userResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> 
                    new RuntimeException("User not found with id: " + id));
        return mapToResponse(user);
    }

    @Override
    public userResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> 
                    new RuntimeException("User not found with email: " + email));
        return mapToResponse(user);
    }

    @Override
    public List<userResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private userResponse mapToResponse(User user) {
        return userResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }

}