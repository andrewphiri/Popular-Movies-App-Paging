package com.example.popularmoviesapppaging;

public class Reviews {

    String content;
    String author;
    String date;

    public Reviews(String content, String author, String date) {
        this.content = content;
        this.author = author;
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }
}
