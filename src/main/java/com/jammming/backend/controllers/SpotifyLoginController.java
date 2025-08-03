package com.jammming.backend.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.jammming.backend.services.SpotifyLoginService;

// frontend makes fetch request to return a string url then redirect there
// after using logs in to spotify redirect back to another redirect
// in the endpoint, redirect user back to spotify using logged in or something by doing return new RedirectView("");


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class SpotifyLoginController {

    private SpotifyLoginService spotifyLoginService;

    public SpotifyLoginController(SpotifyLoginService spotifyLoginService) {
        this.spotifyLoginService = spotifyLoginService;
    }
    
    // builds url and returns string url so the front-end can redirect users to spotify's auth page
    //  have to come back to add other possible error codes
    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> getLoginUrl() {

        String stateId = this.spotifyLoginService.getStateId();

        Map<String, String> response = Map.of(
            "state", stateId,
            "url", this.spotifyLoginService.getLoginUrl(stateId)
        );

        return ResponseEntity.ok(response);

    }

    // user will get redirected to this endpoint
    @GetMapping("/redirect")
    public RedirectView handleSpotifyRedirect() {
       return new RedirectView("http://localhost:3000/loggedin");

    }

    
}
