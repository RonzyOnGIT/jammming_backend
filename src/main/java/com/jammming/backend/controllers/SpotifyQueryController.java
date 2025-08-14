package com.jammming.backend.controllers;

import com.jammming.backend.services.SpotifyQueryService;
import com.jammming.backend.plain_objects.SpotifyApiResponse;

import java.io.Serializable;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// this class handles all queries to make calls to spotify like fetching songs, playlists, etc
@RestController
@RequestMapping("/api/query")
public class SpotifyQueryController {

    private final SpotifyQueryService spotifyQueryService;

    public SpotifyQueryController(SpotifyQueryService spotifyQueryService) {
        this.spotifyQueryService = spotifyQueryService;
    }
    
    // the frontend will make a call passing in the id, as well as the song name, so have to do that
    // here want to return a json object {} with the fields that we care about being :
    // name={song.name} artist={song.artists} key={song.id} preview={song.preview_url} index={index} addSongToPlaylist={addSongToPlaylist} id={song.id} removeSongFromPlaylist={removeFromPlaylist} playlistSongs={playlistSongs} uri={song.uri}
    @GetMapping("songs/")
    public ResponseEntity<Map<String, Serializable>> fetchSongs(@RequestParam(value = "id", required = true) Long id, @RequestParam(value = "songName", required = true) String songName) {

        SpotifyApiResponse response = this.spotifyQueryService.fetchSongs(id, songName);


        if (response == null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "userId", null, "expireTime", null));
        }


        return new ResponseEntity<>(null);

        // https://api.spotify.com/v1/search?q=${songName}&type=track&limit=6

        // return ResponseEntity.ok(Map.of("name", response.getTracks().getItems(), "artists", res.get("userId"), "preview_url", res.get("expireTime"), "id", "id", "uri", "uri"));
    }



}
