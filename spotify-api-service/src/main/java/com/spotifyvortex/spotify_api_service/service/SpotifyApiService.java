package com.spotifyvortex.spotify_api_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class SpotifyApiService {

    private static final Logger log = LoggerFactory.getLogger(SpotifyApiService.class);
    private static final String SPOTIFY_API_BASE_URL = "https://api.spotify.com/v1";

    private final SpotifyAuthService spotifyAuthService;
    private final RestTemplate restTemplate;

    public SpotifyApiService(SpotifyAuthService spotifyAuthService, RestTemplate restTemplate) {
        this.spotifyAuthService = spotifyAuthService;
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> getUserProfile(String userId) {
        String accessToken = spotifyAuthService.getAccessToken(userId);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        log.info("Fetching user profile for userId: {}", userId);
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                SPOTIFY_API_BASE_URL + "/me",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        return response.getBody();
    }

    public Map<String, Object> searchTracks(String userId, String query, int limit) {
        String accessToken = spotifyAuthService.getAccessToken(userId);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                SPOTIFY_API_BASE_URL + "/search?q=" + query + "&type=track&limit=" + limit,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        return response.getBody();
    }

    public void playSong(String userId, String trackUri) {
        String accessToken = spotifyAuthService.getAccessToken(userId);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = "{\"uris\":[\"" + trackUri + "\"]}";
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        restTemplate.exchange(
                SPOTIFY_API_BASE_URL + "/me/player/play",
                HttpMethod.PUT,
                entity,
                Void.class
        );
    }

    public Map<String, Object> getRecommendations(String userId, String seedTracks, String seedArtists, String seedGenres) {
        String accessToken = spotifyAuthService.getAccessToken(userId);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        StringBuilder urlBuilder = new StringBuilder(SPOTIFY_API_BASE_URL + "/recommendations?limit=20");

        if (seedTracks != null && !seedTracks.isEmpty()) {
            urlBuilder.append("&seed_tracks=").append(seedTracks);
        }

        if (seedArtists != null && !seedArtists.isEmpty()) {
            urlBuilder.append("&seed_artists=").append(seedArtists);
        }

        if (seedGenres != null && !seedGenres.isEmpty()) {
            urlBuilder.append("&seed_genres=").append(seedGenres);
        }

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                urlBuilder.toString(),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        return response.getBody();
    }
}