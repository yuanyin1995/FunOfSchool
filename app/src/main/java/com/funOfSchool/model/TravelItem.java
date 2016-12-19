package com.funOfSchool.model;

import android.media.Image;

import java.util.Date;

/**
 * Created by lenovo on 2016/12/14.
 */

public class TravelItem {
    private String avater1;
    private String avater2;
    private String school;
    private String date;
    public TravelItem(String s,String d){
        this.school = s;
        this.date = d;
    }

    public String getSchool() {
        return school;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return school+date;
    }
}
