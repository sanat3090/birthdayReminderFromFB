package com.sanat.birthdayreminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sanat.birthdayreminder.firendList.FriendModel;
import com.sanat.birthdayreminder.utils.Constants;

import java.util.ArrayList;

/**
 * Created by healthcaremagic on 3/4/2017.
 */

public class MyDataBase {
    TableMgmtHelper tableMgmtHelper;
    SQLiteDatabase db1;
    public MyDataBase(Context context) {
        try {
            tableMgmtHelper = new TableMgmtHelper(context);
            db1 = tableMgmtHelper.getWritableDatabase();
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }


    public class TableMgmtHelper extends SQLiteOpenHelper {
        //TODO: TABLEMGMT_TABLE table creation;
        public Context context;
        public static final String FRND_TABLE_NAME = "FRND_TABLE_NAME";
        public static final String FRND_NAME = "FRND_NAME";
        public static final String FRND_ID = "FRND_ID";
        public static final String FRND_PICTURE = "FRND_PICTURE";
        public static final String displayDateOfBirth = "displayDateOfBirth";
        public static final String actualFacebookDate = "actualFacebookDate";
        public static final String dateInMill = "dateInMill";

        public static final String CREATE_FRND_TABLE = "CREATE TABLE IF NOT EXISTS " + FRND_TABLE_NAME +
                "(" + FRND_ID + " VARCHAR(255) PRIMARY KEY,"
                + FRND_NAME + "  VARCHAR(255),"
                + FRND_PICTURE + " VARCHAR(255),"
                + displayDateOfBirth + " VARCHAR(255),"
                + actualFacebookDate + "  VARCHAR(255),"
                + dateInMill + " VARCHAR(255));";
        public static final String DROP_FRND_TABLE = "DROP TABLE IF EXISTS " + FRND_TABLE_NAME;

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_FRND_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_FRND_TABLE);
        }
        public TableMgmtHelper(Context context) {
            super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
            this.context = context;
        }

    }
    public long insertInFrndTable(String name, String id,String birthday,String picture,String displayDateOfBirth,String dateInMilli) {
        long eid = 0;
        try {
            SQLiteDatabase database = tableMgmtHelper.getWritableDatabase();
            ContentValues contentValue = new ContentValues();
            contentValue.put(TableMgmtHelper.FRND_NAME, name);
            contentValue.put(TableMgmtHelper.FRND_ID, id);
            contentValue.put(TableMgmtHelper.FRND_PICTURE, picture);
            contentValue.put(TableMgmtHelper.displayDateOfBirth, displayDateOfBirth);
            contentValue.put(TableMgmtHelper.actualFacebookDate, birthday);
            contentValue.put(TableMgmtHelper.dateInMill, dateInMilli);
            eid = database.insert(TableMgmtHelper.FRND_TABLE_NAME, null, contentValue);

        } catch (Exception e) {

        }
        return eid;
    }

    public ArrayList<FriendModel> getFrndList() {
        ArrayList<FriendModel> myContactDataArrayList = null;
        try {
            myContactDataArrayList = new ArrayList<FriendModel>();
            FriendModel data;
            SQLiteDatabase db = tableMgmtHelper.getWritableDatabase();
            if (db != null) {
                String[] columns = {TableMgmtHelper.FRND_NAME, TableMgmtHelper.FRND_ID, TableMgmtHelper.dateInMill, TableMgmtHelper.FRND_PICTURE, TableMgmtHelper.displayDateOfBirth, TableMgmtHelper.actualFacebookDate};
                Cursor cursor = db.query(TableMgmtHelper.FRND_TABLE_NAME,
                        columns,
                        null, null, null, null, null);

                while (cursor.moveToNext()) {
                    data = new FriendModel();

                    int c1 = cursor.getColumnIndex(TableMgmtHelper.actualFacebookDate);
                    String publicUserName = cursor.getString(c1);
                    data.setActualFacebookDate(publicUserName);

                    int c2 = cursor.getColumnIndex(TableMgmtHelper.FRND_NAME);
                    String name = cursor.getString(c2);
                    data.setName(name);

                    int c3 = cursor.getColumnIndex(TableMgmtHelper.FRND_ID);
                    String id = cursor.getString(c3);
                    data.setId(id);

                    int c4 = cursor.getColumnIndex(TableMgmtHelper.dateInMill);
                    String mill = cursor.getString(c4);
                    data.setDateInMill(Long.valueOf(mill));

                    int c5 = cursor.getColumnIndex(TableMgmtHelper.FRND_PICTURE);
                    String picture = cursor.getString(c5);
                    data.setImageUrl(picture);

                    int c6 = cursor.getColumnIndex(TableMgmtHelper.displayDateOfBirth);
                    String displayDate = cursor.getString(c6);
                    data.setdisplayDateOfBirth(displayDate);

                    int c7 = cursor.getColumnIndex(TableMgmtHelper.actualFacebookDate);
                    String actualBirthday = cursor.getString(c7);
                    data.setActualFacebookDate(actualBirthday);

                    myContactDataArrayList.add(data);
                }
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return myContactDataArrayList;
    }

    public int getFrndListCount() {
        int count = 0;
        try {
            SQLiteDatabase db = tableMgmtHelper.getWritableDatabase();
            if (db != null) {
                String[] columns = {TableMgmtHelper.FRND_NAME, TableMgmtHelper.FRND_ID, TableMgmtHelper.dateInMill, TableMgmtHelper.FRND_PICTURE, TableMgmtHelper.displayDateOfBirth, TableMgmtHelper.actualFacebookDate};
                Cursor cursor = db.query(TableMgmtHelper.FRND_TABLE_NAME,
                        columns,
                        null, null, null, null, TableMgmtHelper.dateInMill+ " ASC");
                count = cursor.getCount();
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return count;
    }

    public int deleteAllFrndList() {
        int count = 0;
        try {
            SQLiteDatabase db = tableMgmtHelper.getWritableDatabase();
            count = db.delete(TableMgmtHelper.FRND_TABLE_NAME, null, null);
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return count;
    }
}
