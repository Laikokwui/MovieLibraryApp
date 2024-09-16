package com.example.movieappextension;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MovieDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyDB";
    private static final String TABLE_NAME = "MOVIE";
    private static final String KEY_ID = "ID";
    private static final String KEY_TITLE = "TITLE";
    private static final String KEY_GENRE = "GENRE";
    private static final String KEY_OVERVIEW = "OVERVIEW";
    private static final String KEY_POSTER_PATH = "POSTER_PATH";
    private static final String KEY_BACKDROP_PATH = "BACKDROP_PATH";
    private static final String KEY_POPULARITY = "POPULARITY";

    public MovieDatabase(@Nullable Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + KEY_ID + " INTEGER, "
                + KEY_TITLE + " TEXT, "
                + KEY_GENRE + " TEXT, "
                + KEY_OVERVIEW + " TEXT, "
                + KEY_POSTER_PATH + " TEXT, "
                + KEY_BACKDROP_PATH + " TEXT, "
                + KEY_POPULARITY + " DOUBLE "
                + ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public long AddAMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, movie.getId());
        contentValues.put(KEY_TITLE, movie.getTitle());
        contentValues.put(KEY_GENRE, movie.getGenre());
        contentValues.put(KEY_OVERVIEW, movie.getOverview());
        contentValues.put(KEY_POSTER_PATH, movie.getPoster_path());
        contentValues.put(KEY_BACKDROP_PATH, movie.getBackdrop_path());
        contentValues.put(KEY_POPULARITY, movie.getPopularity());
        return db.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public ArrayList<Movie> GetAllMovies() {
        ArrayList<Movie> movie_list = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Movie movie = new Movie (
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getDouble(6)
            );
            movie_list.add(movie);
        }
        cursor.close();
        return movie_list;
    }

    public void removeAllMovies() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,null,null);
        db.close();
    }
}
