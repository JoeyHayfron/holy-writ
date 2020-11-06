package com.engineerskasa.holywrit;

public class Book {
    private int index;
    private int order;
    private String caption;
    private String name;
    private String author;
    private String Testament;
    private String genre;
    private int chapters_no;
    private int bible_FK;

    public Book() {
    }

    public Book(int index, int order, String caption, String name, String author, String testament, String genre, int chapters_no, int bible_FK) {
        this.index = index;
        this.order = order;
        this.caption = caption;
        this.name = name;
        this.author = author;
        Testament = testament;
        this.genre = genre;
        this.chapters_no = chapters_no;
        this.bible_FK = bible_FK;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
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
        return Testament;
    }

    public void setTestament(String testament) {
        Testament = testament;
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

    public int getBible_FK() {
        return bible_FK;
    }

    public void setBible_FK(int bible_FK) {
        this.bible_FK = bible_FK;
    }
}
