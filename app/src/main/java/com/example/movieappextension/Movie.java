package com.example.movieappextension;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private final int id;
    private final String title;
    private final String genre;
    private final String overview;
    private final String poster_path;
    private final String backdrop_path;
    private final double popularity;

    public Movie(int id, String title, String genre, String overview, String poster_path, String backdrop_path, double popularity) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.overview = overview;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.popularity = popularity;
    }

    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        genre = in.readString();
        overview = in.readString();
        poster_path = in.readString();
        backdrop_path = in.readString();
        popularity = in.readDouble();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public double getPopularity() {
        return popularity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(genre);
        dest.writeString(overview);
        dest.writeString(poster_path);
        dest.writeString(backdrop_path);
        dest.writeDouble(popularity);
    }
}
