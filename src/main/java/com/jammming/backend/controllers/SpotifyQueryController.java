package com.jammming.backend.controllers;

import com.jammming.backend.services.SpotifyQueryService;
import com.jammming.backend.plain_objects.Artist;
import com.jammming.backend.plain_objects.SpotifyApiResponse;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    
    // name={song.name} artist={song.artists} key={song.id} preview={song.preview_url} index={index} addSongToPlaylist={addSongToPlaylist} id={song.id} removeSongFromPlaylist={removeFromPlaylist} playlistSongs={playlistSongs} uri={song.uri}
    @GetMapping("/songs")
    public ResponseEntity<List<Map<String, Object>>> fetchSongs(@RequestParam(value = "id", required = true) Long id, @RequestParam(value = "songName", required = true) String songName) {

        SpotifyApiResponse response = this.spotifyQueryService.fetchSongs(id, songName);

        if (response == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Map<String, Object>> simplifiedSongs = response.getTracks().getItems().stream().map(song -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", song.getId());
            map.put("name", song.getName());
            String firstArtist = song.getArtists() != null && !song.getArtists().isEmpty() ? song.getArtists().get(0).getName() : ""; // since artists is an array and only want to return first artist, just get first element from artists array and get the name else empty string
            map.put("artist", firstArtist);
            map.put("preview_url", song.getPreview_url()); // because of deprecated api, now no longer have access to preview_url (null), instead just return external uri to redirect in the future :(
            map.put("uri", song.getUri());
            return map;
        })
        .collect(Collectors.toList());

        return ResponseEntity.ok(simplifiedSongs);

    }

    // want to make a post request here to an endpoint, in which the backend (here) will handle retrieving user's spotify id to use that to get playlist, つまり most of the work is donw here in backend
    @PostMapping("/playlist")
    public ResponseEntity<String> createPlaylist(@RequestBody Map<String, Serializable> payload) {

        String response = this.spotifyQueryService.createPlaylist(payload);

        if (response.equals("ok")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create playlist");
        }
    }
}

