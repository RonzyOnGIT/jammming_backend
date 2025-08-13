package com.jammming.backend.services;

import com.jammming.backend.services.SpotifyTokenResponse;
import com.jammming.backend.repositories.UserRepository;
import com.jammming.backend.entities.User;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


@Service
public class SpotifyTokenService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final UserRepository userRepository;

    public SpotifyTokenService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Value("${clientId}")
    private String clientId;

    @Value("${clientSecret}")
    private String clientSecret;


    // front end will make a post request and in the controller get the code string to exchange it with a key or something
    public Map<String, Long> exchangeCodeForTokens(String code) {

        Map<String, Long> res = new HashMap<>();

        // in here will have to make a post request to /api/token endpoint
        String url = "https://accounts.spotify.com/api/token";

        // data structure that represents HTTP headers ("header": {})
        HttpHeaders headers = new HttpHeaders();

        // set 'content-type'
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // set 'authorization' field of headers
        String auth = clientId + ":" + clientSecret;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        String authHeader = "Basic " + new String(encodedAuth);
        headers.set("Authorization", authHeader);

        // form field
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("code", code);
        form.add("redirect_uri", "http://localhost:8080/api/redirect");
        form.add("grant_type", "authorization_code");

        // Wrap headers and form data into a request entity to be able to make post request with it
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(form, headers);

        try {
            
            // response will be some json object which will be serialized into SpotifyTokenResponse, then wrap that object with a HTTP response body
            ResponseEntity<SpotifyTokenResponse> response = restTemplate.postForEntity(url, requestEntity, SpotifyTokenResponse.class);

            SpotifyTokenResponse tokens = response.getBody();

            if (tokens != null && tokens.getAccess_token() != null) {
                User user = new User();
                user.setAccessToken(tokens.getAccess_token());
                user.setExpiresIn(tokens.getExpires_in());
                user.setRefreshToken(tokens.getRefresh_token());

                long secondsSinceEpoch = ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond();
                long expireTime = secondsSinceEpoch + tokens.getExpires_in() - 5;

                user.setExpireTime(expireTime);

                User savedUser = this.userRepository.save(user);
                res.put("userId", savedUser.getId());
                res.put("expireTime", savedUser.getExpireTime());
                return res;
            } else {
                return null;
            }

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        }

    }

    public boolean deleteUserById(Long id) {
        if (this.userRepository.existsById(id)) {
            this.userRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    
}
