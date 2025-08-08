package com.jammming.backend.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

    @GetMapping("/redirect")
    public RedirectView handleSpotifyRedirect(@RequestParam(value = "code", required = false) String code, @RequestParam(value = "state", required = true) String state, @RequestParam(value = "error", required = false) String error, HttpServletRequest request) {
        
        // gonna have to use JWT or something to encrypt this and better manage the state
        // state from redirect does not match the original state that was passed into the original redirect, fishy stuff
        if (!state.equals(request.getSession().getAttribute("spotify_state"))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid state parameter");
        }

        StringBuilder redirectUrl = new StringBuilder("http://localhost:5173/");

        // user accepted conditions and matching states
        if (code != null) {
            // redirect back to frontend application with "/logged_in" url or something to show that logged in with returned code 
            redirectUrl.append("?code=").append(code);
        }

        // some error happened or user declined conditions
        if (error != null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, error);
        }
        
        return new RedirectView(redirectUrl.toString());

    }

    
}
