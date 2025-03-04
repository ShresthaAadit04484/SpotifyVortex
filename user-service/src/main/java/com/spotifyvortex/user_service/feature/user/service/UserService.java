package com.spotifyvortex.user_service.feature.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.spotifyvortex.user_service.feature.user.model.Role;
import com.spotifyvortex.user_service.feature.user.model.RoleName;
import com.spotifyvortex.user_service.feature.user.model.Users;
import com.spotifyvortex.user_service.feature.user.repository.RoleRepository;
import com.spotifyvortex.user_service.feature.user.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private RoleRepository roleRepo;

    public UserService(UserRepository userRepository, RoleRepository roleRepo){
        this.userRepository = userRepository;
        this.roleRepo = roleRepo;
    }

    //Create or update users
    public Users saveUser(Users user){
        if (user.getRole() != null) {
            Role existingRole = roleRepo.findByRole(user.getRole().getRole())
                .orElse(null); 
            
            if (existingRole == null){
                roleRepo.save(user.getRole());
            } else {
                user.setRole(existingRole);
            }
        } else {
            Role defaultRole = roleRepo.findByRole(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Default role not found."));
            user.setRole(defaultRole);
        }

        return userRepository.save(user);
    }

    public Users updateUser(Long id, Users updatedUser){
        return userRepository.findById(id)
            .map(existingUser -> {
                if(updatedUser.getUsername() != null){
                    existingUser.setUsername(updatedUser.getUsername());
                }

                if(updatedUser.getEmail() != null){
                    existingUser.setEmail(updatedUser.getEmail());
                }

                if(updatedUser.getFirstName() != null){
                    existingUser.setFirstName(updatedUser.getFirstName());
                }

                if(updatedUser.getMiddleName() != null){
                    existingUser.setMiddleName(updatedUser.getMiddleName());
                }

                if(updatedUser.getLastName() != null){
                    existingUser.setLastName(updatedUser.getLastName());
                }

                if(updatedUser.getAddress() != null){
                    existingUser.setAddress(updatedUser.getAddress());
                }

                if(updatedUser.getMobileNumber() != null){
                    existingUser.setMobileNumber(updatedUser.getMobileNumber());
                }

                if(updatedUser.getGender() != null){
                    existingUser.setGender(updatedUser.getGender());
                }

                return userRepository.save(existingUser);
            })
            .orElseThrow(() -> new RuntimeException("User not found with id: "+id));
    }

    //List all users
    public List<Users> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<Users> getUserById(Long id){
        return userRepository.findById(id);
    }

    public Optional<Users> getUserByUsername(String username){
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    public Optional<Users> getUserByEmail(String email){
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    public void deleteByUserId(Long id){
        userRepository.deleteById(id);
    }
    
}
