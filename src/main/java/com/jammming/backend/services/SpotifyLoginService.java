package com.jammming.backend.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.web.servlet.view.RedirectView;


@Service
public class SpotifyLoginService {

    @Value("${clientId}")
    private String clientId;
    
    // builds url and returns string url so the front-end can redirect users to spotify's auth page
    public RedirectView getLoginUrl() {

        String fallbackUrl = "http://localhost:5173/";

        String responseType = "code";

        String scope = "playlist-modify-public playlist-modify-private user-read-private";
    
        UUID uniqueId = UUID.randomUUID();

        // state will be for added security when redirecting user and back
        String state = uniqueId.toString();

        String loginUrl = "https://accounts.spotify.com/authorize?response_type=" + responseType + "&client_id=" + clientId + "&scope=" + scope + "&redirect_uri=" + fallbackUrl + "&state=" + state;

        return new RedirectView(loginUrl);
    }

}
