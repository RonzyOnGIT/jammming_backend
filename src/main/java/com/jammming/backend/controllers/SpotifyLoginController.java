package com.jammming.backend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jammming.backend.services.SpotifyLoginService;

@RestController
@RequestMapping("/api")
public class SpotifyLoginController {

    private SpotifyLoginService spotifyLoginService;

    public SpotifyLoginController(SpotifyLoginService spotifyLoginService) {
        this.spotifyLoginService = spotifyLoginService;
    }
    
    // builds url and returns string url so the front-end can redirect users to spotify's auth page
    @GetMapping("/login")
    public String getLoginUrl() {
    
        return this.spotifyLoginService.getLoginUrl();
    }
    
}
