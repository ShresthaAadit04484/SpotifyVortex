package com.spotifyvortex.auth_service.payload;

public class AuthResponse {
    private String token;
    private String refreshToken;
    private String type = "Bearer";
    private Long userId;
    private String username;
    private String role;

    public AuthResponse(){}

    public AuthResponse(String token, String refreshToken, Long userId, String username, 
                        String role){
        this.token = token;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    public String gettoken(){
        return token;
    }

    public void setToken(String token){
        this.token = token;
    }

    public String getRefreshToken(){
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public Long getUserId(){
        return userId;
    }

    public void setUserId(Long userId){
        this.userId = userId;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getRole(){
        return role;
    }

    public void setRole(String role){
        this.role = role;
    }
}
