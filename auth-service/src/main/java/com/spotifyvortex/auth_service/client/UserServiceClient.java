package com.spotifyvortex.auth_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.spotifyvortex.auth_service.payload.UserDTO;

@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface UserServiceClient {

    @GetMapping("/users/username/{username}")
    ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username);

    @PostMapping("/users")
    ResponseEntity<UserDTO> createUser(@RequestBody UserDTO user);
}
