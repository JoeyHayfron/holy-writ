package com.engineerskasa.holywrit;

public class Bible {

    private int index;
    private String name;
    private String edition;
    private int books_no;

    public Bible() {
    }

    public Bible(int index, String name, String edition, int books_no) {
        this.index = index;
        this.name = name;
        this.edition = edition;
        this.books_no = books_no;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public int getBooks_no() {
        return books_no;
    }

    public void setBooks_no(int books_no) {
        this.books_no = books_no;
    }
}
