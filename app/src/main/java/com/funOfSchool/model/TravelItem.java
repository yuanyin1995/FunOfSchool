package com.funOfSchool.model;

import android.media.Image;

import java.util.Date;

/**
 * Created by lenovo on 2016/12/14.
 */

public class TravelItem {
    private String userId;
    private String guiderId;
    private String travelCollege;
    private String travelDate;
    private Boolean travelFlag;

    public TravelItem(String userId, String guiderId, String travelCollege,
                      String travelDate, Boolean travelFlag) {
        this.userId = userId;
        this.guiderId = guiderId;
        this.travelCollege = travelCollege;
        this.travelDate = travelDate;
        this.travelFlag = travelFlag;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGuiderId() {
        return guiderId;
    }

    public void setGuiderId(String guiderId) {
        this.guiderId = guiderId;
    }

    public String getTravelCollege() {
        return travelCollege;
    }

    public void setTravelCollege(String travelCollege) {
        this.travelCollege = travelCollege;
    }

    public String getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(String travelDate) {
        this.travelDate = travelDate;
    }

    public Boolean getTravelFlag() {
        return travelFlag;
    }

    public void setTravelFlag(Boolean travelFlag) {
        this.travelFlag = travelFlag;
    }

    @Override
    public String toString() {
        return userId+guiderId+travelCollege+travelDate+travelFlag+"!!!";
    }
}
