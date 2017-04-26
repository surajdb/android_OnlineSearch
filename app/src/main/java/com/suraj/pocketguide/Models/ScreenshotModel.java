package com.suraj.pocketguide.Models;

/**
 * Created by Suraj on 12/28/2016.
 */

public class ScreenshotModel {

    int sreenshotId ;
    String sreenshotLink;
    String sreenshotDesc;
    String sreenshotDate;

    public void setSreenshotId(int sreenshotId) {
        this.sreenshotId = sreenshotId;
    }

    public void setSreenshotLink(String sreenshotLink) {
        this.sreenshotLink = sreenshotLink;
    }

    public void setSreenshotDesc(String sreenshotDesc) {
        this.sreenshotDesc = sreenshotDesc;
    }

    public void setSreenshotDate(String sreenshotDate) {
        this.sreenshotDate = sreenshotDate;
    }

    public int getSreenshotId() {

        return sreenshotId;
    }

    public String getSreenshotLink() {
        return sreenshotLink;
    }

    public String getSreenshotDesc() {
        return sreenshotDesc;
    }

    public String getSreenshotDate() {
        return sreenshotDate;
    }

}
