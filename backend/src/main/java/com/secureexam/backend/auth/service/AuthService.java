package com.secureexam.backend.auth.service;

import com.secureexam.backend.auth.jwt.JwtUtil;
import com.secureexam.backend.user.User;
import com.secureexam.backend.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.secureexam.backend.auth.dto.AuthResponse;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final com.secureexam.backend.user.repository.RoleRepository roleRepository;

    public AuthService(UserRepository userRepository,
            BCryptPasswordEncoder encoder,
            JwtUtil jwtUtil,
            com.secureexam.backend.user.repository.RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
        this.roleRepository = roleRepository;
    }

    // Authenticate user credentials
    public User authenticate(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!user.isEnabled()) {
            throw new RuntimeException("Account disabled");
        }

        return user;
    }

    // Login and generate JWT token
    public AuthResponse login(String email, String password) {

        User user = authenticate(email, password);
        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user);
    }

    public AuthResponse register(com.secureexam.backend.auth.dto.RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setEnabled(true);

        com.secureexam.backend.user.Role role = roleRepository.findByName("ROLE_STUDENT")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.setRoles(java.util.Set.of(role));

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user);
    }
}