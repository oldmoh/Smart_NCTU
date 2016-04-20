package com.example.user.googlemapstest;

/**
 * Created by User on 2016/3/23.
 */
public class DataObject {
    private String tittle,content;

    // constructor
    DataObject(String tittle, String content) {
        this.tittle = tittle;
        this.content = content;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
