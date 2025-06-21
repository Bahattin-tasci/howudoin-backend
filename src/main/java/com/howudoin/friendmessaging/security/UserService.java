package com.howudoin.friendmessaging.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(UserRequestDTO userRequestDTO) {
        if (userRequestDTO.getFirstName() == null || userRequestDTO.getFirstName().isBlank()) {
            throw new IllegalArgumentException("First name cannot be null or blank.");
        }
        if (userRequestDTO.getLastName() == null || userRequestDTO.getLastName().isBlank()) {
            throw new IllegalArgumentException("Last name cannot be null or blank.");
        }

        // Encode the password before saving
        String encodedPassword = passwordEncoder.encode(userRequestDTO.getPassword());

        User user = User.builder()
                .firstName(userRequestDTO.getFirstName())
                .lastName(userRequestDTO.getLastName())
                .email(userRequestDTO.getEmail())
                .password(encodedPassword)
                .friends(new ArrayList<>())
                .friendRequests(new ArrayList<>())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        User savedUser = userRepository.save(user);
        System.out.println("UserService: User created with ID: " + savedUser.getId());
        return savedUser;
    }

    public boolean existsById(String userId) {
        boolean exists = userRepository.existsById(userId);
        System.out.println("UserService: existsById(" + userId + "): " + exists);
        return exists;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }

    public Optional<User> getUserById(String userId) {
        Optional<User> user = userRepository.findOneById(userId);
        System.out.println("UserService: Fetching user with ID: " + userId);
        System.out.println("UserService: User found: " + user.isPresent());
        return user;
    }

}
