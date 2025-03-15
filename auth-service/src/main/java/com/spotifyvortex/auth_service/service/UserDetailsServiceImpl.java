package com.spotifyvortex.auth_service.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spotifyvortex.auth_service.client.UserServiceClient;
import com.spotifyvortex.auth_service.model.JwtUserDetails;
import com.spotifyvortex.auth_service.model.UserCredentials;
import com.spotifyvortex.auth_service.payload.UserDTO;
import com.spotifyvortex.auth_service.repository.UserCredentialsRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    private final UserCredentialsRepository userCredentialRepository;
    private final UserServiceClient userServiceClient;

    public UserDetailsServiceImpl(UserCredentialsRepository userCredentialRepository, UserServiceClient userServiceClient){
        this.userCredentialRepository = userCredentialRepository;
        this.userServiceClient = userServiceClient;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        UserCredentials userCredential = userCredentialRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    
        UserDTO userDTO;
        try {
            userDTO = userServiceClient.getUserByUsername(username).getBody();
            if (userDTO == null) {
                throw new UsernameNotFoundException("User details not found for username: " + username);
            }
        } catch (Exception e) {
            throw new UsernameNotFoundException("Error fetching user details: " + e.getMessage());
        }

        return new JwtUserDetails(
            userCredential.getUserId(),
            userCredential.getUsername(),
            userCredential.getPassword(),
            userDTO.isActive(),
            userDTO.isLocked(),
            userDTO.getRole().getRole()
        );
    }

}
