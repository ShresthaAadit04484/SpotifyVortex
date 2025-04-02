package com.spotifyvortex.spotify_api_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spotifyvortex.spotify_api_service.model.SpotifyToken;

@Repository
public interface SpotifyTokenRepository extends JpaRepository<SpotifyToken, String>{
}
