package com.suraj.pocketguide.Models;

/**
 * Created by Suraj on 12/28/2016.
 */

public class SearchRankModel {
    int sreachStringID         ;
    String sreachString        ;
    String sreachStringRank    ;

    public void setSreachStringID(int sreachStringID) {
        this.sreachStringID = sreachStringID;
    }

    public void setSreachString(String sreachString) {
        this.sreachString = sreachString;
    }

    public void setSreachStringRank(String sreachStringRank) {
        this.sreachStringRank = sreachStringRank;
    }

    public String getSreachString() {

        return sreachString;
    }

    public String getSreachStringRank() {
        return sreachStringRank;
    }

    public int getSreachStringID() {

        return sreachStringID;
    }
}
