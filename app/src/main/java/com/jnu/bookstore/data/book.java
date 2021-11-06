package com.jnu.bookstore.data;

import java.io.Serializable;

public class book implements Serializable {
    private  int CoverResourceId;
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

    public void setTitle(String title) {
        Title = title;
    }
    public void setCoverResourceId(int coverResourceId) {
        this.CoverResourceId = coverResourceId;
    }
}
