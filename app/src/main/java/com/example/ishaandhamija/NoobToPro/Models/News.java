package com.example.ishaandhamija.NoobToPro.Models;

import java.util.ArrayList;

/**
 * Created by tanay on 21/4/17.
 */

public class News {
    public String source,status;
    public ArrayList<articles> articles;

    public String getSource() {
        return source;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<articles> getArticles() {
        return articles;
    }

    public News(String source, String status, ArrayList<articles> articles) {

        this.source = source;
        this.status = status;
        this.articles = articles;
    }
}
