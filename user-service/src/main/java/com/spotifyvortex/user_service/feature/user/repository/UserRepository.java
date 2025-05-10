package com.spotifyvortex.user_service.feature.user.repository;

import com.spotifyvortex.user_service.feature.user.model.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long>{
    public Users findByUsername(String username);
    public Users findByEmail(String email);
}
