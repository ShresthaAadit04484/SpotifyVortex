package com.spotifyvortex.auth_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spotifyvortex.auth_service.model.UserCredentials;

@Repository
public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Long>{
    Optional<UserCredentials> findByUsername(String username);
    Optional<UserCredentials> findByUserId(Long userId);
    Optional<UserCredentials> findByRefreshToken(String refreshToken);  
    boolean existsByUsername(String username);
}
