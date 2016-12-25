package com.funOfSchool.model;

/**
 * Created by lenovo on 2016/12/14.
 */

public class TravelItem {
    private String userAvatarUrl;
    private String guideAvatarUrl;
    private String userId;
    private String guiderId;
    private String travelCollege;
    private String travelDate;
    private Boolean travelFlag;

    public TravelItem(String userAvatarUrl, String guideAvatarUrl, String userId, String guiderId, String travelCollege, String travelDate, Boolean travelFlag) {
        this.userAvatarUrl = userAvatarUrl;
        this.guideAvatarUrl = guideAvatarUrl;
        this.userId = userId;
        this.guiderId = guiderId;
        this.travelCollege = travelCollege;
        this.travelDate = travelDate;
        this.travelFlag = travelFlag;
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

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public String getGuideAvatarUrl() {
        return guideAvatarUrl;
    }

    @Override
    public String toString() {
        return userId+guiderId+travelCollege+travelDate+travelFlag+"!!!";
    }
}
