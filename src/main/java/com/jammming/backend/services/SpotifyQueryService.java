package com.jammming.backend.services;

import com.jammming.backend.plain_objects.SpotifyApiResponse;
import com.jammming.backend.plain_objects.SpotifyUser;
import com.jammming.backend.plain_objects.Playlist;
import com.jammming.backend.entities.User;
import com.jammming.backend.repositories.UserRepository;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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

    // in the future save the Spotify ID into the database here
    public String createPlaylist(Map<String, Serializable> payload) {
        // want to first look up user

        if (payload == null || payload.get("userId") == null || payload.get("name") == null || payload.get("description") == null) {
            System.out.println("Empty payload or missing fields");
            return null;
        }

        Object payloadIdObj = payload.get("userId");
        Long payloadId = null;

        // make sure userId is valid format -----------------------
        if (payloadIdObj instanceof Number) {
            payloadId = ((Number) payloadIdObj).longValue();
            // if String, convert to Long
        } else if (payloadIdObj instanceof String) {
            try {
                payloadId = Long.parseLong((String) payloadIdObj);
            } catch (NumberFormatException e) {
                System.out.println("Invalid userId format");
            }
        } else {
            System.out.println("userId not valid type");
        }
        // ---------------------------------------------------------

        Optional<User> retrievedUserOptional = this.userRepository.findById(payloadId);

        if (retrievedUserOptional.isPresent()) {
            User user = retrievedUserOptional.get();

            try {

                String endpoint = "https://api.spotify.com/v1/me";

                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + user.getAccessToken());

                HttpEntity<String> entity = new HttpEntity<>(headers);

                ResponseEntity<SpotifyUser> response = this.restTemplate.exchange(endpoint, HttpMethod.GET, entity, SpotifyUser.class);

                SpotifyUser spotifyUser = response.getBody();

                // here we make a post request to create an empty playlist
                String playlistEndpoint = "https://api.spotify.com/v1/users/" + spotifyUser.getId() + "/playlists";
                headers.setContentType(MediaType.APPLICATION_JSON);

                Map<String, Object> body = new HashMap<>();

                body.put("name", payload.get("name"));
                body.put("description", payload.get("description"));
                body.put("public", true);

                HttpEntity<Map<String, Object>> playlistEntity = new HttpEntity<>(body, headers);

                ResponseEntity<Playlist> playlistResponse = restTemplate.postForEntity(playlistEndpoint, playlistEntity, Playlist.class);

                Playlist newPlaylist = playlistResponse.getBody();



                // now we populate the newly created playlist

                String populatePlaylistEndpoint = "https://api.spotify.com/v1/playlists/" + newPlaylist.getId() + "/tracks";

                HttpHeaders newHeaders = new HttpHeaders();

                newHeaders.set("Authorization", "Bearer " + user.getAccessToken());
                newHeaders.setContentType(MediaType.APPLICATION_JSON);

                @SuppressWarnings("unchecked")
                List<String> uris = (List<String>) payload.get("uris"); // cast payload value to List<String>
                Map<String, Object> uriArraysBody = new HashMap<>();
                
                uriArraysBody.put("uris", uris);

                HttpEntity<Map<String, Object>> playlistCreatedEntity = new HttpEntity<>(body, newHeaders);
                ResponseEntity<String> finalResponse = restTemplate.postForEntity(populatePlaylistEndpoint, playlistCreatedEntity, String.class);
            
                return finalResponse.getBody();

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
