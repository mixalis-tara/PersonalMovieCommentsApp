package com.movieapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper  extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "films_db.db";

    private static final int DATABASE_VERSION = 17;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null,
                DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE films (" +
                "id integer primary key autoincrement," +
                "filmtitle text," +
                "filmcategory text," +
                "filmcomments text," +
                "filmdate text )");

         insertDefaultCategories(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void insertDefaultCategories(SQLiteDatabase db) {
        String[] defaultCategories = {"Choose a category", "Action", "Adventure", "Comedy", "Drama", "Horror" ,"Romance", "Sci-Fi", "Thriller"};

        for (String category : defaultCategories) {
            ContentValues values = new ContentValues();
            values.put("filmcategory", category);
            db.insert("films", null, values);
        }
    }
}

