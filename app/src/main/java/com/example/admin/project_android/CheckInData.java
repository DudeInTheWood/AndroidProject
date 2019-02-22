package com.example.admin.project_android;

import android.support.v7.widget.RecyclerView;

public class CheckInData
{


    private String key, comment;
    public CheckInData() {
        // non arg
    }

    public CheckInData(String key,String comment) {
        this.key = key;
        this.comment = comment;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }
}
