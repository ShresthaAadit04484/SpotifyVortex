package com.spotifyvortex.spotify_api_service.service;

import com.spotifyvortex.spotify_api_service.model.SpotifyToken;
import com.spotifyvortex.spotify_api_service.repository.SpotifyTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

@Service
public class SpotifyAuthService {

    private static final Logger log = LoggerFactory.getLogger(SpotifyAuthService.class);

    private final SpotifyTokenRepository tokenRepository;
    private final RestTemplate restTemplate;

    @Value("${spotify.client.id}")
    private String clientId;

    @Value("${spotify.client.secret}")
    private String clientSecret;

    @Value("${spotify.redirect.uri}")
    private String redirectUri;

    public SpotifyAuthService(SpotifyTokenRepository tokenRepository, RestTemplate restTemplate) {
        this.tokenRepository = tokenRepository;
        this.restTemplate = restTemplate;
    }

    public String getAuthorizationUrl(String userId, String state) {
        return "https://accounts.spotify.com/authorize" +
                "?response_type=code" +
                "&client_id=" + clientId +
                "&scope=user-read-private,user-read-email,streaming,user-read-playback-state,user-modify-playback-state" +
                "&redirect_uri=" + redirectUri +
                "&state=" + state +
                "&show_dialog=true";
    }

    public void handleAuthorizationCode(String userId, String code) {
        HttpHeaders headers = createAuthHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        body.add("redirect_uri", redirectUri);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
            "https://accounts.spotify.com/api/token",
            HttpMethod.POST,
            requestEntity,
            new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> tokenData = response.getBody();
            saveTokenData(userId, tokenData);
        } else {
            log.error("Failed to obtain access token from Spotify. Status: {}", response.getStatusCode().value());
            throw new RuntimeException("Failed to obtain Spotify access token");
        }
    }

    public String getAccessToken(String userId) {
        Optional<SpotifyToken> tokenOpt = tokenRepository.findById(userId);
        if (tokenOpt.isEmpty()) {
            log.error("No Spotify token found for user: {}", userId);
            throw new RuntimeException("User not connected to Spotify");
        }

        SpotifyToken token = tokenOpt.get();
        if (token.isExpired()) {
            refreshToken(token);
        }

        return token.getAccessToken();
    }

    private void refreshToken(SpotifyToken token) {
        HttpHeaders headers = createAuthHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", token.getRefreshToken());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "https://accounts.spotify.com/api/token",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> tokenData = response.getBody();
            token.setAccessToken((String) tokenData.get("access_token"));
            int expiresIn = (Integer) tokenData.get("expires_in");
            token.setExpiresAt(Instant.now().plusSeconds(expiresIn));

            // Some implementations don't return a new refresh token
            if (tokenData.containsKey("refresh_token")) {
                token.setRefreshToken((String) tokenData.get("refresh_token"));
            }

            tokenRepository.save(token);
        } else {
            log.error("Failed to refresh Spotify token. Status: {}", response.getStatusCode().value());
            throw new RuntimeException("Failed to refresh Spotify token");
        }
    }

    private HttpHeaders createAuthHeaders() {
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + encodedAuth);
        return headers;
    }

    private void saveTokenData(String userId, Map<String, Object> tokenData) {
        String accessToken = (String) tokenData.get("access_token");
        String refreshToken = (String) tokenData.get("refresh_token");
        Integer expiresIn = (Integer) tokenData.get("expires_in");

        com.spotifyvortex.spotify_api_service.model.SpotifyToken token = new com.spotifyvortex.spotify_api_service.model.SpotifyToken();
        token.setUserId(userId);
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setExpiresAt(Instant.now().plusSeconds(expiresIn));

        tokenRepository.save(token);
    }
}