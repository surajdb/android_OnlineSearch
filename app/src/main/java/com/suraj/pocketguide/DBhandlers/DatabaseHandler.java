package com.suraj.pocketguide.DBhandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.suraj.pocketguide.Models.ScreenshotModel;
import com.suraj.pocketguide.Models.SearchRankModel;
import com.suraj.pocketguide.Models.SpeedDialModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Suraj on 12/5/2016.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME       = "pocket_guide";

    // notes table name
    private static final String TABLE_IMAGE        = "sreenshots";
    private static final String TABLE_BOOKMARK     = "bookmark";
    private static final String TABLE_SEARCHRANK   = "search_rank";
    // Notes Table Columns names
    private static final String kScreenshotId                 = "sreenshot_id";
    private static final String kScreenshotLink                 = "sreenshot_link";
    private static final String kScreenshotDesc                 = "sreenshot_desc";
    private static final String kScreenshotDate                 = "sreenshot_date";

    private static final String kBookmarkId                 = "bookmark_id";
    private static final String kBookmarkLink                 = "bookmark_link";
    private static final String kBookmarkType                  = "bookmark_type";
    private static final String kBookmarkImage               = "bookmark_image";
    private static final String kBookmarkDate                = "bookmark_date";

    private static final String kSearchStringID             = "sreach_string_id";
    private static final String kSearchString               = "sreach_string";
    private static final String kSearchStringRank           = "sreach_string_rank";
////
    private static final String LOG = "PocketGuide Log" ;


    public DatabaseHandler(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);// null here checks if the database isalready cerated then it will skip the database creation step.
    }

    // Creating Tables
    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm aaa");
    String currentDateandTime = sdf.format(new Date());

    String CREATE_TABLE_IMAGE = "CREATE TABLE " + TABLE_IMAGE + "("
            + kScreenshotId + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + kScreenshotLink + " TEXT,"
            + kScreenshotDesc + " TEXT,"
            + kScreenshotDate + " TEXT" + ")";

    String CREATE_TABLE_BOOKMARK = "CREATE TABLE " + TABLE_BOOKMARK + "("
            + kBookmarkId + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + kBookmarkLink + " TEXT ,"
            + kBookmarkType + " TEXT ,"
            + kBookmarkImage + " TEXT ,"
            + kBookmarkDate + " TEXT" + ")";

    String CREATE_TABLE_SEARCHRANK = "CREATE TABLE " + TABLE_SEARCHRANK + "("
            + kSearchStringID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + kSearchString + " TEXT,"
            + kSearchStringRank + " TEXT" + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_IMAGE);
        db.execSQL(CREATE_TABLE_BOOKMARK);
        db.execSQL(CREATE_TABLE_SEARCHRANK);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKMARK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEARCHRANK);
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations==============================================================================================================================
     */
// CRUD for SpeedDial which is also a bookmark
   public void addBookMark(SpeedDialModel speedial) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(kBookmarkLink, speedial.getBookmarkLink());
        values.put(kBookmarkType, speedial.getBookmarkType());
        values.put(kBookmarkImage, speedial.getBookmarkImage());
        values.put(kBookmarkDate, currentDateandTime);

        // Inserting Row
        db.insert(TABLE_BOOKMARK, null, values);
        db.close();
    }

    public void deleteBookMark(int bookmark_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        db.delete(TABLE_BOOKMARK, kBookmarkId + " = " + bookmark_id, null);
        db.close();
    }

    public void updateBookMark(SpeedDialModel speedDial) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(kBookmarkLink, speedDial.getBookmarkLink()); // Notes Desc

        db.update(TABLE_BOOKMARK, values, kBookmarkId + " = ?",
                new String[] { String.valueOf(speedDial.getBookmarkId()) });
        db.close(); // Closing database connection
    }

    public List<SpeedDialModel> getAllSpeedDial(String type) {
        List<SpeedDialModel> speedDialList = new ArrayList<SpeedDialModel>();
        // Select All Query
        String selectQuery = "SELECT  "+kBookmarkId +" , "
                                      +kBookmarkLink +" , "
                                      +kBookmarkDate
                + " FROM " + TABLE_BOOKMARK + " WHERE "+kBookmarkType+ " = '"+type+"'" ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SpeedDialModel speedDial = new SpeedDialModel();
                speedDial.setBookmarkId(Integer.parseInt(cursor.getString(0)));
                speedDial.setBookmarkLink(cursor.getString(1).toString());
                speedDial.setBookmarkDate(cursor.getString(2).toString());

                speedDialList.add(speedDial);
            } while (cursor.moveToNext());
        }
        // return contact list
        return speedDialList;
    }

    public SpeedDialModel getSpeedDial(int bookmark_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_BOOKMARK + " WHERE "
                + kBookmarkId + " = " + bookmark_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();
        SpeedDialModel speedDial = new SpeedDialModel();
        speedDial.setBookmarkId(c.getInt(c.getColumnIndex(kBookmarkId)));
        speedDial.setBookmarkLink(c.getString(c.getColumnIndex(kBookmarkLink)));
        speedDial.setBookmarkDate(c.getString(c.getColumnIndex(kBookmarkDate)));
        return speedDial;
    }


    //CRUD for all the screenshots=====================================================================================================================================================
    public void addScreenShot(ScreenshotModel screenShot) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
      //  values.put(kScreenshotId, screenShot.getSreenshotId());
        values.put(kScreenshotLink, screenShot.getSreenshotLink());
        values.put(kScreenshotDate,currentDateandTime);
        //values.put(kScreenshotDesc, currentDateandTime);

        // Inserting Row
        db.insert(TABLE_IMAGE, null, values);
        db.close();
    }

    public void deleteScreenShot(int bookmark_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        db.delete(TABLE_IMAGE, kScreenshotId + " = " + bookmark_id, null);
        db.close();
    }


    public List<ScreenshotModel> getAllScreenshots() {
        List<ScreenshotModel> screenShotModelList = new ArrayList<ScreenshotModel>();
        // Select All Query
        String selectQuery = "SELECT  "+kScreenshotId +" , "
                +kScreenshotLink +" , "
                +kScreenshotDate
                + " FROM " + TABLE_IMAGE ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ScreenshotModel screenShot = new ScreenshotModel();
                screenShot.setSreenshotId(Integer.parseInt(cursor.getString(0)));
                screenShot.setSreenshotLink(cursor.getString(1).toString());
                screenShot.setSreenshotDate(cursor.getString(2).toString());
                screenShotModelList.add(screenShot);
            } while (cursor.moveToNext());
        }
        // return contact list
        return screenShotModelList;
    }

    public ScreenshotModel getScreenShot(int screenShot_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_IMAGE + " WHERE "
                + kScreenshotId + " = " + screenShot_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();
        ScreenshotModel screenShot = new ScreenshotModel();
        screenShot.setSreenshotId(c.getInt(c.getColumnIndex(kScreenshotId)));
        screenShot.setSreenshotLink(c.getString(c.getColumnIndex(kScreenshotLink)));
        screenShot.setSreenshotDate(c.getString(c.getColumnIndex(kScreenshotDate)));
        return screenShot;
    }

    // CRUD for SpeedDial which is also a bookmark=====================================================================================================================================================
    public void addSearchRank(SearchRankModel searchRank) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(kSearchStringID, searchRank.getSreachStringID());
        values.put(kSearchString, searchRank.getSreachString());
        values.put(kSearchStringRank, searchRank.getSreachStringRank());

        // Inserting Row
        db.insert(TABLE_SEARCHRANK, null, values);
        db.close();
    }

    public List<SearchRankModel> getAllSearchRank() {
        List<SearchRankModel> searchRankList = new ArrayList<SearchRankModel>();
        // Select All Query
        String selectQuery = "SELECT  "+kSearchStringID +" , "
                +kSearchString +" , "
                +kSearchStringRank +" , "
                + " FROM " + TABLE_SEARCHRANK  ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SearchRankModel searchRank = new SearchRankModel();
                searchRank.setSreachStringID(Integer.parseInt(cursor.getString(0)));
                searchRank.setSreachString(cursor.getString(1).toString());
                searchRank.setSreachStringRank(cursor.getString(2).toString());

                searchRankList.add(searchRank);
            } while (cursor.moveToNext());
        }
        // return contact list
        return searchRankList;
    }

}
