package com.example.user.googlemapstest;

import org.w3c.dom.ProcessingInstruction;

import java.util.ArrayList;

/**
 * Created by User on 2016/3/23.
 */
public class DataObject {
    private String title,content,date;
    private int ViewType;

    // constructor
    DataObject(String title, String content, String date, int ViewType) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.ViewType = ViewType;
    }

    public String getTitle() {
        return title;
    }

    public void setTittle(String tittle) {
        this.title = tittle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public int getViewType() {
        return ViewType;
    }

    void setViewType(int ViewType) {
        this.ViewType = ViewType;
    }

    private ArrayList<DataObject> getDataSet() {
        ArrayList<DataObject> results = new ArrayList<>();
        for(int index = 0; index < 36; index++) {
            DataObject dataObject = new DataObject("Title." + index, "Content " + index, "2016/4/26", index%3);
            results.add(index, dataObject);
        }
        return results;
    }
}
