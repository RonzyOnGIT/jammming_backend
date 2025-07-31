package com.jammming.backend.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class SpotifyLoginService {
    
    // builds url and returns string url so the front-end can redirect users to spotify's auth page
    public String getLoginUrl() {

        // http://localhost:5173/ - fallback url

        // response_type = 'code'

        // scope = 'playlist-modify-public playlist-modify-private user-read-private';

        // state = uuid
    
        UUID uniqueId = UUID.randomUUID();
        String stringId = uniqueId.toString();

        return stringId;
    }

}
