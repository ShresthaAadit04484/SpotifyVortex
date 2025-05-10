package com.spotifyvortex.auth_service.payload;

public class UserDTO {
    private Long id;
    private String email;
    private String username;
    private String firstName;
    private String middleName;
    private String lastName;
    private String mobileNumber;
    private String address;
    private String gender;
    private RoleDTO role;
    private boolean active;
    private boolean locked;
    
    // Default constructor
    public UserDTO() {}
    
    // Getters
    public Long getId() {
        return id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getMiddleName() {
        return middleName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getMobileNumber() {
        return mobileNumber;
    }
    
    public String getAddress() {
        return address;
    }
    
    public String getGender() {
        return gender;
    }
    
    public RoleDTO getRole() {
        return role;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public boolean isLocked() {
        return locked;
    }
    
    // Setters
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public void setRole(RoleDTO role) {
        this.role = role;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
