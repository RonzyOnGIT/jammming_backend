package com.jammming.backend.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.web.servlet.view.RedirectView;


@Service
public class SpotifyLoginService {

    @Value("${clientId}")
    private String clientId;
    
    // returns an array where first element is the state id and second element is the url to direct user to login to spotify account
    public String getLoginUrl(String state) {

        String fallbackUrl = "http://localhost:5173/";

        String responseType = "code";

        String scope = "playlist-modify-public playlist-modify-private user-read-private";

        // state will be passed down to user, when a user gets to the login page will be sent the state, then once the user comes back from the redirect to fallback, check that the state matches, if it does not match, means there was some interception in between, and do not grant access to user
        String loginUrl = "https://accounts.spotify.com/authorize?response_type=" + responseType + "&client_id=" + clientId + "&scope=" + scope + "&redirect_uri=" + fallbackUrl + "&state=" + state;

        return loginUrl;

    }

    public String getStateId() {

        UUID uniqueId = UUID.randomUUID();
        String state = uniqueId.toString();

        return state;
        
    }

}
