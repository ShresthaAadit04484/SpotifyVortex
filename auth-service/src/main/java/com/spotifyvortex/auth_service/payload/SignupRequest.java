package com.spotifyvortex.auth_service.payload;

public class SignupRequest {
    private String email;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String middleName;
    private String mobileNumber;
    private String address;
    private String gender;

    public SignupRequest(){}

    //Getters
    public String getEmail(){
        return email;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getMiddleName(){
        return middleName;
    }

    public String getMobileNumber(){
        return mobileNumber;
    }
    
    public String getAddress(){
        return address;
    }

    public String getGender(){
        return gender;
    }

    //Setters
    public void setEmail(String email){
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
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
}
