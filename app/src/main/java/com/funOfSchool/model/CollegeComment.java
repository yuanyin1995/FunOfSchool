package com.funOfSchool.model;

/**
 * Created by asus1 on 2016/12/22.
 */
public class CollegeComment {
    private String comment;
    private int score;
    private String userId;
    private String commentPicUrl;
    private String commentDate;
    private String userName;

    public CollegeComment(String comment, int score, String userId, String commentPic, String commentDate, String userName) {
        this.comment = comment;
        this.score = score;
        this.userId = userId;
        this.commentPicUrl = commentPic;
        this.commentDate = commentDate;
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getCommentPic() {
        return commentPicUrl;
    }

    public void setCommentPic(String commentPic) {
        this.commentPicUrl = commentPic;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommentPicUrl() {
        return commentPicUrl;
    }

    public void setCommentPicUrl(String commentPicUrl) {
        this.commentPicUrl = commentPicUrl;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return comment+score;
    }
}
