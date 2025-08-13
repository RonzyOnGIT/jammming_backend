package com.jammming.backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.jammming.backend.services.SpotifyTokenService;

import jakarta.servlet.http.HttpServletRequest;

// handles exchange for token, and logging out users (deleting users)
@RestController
@RequestMapping("/api")
public class SpotifyTokenController {
    

    private final SpotifyTokenService spotifyTokenService;

    public SpotifyTokenController(SpotifyTokenService spotifyTokenService) {
        this.spotifyTokenService = spotifyTokenService;
    }


    // make an endpoint where the user will request for 
    // so the frontend will just make a fetch here to this endpoint, and from here, we will actually make the call to spotify
    @PostMapping("/exchange")
    // this returns a responseEntity of type hashmap which stores String as id and object as value, essentially returns json object like {"key": {obj}};
    // frontend will make a post request with the url and call this endpoint, from here get the body of the post request and directly interact with spotify
    public ResponseEntity<Map<String, Object>> exchangeCode(@RequestBody Map<String, String> payload, HttpServletRequest request) {
        
        String code = payload.get("code");

        if (code == null || code.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "userId", null, "expireTime", null));
        }

        Map<String, Long> res = this.spotifyTokenService.exchangeCodeForTokens(code);
        // Long userId = this.spotifyTokenService.exchangeCodeForTokens(code);
        if (res == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false, "userId", null, "expireTime", null));
        }

        return ResponseEntity.ok(Map.of("success", true, "userId", res.get("userId"), "expireTime", res.get("expireTime")));

    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = this.spotifyTokenService.deleteUserById(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    
}
