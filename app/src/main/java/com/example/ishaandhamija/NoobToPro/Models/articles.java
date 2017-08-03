package com.example.ishaandhamija.NoobToPro.Models;

/**
 * Created by tanay on 21/4/17.
 */

public class articles {

    public String author;
    public String title;
    public  String description;
    public String urlToImage;
    public String url;

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getUrl() {
        return url;
    }

    public articles(String author, String title, String description, String urlToImage, String url) {

        this.author = author;
        this.title = title;
        this.description = description;
        this.urlToImage = urlToImage;
        this.url = url;
    }
}
