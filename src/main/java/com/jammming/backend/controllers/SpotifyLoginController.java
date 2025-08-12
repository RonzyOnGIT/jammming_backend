package com.jammming.backend.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.server.ResponseStatusException;


import com.jammming.backend.services.SpotifyLoginService;

// frontend makes fetch request to return a string url then redirect there
// after using logs in to spotify redirect back to another redirect
// in the endpoint, redirect user back to spotify using logged in or something by doing return new RedirectView("");

@RestController
@RequestMapping("/api")
public class SpotifyLoginController {

    private final SpotifyLoginService spotifyLoginService;

    public SpotifyLoginController(SpotifyLoginService spotifyLoginService) {
        this.spotifyLoginService = spotifyLoginService;
    }
    
    // builds url and returns string url so the front-end can redirect users to spotify's auth page
    //  have to come back to add other possible error codes
    // for some reason, cant even hit this api from the frontend, has to do with interaction between frontend and the backend
    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> getLoginUrl(HttpServletRequest request) {

        String stateId = this.spotifyLoginService.getStateId();

        // store the state into the current HTTP session
        request.getSession().setAttribute("spotify_state", stateId);

        Map<String, String> response = Map.of(
            "url", this.spotifyLoginService.getLoginUrl(stateId)
        );

        return ResponseEntity.ok(response);

    }

    // clean this up and throw logic in the service class
    @GetMapping("/redirect")
    public RedirectView handleSpotifyRedirect(@RequestParam(value = "code", required = false) String code, @RequestParam(value = "state", required = true) String state, @RequestParam(value = "error", required = false) String error, HttpServletRequest request) {
        
        try {
            return this.spotifyLoginService.handleLoginRedirect(code, state, error, request);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }   

    }

    
}
