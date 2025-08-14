package com.jammming.backend.plain_objects;

import com.jammming.backend.plain_objects.Artist;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Song {

    private String name;
    private List<Artist> artists;
    private String preview_url;
    private String id; // this is for frontend to use the id of song for react component
    private String uri;
    
}
// name={song.name} artist={song.artists} key={song.id} preview={song.preview_url} 
// index={index} addSongToPlaylist={addSongToPlaylist} id={song.id} removeSongFromPlaylist={removeFromPlaylist} playlistSongs={playlistSongs} uri={song.uri}
