package com.spotifyvortex.user_service.feature.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleName role;

    public Role(){}

    public Role(RoleName role){
        this.role = role;
    }

    public Long getRoleId(){
        return id;
    }

    public RoleName getRole(){
        return role;
    }

    public void setRole(RoleName role){
        this.role = role;
    }
}
