package com.funOfSchool.model;

/**
 * Created by asus1 on 2016/12/22.
 */
public class CollegeComment {
    private String userImage;
    private String userName;
    private String commentDate;
    private int score;
    private String comment;
    private String picUrl;

    public CollegeComment(String userImage, String userName,
                          String commentDate, int score, String comment, String picUrl) {
        this.userImage = userImage;
        this.userName = userName;
        this.commentDate = commentDate;
        this.score = score;
        this.comment = comment;
        this.picUrl = picUrl;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getUserName() {
        return userName;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public int getScore() {
        return score;
    }

    public String getComment() {
        return comment;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    @Override
    public String toString() {
        return userName+"-"+comment+"-"+score+"-"+userImage+"-"+picUrl;
    }
}
