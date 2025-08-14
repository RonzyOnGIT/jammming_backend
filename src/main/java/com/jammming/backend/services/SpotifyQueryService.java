package com.jammming.backend.services;

import com.jammming.backend.plain_objects.SpotifyApiResponse;
import com.jammming.backend.entities.User;
import com.jammming.backend.repositories.UserRepository;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SpotifyQueryService {
    
    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public SpotifyQueryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public SpotifyApiResponse fetchSongs(Long id, String songName) {
        // https://api.spotify.com/v1/search?q=${songName}&type=track&limit=6

        Optional<User> optionalUser = this.userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            try {
                
                String endpoint = "https://api.spotify.com/v1/search?q=" + songName + "&type=track&limit=6";

                HttpHeaders headers = new HttpHeaders();

                headers.set("Authorization", "Bearer " + user.getAccessToken());

                HttpEntity<String> entity = new HttpEntity<>(headers);

                ResponseEntity<SpotifyApiResponse> response = this.restTemplate.exchange(endpoint, HttpMethod.GET, entity, SpotifyApiResponse.class);

                SpotifyApiResponse data = response.getBody();

                return data;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }


        } else {
            System.out.println("User not found");
            return null;
        }

    }


}
