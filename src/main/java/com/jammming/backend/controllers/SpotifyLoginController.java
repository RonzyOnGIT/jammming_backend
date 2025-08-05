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
    public ResponseEntity<Map<String, String>> getLoginUrl(HttpServletRequest request) {

        String stateId = this.spotifyLoginService.getStateId();

        // store the state into the current HTTP session
        request.getSession().setAttribute("spotify_state", stateId);

        Map<String, String> response = Map.of(
            "url", this.spotifyLoginService.getLoginUrl(stateId)
        );

        return ResponseEntity.ok(response);

    }

    // user will get redirected to this endpoint
    @GetMapping("/redirect")
    public RedirectView handleSpotifyRedirect(@RequestParam(value = "code", required = false) String code, @RequestParam(value = "state", required = true) String state, @RequestParam(value = "error", required = false) String error, HttpServletRequest request) {
        
        // 4
        // first case is that the user accepted the terms and state matches
        // first check that the state matches, because no matter what if mismatch of states, then probably not a good thing
        if (request.getSession().getAttribute("spotify_state") != state) {
            // state mismatch, return some message saying error
        }

        

        // second case is that the user accepted but state no match

        // 3rd case is that the user did not accept and the state match
        // 4th case is that the user did not accept and state no match 
        // case 3 and 4 can be bundled up into of if() block because either way user declined

        // check to see if the state that was passed in matches state 
       return new RedirectView("http://localhost:3000/loggedin");

    }

    
}
