package com.spotifyvortex.auth_service.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spotifyvortex.auth_service.client.UserServiceClient;
import com.spotifyvortex.auth_service.model.JwtUserDetails;
import com.spotifyvortex.auth_service.model.UserCredentials;
import com.spotifyvortex.auth_service.payload.AuthRequest;
import com.spotifyvortex.auth_service.payload.AuthResponse;
import com.spotifyvortex.auth_service.payload.RoleDTO;
import com.spotifyvortex.auth_service.payload.SignupRequest;
import com.spotifyvortex.auth_service.payload.UserDTO;
import com.spotifyvortex.auth_service.repository.UserCredentialsRepository;
import com.spotifyvortex.auth_service.security.JwtTokenUtil;

import java.util.Optional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserCredentialsRepository userCredentialRepository;
    private final UserServiceClient userServiceClient;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            AuthenticationManager authenticationManager,
            UserCredentialsRepository userCredentialRepository,
            UserServiceClient userServiceClient,
            JwtTokenUtil jwtTokenUtil,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userCredentialRepository = userCredentialRepository;
        this.userServiceClient = userServiceClient;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse authenticateUser(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        
        String token = jwtTokenUtil.generateToken(userDetails, userId);
        String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails, userId);
        
        // Store the refresh token
        Optional<UserCredentials> userCredentialOpt = userCredentialRepository.findByUserId(userId);
        if (userCredentialOpt.isPresent()) {
            UserCredentials userCredential = userCredentialOpt.get();
            userCredential.setRefreshToken(refreshToken);
            userCredentialRepository.save(userCredential);
        }
        
        // Get user info from user service
        UserDTO userDTO = userServiceClient.getUserByUsername(userDetails.getUsername()).getBody();
        
        return new AuthResponse(
                token,
                refreshToken,
                userId,
                userDetails.getUsername(),
                userDTO != null ? userDTO.getRole().getRole() : "USER"
        );
    }

    public AuthResponse refreshToken(String refreshToken) {
        Optional<UserCredentials> userCredentialOpt = userCredentialRepository.findByRefreshToken(refreshToken);
        
        if (!userCredentialOpt.isPresent()) {
            throw new RuntimeException("Invalid refresh token");
        }
        
        UserCredentials userCredential = userCredentialOpt.get();
        
        // Validate the refresh token
        try {
            String username = jwtTokenUtil.getUsernameFromToken(refreshToken);
            if (!username.equals(userCredential.getUsername())) {
                throw new RuntimeException("Invalid refresh token");
            }
        } catch (Exception e) {
            throw new RuntimeException("Expired or invalid refresh token");
        }
        
        // Get user details
        UserDTO userDTO = userServiceClient.getUserByUsername(userCredential.getUsername()).getBody();
        if (userDTO == null) {
            throw new RuntimeException("User not found");
        }
        
        // Create new JWT token
        JwtUserDetails userDetails = new JwtUserDetails(
                userCredential.getUserId(),
                userCredential.getUsername(),
                userCredential.getPassword(),
                userDTO.isActive(),
                userDTO.isLocked(),
                userDTO.getRole().getRole()
        );
        
        String token = jwtTokenUtil.generateToken(userDetails, userCredential.getUserId());
        String newRefreshToken = jwtTokenUtil.generateRefreshToken(userDetails, userCredential.getUserId());
        
        // Update refresh token in database
        userCredential.setRefreshToken(newRefreshToken);
        userCredentialRepository.save(userCredential);
        
        return new AuthResponse(
                token,
                newRefreshToken,
                userCredential.getUserId(),
                userCredential.getUsername(),
                userDTO.getRole().getRole()
        );
    }

    public AuthResponse registerUser(SignupRequest signupRequest) {
        // Check if username already exists
        if (userCredentialRepository.existsByUsername(signupRequest.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }
        
        // Create new user in user-service
        UserDTO newUser = new UserDTO();
        newUser.setUsername(signupRequest.getUsername());
        newUser.setEmail(signupRequest.getEmail());
        newUser.setFirstName(signupRequest.getFirstName());
        newUser.setLastName(signupRequest.getLastName());
        newUser.setMiddleName(signupRequest.getMiddleName());
        newUser.setMobileNumber(signupRequest.getMobileNumber());
        newUser.setAddress(signupRequest.getAddress());
        newUser.setGender(signupRequest.getGender());
        
        // Default role
        RoleDTO role = new RoleDTO();
        role.setId(2L); // Assuming 2 is for USER role
        role.setRole("ROLE_USER");
        newUser.setRole(role);
        
        // Active by default
        newUser.setActive(true);
        newUser.setLocked(false);
        
        UserDTO createdUser = userServiceClient.createUser(newUser).getBody();
        if (createdUser == null) {
            throw new RuntimeException("Failed to create user");
        }
        
        // Save user credentials
        UserCredentials userCredential = new UserCredentials();
        userCredential.setUserId(createdUser.getId());
        userCredential.setUsername(signupRequest.getUsername());
        userCredential.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        
        userCredentialRepository.save(userCredential);
        
        // Generate authentication tokens
        JwtUserDetails userDetails = new JwtUserDetails(
                createdUser.getId(),
                createdUser.getUsername(),
                userCredential.getPassword(),
                createdUser.isActive(),
                createdUser.isLocked(),
                createdUser.getRole().getRole()
        );
        
        String token = jwtTokenUtil.generateToken(userDetails, createdUser.getId());
        String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails, createdUser.getId());
        
        // Save refresh token
        userCredential.setRefreshToken(refreshToken);
        userCredentialRepository.save(userCredential);
        
        return new AuthResponse(
                token,
                refreshToken,
                createdUser.getId(),
                createdUser.getUsername(),
                createdUser.getRole().getRole()
        );
    }
}