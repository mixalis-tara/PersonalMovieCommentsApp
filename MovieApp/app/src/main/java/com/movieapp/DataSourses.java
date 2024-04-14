package com.movieapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DataSourses {

    private SQLiteHelper dbHelper;

    private SQLiteDatabase database;

    private String[] allFilmsColums = {"id", "filmtitle", "filmcategory", "filmcomments", "filmdate"};


    public DataSourses(Context context) { dbHelper = new SQLiteHelper(context);}

    public void open() {database = dbHelper.getWritableDatabase(); }

    public void close() { dbHelper.close(); }

    public ArrayList<Films> getAllFilms(){

    ArrayList<Films> films = new ArrayList<>();

    Cursor cursor = database.query("films", allFilmsColums, null, null, null, null, null);

    cursor.moveToFirst();

    while (!cursor.isAfterLast()){
        Films film = new Films();
        film.setId(cursor.getInt(0));
        film.setFilmtitle(cursor.getString(1));
        film.setFilmcategory(cursor.getString(2));
        film.setFilmcomments(cursor.getString(3));
        film.setFilmdate(cursor.getString(4));
    films.add(film);
    cursor.moveToNext();


    }
        cursor.close();
        return films;
    }

    public void addFilm(String title, String category, String comments, String filmDate) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("filmtitle", title);
        values.put("filmcategory", category);
        values.put("filmcomments", comments);
        values.put("filmdate", filmDate);

        database.insert("films", null, values);
        database.close();
    }
    public ArrayList<String> getAllCategories() {
        ArrayList<String> categories = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT DISTINCT filmcategory FROM films", null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String category = cursor.getString(cursor.getColumnIndex("filmcategory"));
                categories.add(category);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return categories;
    }
    public ArrayList<Films> getFilmsByCategory(String category) {
        ArrayList<Films> filmsByCategory = new ArrayList<>();
        Cursor cursor = database.query("films", allFilmsColums, "filmcategory=?", new String[]{category}, null, null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Films film = cursorToFilm(cursor);
                filmsByCategory.add(film);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return filmsByCategory;
    }
    public Films getFilmByTitle(String title) {
        Films film = null;
        Cursor cursor = database.query("films", allFilmsColums, "filmtitle=?", new String[]{title}, null, null, null);
        try {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()){
                film = cursorToFilm(cursor);
            }
        } finally {
            cursor.close();
        }
        return film;
    }


    private Films cursorToFilm(Cursor cursor) {
        Films film = new Films();
        film.setId(cursor.getInt(cursor.getColumnIndex("id")));
        film.setFilmtitle(cursor.getString(cursor.getColumnIndex("filmtitle")));
        film.setFilmcategory(cursor.getString(cursor.getColumnIndex("filmcategory")));
        film.setFilmcomments(cursor.getString(cursor.getColumnIndex("filmcomments")));
        film.setFilmdate(cursor.getString((cursor.getColumnIndex("filmdate"))));

        return film;
    }

    public void deleteFilmById(String title) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete("films", "filmtitle=?", new String[]{title});
        database.close();
    }

    public void clearFilmComments(String title){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("filmcomments", "");
        database.update("films", values, "filmtitle=?", new String[]{title});
        database.close();
    }

    public void updateFilmComments(String filmTitle, String newComment) {
        ContentValues values = new ContentValues();
        values.put("filmcomments", newComment);

        database.update("films", values, "filmtitle=?", new String[]{filmTitle});
    }






}
