package com.spotifyvortex.user_service.feature.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String firstName;

    @Column
    private String middleName;
    
    @Column(nullable = false)
    private String lastName;

    @Column
    private String mobileNumber;

    @Column
    private String address;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    private boolean active = true;
    private boolean locked = false;

    //Constructor
    public Users(){
        super();
    }

    //Parametarized Constructor
    public Users(Long id, String email, String username, String firstName, String middleName, String lastName, String mobileNumber, String address, boolean active, boolean locked, Gender gender, Role role){
        super();
        this.id = id;
        this.email = email;
        this.username = username;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.active = active;
        this.locked = locked;
        this.gender = gender;
        this.role = role;
    }

    //Getters
    public Long getId(){
        return id;
    }

    public String getEmail(){
        return email;
    }

    public String getUsername(){
        return username;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getMiddleName(){
        return middleName;
    }
    public String getLastName(){
        return lastName;
    }

    public String getMobileNumber(){
        return mobileNumber;
    }

    public String getAddress(){
        return address;
    }

    public boolean isActive(){
        return active;
    }

    public boolean isLocked(){
        return locked;
    }
    
    public Gender getGender(){
        return gender;
    }

    public Role getRole(){
        return role;
    }


    //Setters
    public void setId(Long id){
        this.id = id;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName){
        this.middleName = middleName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public void setMobileNumber(String mobileNumber){
        this.mobileNumber = mobileNumber;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public void setLocked(boolean locked){
        this.locked = locked;
    }

    public void setGender(Gender gender){
        this.gender = gender;
    }

    public void setRole(Role role){
        this.role = role;
    }
}
