package com.example.user.googlemapstest;

import org.w3c.dom.ProcessingInstruction;

/**
 * Created by User on 2016/3/23.
 */
public class DataObject {
    private String tittle,content;
    private int ViewType;

    // constructor
    DataObject(String tittle, String content, int ViewType) {
        this.tittle = tittle;
        this.content = content;
        this.ViewType = ViewType;
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

    public int getViewType() {
        return ViewType;
    }

    void setViewType(int ViewType) {
        this.ViewType = ViewType;
    }
}
