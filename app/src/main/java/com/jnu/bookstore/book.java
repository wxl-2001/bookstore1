package com.jnu.bookstore;

public class book {

    private  int CoverResourceId;

    public void setTitle(String title) {
        Title = title;
    }

    private String Title;

    public book(String name, int pictureId) {
        this.CoverResourceId=pictureId;
        this.Title=name;
    }

    public int getCoverResourceId() {
        return CoverResourceId;
    }

    public String getTitle() {
        return Title;
    }
}
