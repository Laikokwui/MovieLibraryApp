package com.example.movieappextension;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private JSONArray jsonArray;
    private ArrayList<Movie> watch_list;
    private Context context;

    public DbInteractionListener dbInteractionListener;

    public interface DbInteractionListener {
        boolean inWatchList(int movieID);
        ArrayList<Movie> getWatchList();
    }

    public MyAdapter(JSONArray jsonArray, DbInteractionListener dbInteractionListener) {
        if (jsonArray == null) { this.watch_list = dbInteractionListener.getWatchList(); }
        else { this.jsonArray = jsonArray; }
        this.dbInteractionListener = dbInteractionListener;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie,parent,false);
        context = view.getContext();
        return new MyViewHolder(view, dbInteractionListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String movie_title = "", genre_list = "", image_url = "", image_path = "";
        double popularity = 0.0;
        JSONObject jsonObject_movie;

        try {
            if (jsonArray == null) {
                movie_title = watch_list.get(position).getTitle();
                genre_list = watch_list.get(position).getGenre();
                popularity = watch_list.get(position).getPopularity();
                image_path = watch_list.get(position).getPoster_path();
            }
            else {
                jsonObject_movie = jsonArray.getJSONObject(position);
                movie_title = jsonObject_movie.getString("title");
                genre_list = getGenreType(jsonObject_movie.getJSONArray("genre_ids"));
                popularity = jsonObject_movie.getDouble("popularity");
                image_path = jsonObject_movie.getString("poster_path");
            }
            image_url = "https://image.tmdb.org/t/p/w300" + image_path;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.itemView.setTag(position);
        holder.textView_title.setText(movie_title);
        holder.textView_genres.setText(genre_list);
        holder.textView_popularities.setText(String.valueOf(popularity));

        ImageLoader imageLoader = Singleton.getInstance(context).getImageLoader();
        imageLoader.get(image_url, ImageLoader.getImageListener(holder.networkImageView, android.R.drawable.ic_lock_lock, android.R.drawable.ic_dialog_alert));
        holder.networkImageView.setImageUrl(image_url,imageLoader);
    }

    @Override
    public int getItemCount() {
        int count;
        if (jsonArray == null) { count = watch_list.size(); }
        else { count = jsonArray.length(); }
        return count;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView_title, textView_genres, textView_popularities;
        public NetworkImageView networkImageView;
        public DbInteractionListener dbInteractionListener;

        public MyViewHolder(@NonNull View itemView, DbInteractionListener dbInteractionListener) {
            super(itemView);
            networkImageView = itemView.findViewById(R.id.networkImageView_movie);
            textView_title = itemView.findViewById(R.id.textView_title);
            textView_genres = itemView.findViewById(R.id.textView_genres);
            textView_popularities = itemView.findViewById(R.id.textView_popularities);
            this.dbInteractionListener = dbInteractionListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, MainActivity2.class);
            Movie movie = null;

            if (jsonArray != null) {
                try {
                    JSONObject selected_movie = jsonArray.getJSONObject((int) v.getTag());
                    movie = new Movie(
                        selected_movie.getInt("id"),
                        selected_movie.getString("title"),
                        getGenreType(selected_movie.getJSONArray("genre_ids")),
                        selected_movie.getString("overview"),
                        selected_movie.getString("poster_path"),
                        selected_movie.getString("backdrop_path"),
                        selected_movie.getDouble("popularity")
                    );
                    intent.putExtra(MainActivity2.CLICKABLE_OPTION, dbInteractionListener.inWatchList(movie.getId()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                // movies in watch list all doesn't have add to wish list button
                watch_list = dbInteractionListener.getWatchList();
                movie = watch_list.get((int) v.getTag());
                intent.putExtra(MainActivity2.CLICKABLE_OPTION, true);
            }
            intent.putExtra(MainActivity2.DATA_KEY, movie);
            ((MainActivity) context).startActivityForResult(intent, MainActivity.REQUEST_ADD);
        }
    }

    private String getGenreType(JSONArray jsonArray) {
        Genre genre = new Genre();
        return genre.getGenre(jsonArray);
    }
}
