package com.spotifyvortex.auth_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spotifyvortex.auth_service.model.RefreshTokenRequest;
import com.spotifyvortex.auth_service.payload.AuthRequest;
import com.spotifyvortex.auth_service.payload.AuthResponse;
import com.spotifyvortex.auth_service.payload.SignupRequest;
import com.spotifyvortex.auth_service.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest authRequest){
        return ResponseEntity.ok(authService.authenticateUser(authRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest.getRefreshToken()));
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody SignupRequest signupRequest) {
        return ResponseEntity.ok(authService.registerUser(signupRequest));
    }
    
}
