package com.engineerskasa.holywrit;

public class BookInfo {

    int id;
    String caption;
    String name;
    String author;
    String testament;
    String genre;
    int chapters_no;

    public BookInfo() {
    }

    public BookInfo(int id, String caption, String name, String author, String testament, String genre, int chapters_no) {
        this.id = id;
        this.caption = caption;
        this.name = name;
        this.author = author;
        this.testament = testament;
        this.genre = genre;
        this.chapters_no = chapters_no;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTestament() {
        return testament;
    }

    public void setTestament(String testament) {
        this.testament = testament;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getChapters_no() {
        return chapters_no;
    }

    public void setChapters_no(int chapters_no) {
        this.chapters_no = chapters_no;
    }
}
