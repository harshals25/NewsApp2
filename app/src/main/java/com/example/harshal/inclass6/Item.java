package com.example.harshal.inclass6;

/**
 * Created by Harshal on 2/24/2018.
 */

public class  Item {

    String title, description, link, pubDate;
    MediaGroup imageUrl;

    @Override
    public String toString() {
        return "Item{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", imageUrl=" + imageUrl +
                '}';
    }
}
