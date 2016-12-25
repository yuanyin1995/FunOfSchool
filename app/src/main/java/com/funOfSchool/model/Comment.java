package com.funOfSchool.model;

/**
 * Created by asus1 on 2016/12/22.
 */
public class Comment {
    private String userImage;
    private String userName;
    private String commentDate;
    private int score;
    private String comment;

    public Comment(String userImage, String userName,
                   String commentDate, int score, String comment) {
        this.userImage = userImage;
        this.userName = userName;
        this.commentDate = commentDate;
        this.score = score;
        this.comment = comment;
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

    @Override
    public String toString() {
        return userName+"-"+comment+"-"+score+"-"+userImage;
    }
}
