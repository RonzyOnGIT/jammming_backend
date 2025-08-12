package com.jammming.backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.jammming.backend.services.SpotifyTokenService;

import jakarta.servlet.http.HttpServletRequest;

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
    public ResponseEntity<Map<String, Boolean>> exchangeCode(@RequestBody Map<String, String> payload, HttpServletRequest request) {
        
        String code = payload.get("code");

        if (code == null || code.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false));
        }

        boolean success = this.spotifyTokenService.exchangeCodeForTokens(code);
        if (!success) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false));
        }

        return ResponseEntity.ok(Map.of("success", success));

    }


    
}
