package com.jammming.backend.plain_objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Playlist {

    private String id;
    private String name;
    private String description;
    
}
