package com.suraj.pocketguide.Models;

/**
 * Created by Suraj on 12/26/2016.
 */

public class SpeedDialModel {
    int bookmarkId       ;
    String bookmarkLink  ;
    String bookmarkType  ;// s for speedial , B for bookmark , A for add site
    String bookmarkImage ;
    String bookmarkDate  ;

    public void setBookmarkId(int bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    public void setBookmarkLink(String bookmarkLink) {
        this.bookmarkLink = bookmarkLink;
    }

    public void setBookmarkType(String bookmarkType) {
        this.bookmarkType = bookmarkType;
    }

    public void setBookmarkImage(String bookmarkImage) {
        this.bookmarkImage = bookmarkImage;
    }

    public void setBookmarkDate(String bookmarkDate) {
        this.bookmarkDate = bookmarkDate;
    }

    public int getBookmarkId() {

        return bookmarkId;
    }

    public String getBookmarkLink() {
        return bookmarkLink;
    }

    public String getBookmarkType() {
        return bookmarkType;
    }

    public String getBookmarkImage() {
        return bookmarkImage;
    }

    public String getBookmarkDate() {
        return bookmarkDate;
    }
}
