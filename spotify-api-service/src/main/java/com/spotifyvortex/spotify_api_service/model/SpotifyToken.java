package com.spotifyvortex.spotify_api_service.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "spotify_tokens")
public class SpotifyToken {

    @Id
    private String userId;

    @Column(length = 2000)
    private String accessToken;

    @Column(length = 2000)
    private String refreshToken;

    private Instant expiresAt;

    public SpotifyToken(){
        super();
    }

    public SpotifyToken(String userId, String accessToken, String refreshToken, Instant expiresAt){
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
    }

    
    // Getters
    public String getUserId(){
        return userId;
    }
    
    public String getAccessToken(){
        return accessToken;
    }
    
    public String getRefreshToken(){
        return refreshToken;
    }
    
    public boolean isExpired(){
        return Instant.now().isAfter(expiresAt);
    }

// Setters
    public void setUserId(String userId){
        this.userId = userId;
    }

    public void setAccessToken(String accessToken){
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }
    
    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpotifyToken that = (SpotifyToken) o;

        if (!userId.equals(that.userId)) return false;
        if (!accessToken.equals(that.accessToken)) return false;
        if (!refreshToken.equals(that.refreshToken)) return false;
        return expiresAt.equals(that.expiresAt);
    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + accessToken.hashCode();
        result = 31 * result + refreshToken.hashCode();
        result = 31 * result + expiresAt.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SpotifyToken{" +
                "userId='" + userId + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", expiresAt=" + expiresAt +
                '}';
    }
}
