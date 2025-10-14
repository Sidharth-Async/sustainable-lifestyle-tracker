package com.example.sustainable_lifestyle_tracker.service;

import com.example.sustainable_lifestyle_tracker.dto.UserDTO;
import com.example.sustainable_lifestyle_tracker.entity.User;
import com.example.sustainable_lifestyle_tracker.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    // ✅ Sign up logic
    public UserDTO register(UserDTO userDTO) {

        // Check if the email already exists
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email already exists.");
        }

        // Encrypt password
        String hashPassword = passwordEncoder.encode(userDTO.getPassword());

        // Create new user entity
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(hashPassword);

        // ✅ Ensure at least one default role
        Set<String> roles = userDTO.getRoles();
        if (roles == null || roles.isEmpty()) {
            roles = new HashSet<>();
            roles.add("USER");
        }
        user.setRoles(roles);

        // Save user
        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }

    private UserDTO mapToResponse(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRoles(user.getRoles());
        return userDTO;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
