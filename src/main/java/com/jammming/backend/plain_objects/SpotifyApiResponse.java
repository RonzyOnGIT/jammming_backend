package com.jammming.backend.plain_objects;

import com.jammming.backend.plain_objects.Tracks;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotifyApiResponse {
    
    private Tracks tracks;


}   
