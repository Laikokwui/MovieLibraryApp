package com.example.movieappextension;

import org.json.JSONArray;
import java.util.Hashtable;

public class Genre {

    Hashtable<Integer,String> genre_mapping = new Hashtable<Integer,String>();

    public Genre() {
        if (genre_mapping.isEmpty()) {
            create_table();
        }
    }

    private void create_table() {
        genre_mapping.put(28,"Action");
        genre_mapping.put(12,"Adventure");
        genre_mapping.put(16,"Animation");
        genre_mapping.put(35,"Comedy");
        genre_mapping.put(80,"Crime");
        genre_mapping.put(99,"Documentary");
        genre_mapping.put(18,"Drama");
        genre_mapping.put(10751,"Family");
        genre_mapping.put(14,"Fantasy");
        genre_mapping.put(36,"History");
        genre_mapping.put(27,"Horror");
        genre_mapping.put(10402,"Music");
        genre_mapping.put(9648,"Mystery");
        genre_mapping.put(10749,"Romance");
        genre_mapping.put(878,"Science Fiction");
        genre_mapping.put(10770,"TV Movie");
        genre_mapping.put(53,"Thriller");
        genre_mapping.put(10752,"War");
        genre_mapping.put(37,"Western");
    }

    // call this method to obtain the genre from the list of genre ids
    public String getGenre(JSONArray genre_id) {
        String genre = "";
        for (int c = 0; c < genre_id.length(); c++) {
            int id = genre_id.optInt(c);
            genre = genre + genre_mapping.get(id);
            if (c < genre_id.length() - 1)
                genre = genre + ", ";
        }
        return genre;
    }
}
