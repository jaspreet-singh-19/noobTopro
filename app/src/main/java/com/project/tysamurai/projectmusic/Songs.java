package com.project.tysamurai.projectmusic;

/**
 * Created by tanay on 8/4/17.
 */

public class Songs {
    private long id;
    private String title;
    private String artist;

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Songs(long id, String title, String artist) {
        this.id = id;
        this.title = title;
        this.artist = artist;
    }
}
