package com.example.sustainable_lifestyle_tracker.controller;

import com.example.sustainable_lifestyle_tracker.dto.AuthResponse;
import com.example.sustainable_lifestyle_tracker.dto.LoginRequest;
import com.example.sustainable_lifestyle_tracker.dto.UserDTO;
import com.example.sustainable_lifestyle_tracker.entity.User;
import com.example.sustainable_lifestyle_tracker.repository.UserRepository;
import com.example.sustainable_lifestyle_tracker.service.AuthService;
import com.example.sustainable_lifestyle_tracker.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private UserRepository userRepository;
    private final AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    public AuthController(UserRepository userRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @RequestMapping("/signup")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
        UserDTO saveUser = authService.register(userDTO);
        return ResponseEntity.ok(saveUser);
    }

    @GetMapping("/signup")
    public List<User> getAllUsers() {
        return authService.getAllUsers();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            final UserDetails userDetails = userDetailsService
                    .loadUserByUsername(loginRequest.getEmail());

            final String token = jwtUtil.generateToken(userDetails);

            // Get the user entity to access displayUsername
            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            AuthResponse authResponse = new AuthResponse(token, user.getDisplayUsername(), user.getId());
            return ResponseEntity.ok(authResponse);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
        }
    }

    @GetMapping("/user/profile")
    public UserDTO getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail((userDetails.getUsername()))
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDTO userDTO = new UserDTO();

        userDTO.setUsername(user.getDisplayUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRoles(user.getRoles());

        return userDTO;

    }
}
