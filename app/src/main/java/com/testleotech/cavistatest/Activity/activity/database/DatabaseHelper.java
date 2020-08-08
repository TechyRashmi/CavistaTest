package com.testleotech.cavistatest.Activity.activity.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.testleotech.cavistatest.Activity.activity.model.CommentModel;

import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String KEY_ID = "id";
    // Database Name
    private static final String DATABASE_NAME = "CavistaDatabase";
    //tables
    public static final String TABLE_COMMENT = "table_comment";

    //table columns
    public static final String image_id = "image_id";
    public static final String comment = "comment";

    //======


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        try
        {
            String CREATE_COMMENT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_COMMENT + "("
                    + KEY_ID + " INTEGER PRIMARY KEY," + image_id + " TEXT,"
                    + comment + " TEXT" + ")";

            db.execSQL(CREATE_COMMENT_TABLE);


        }catch (Exception e)
        {
            Log.i("create Database",e.getMessage().toString());
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENT);
        // Create tables again
        onCreate(db);

    }
    public void InsertComment(String id, String comments) {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
                values.put(image_id, id);
                    values.put(comment, comments);

                    Log.e("values",""+values);
            // Inserting Row
            db.insert(TABLE_COMMENT, null, values);
            Log.e("values",""+values);
            Log.i("", "File added successful.");
            db.close(); // Closing database connection


        }
        catch (Exception e){

            Log.e("exc",e.toString());
        }

    }






    public  boolean checkRecordExist(String tableName, String columnName, String uniqueId) {
        boolean status = false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(tableName, null, columnName + "='" + uniqueId + "'", null, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                status = true;
            }
            cursor.close();
        }
        return status;
    }




    public  long insertUpdateData(ContentValues values, String tableName, String columnName, String uniqueId) {

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (checkRecordExist(tableName, columnName, uniqueId)) {
                Log.e("database_insert1",""+values);
                return (long) db.update(tableName, values, columnName + "='" + uniqueId + "'", null);


            }
            Log.e("database_insert2",""+values);
            return db.insert(tableName, null, values);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }





    //fetching data from ID
    public String fetchAllDataSpecificColumn(String columnName ,String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + columnName + " FROM " + TABLE_COMMENT + " WHERE image_id " + "= '" + id + "'", null);
        ArrayList<String> arrayList = new ArrayList();
        String getData = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    getData=(cursor.getString(0));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return getData;

    }

    }

