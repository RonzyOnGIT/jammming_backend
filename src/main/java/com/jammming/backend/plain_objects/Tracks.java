package com.jammming.backend.plain_objects;

import com.jammming.backend.plain_objects.Song;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tracks {

    private List<Song> items;
    
}
