package com.example.movieappextension;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

public class MainActivity2 extends AppCompatActivity {
    public static final String DATA_KEY = "json";
    public static final String CLICKABLE_OPTION = "clickable";

    private TextView textViewMovieTitle;
    private TextView textViewPopularity;
    private TextView textViewOverview;
    private ImageView imageViewMovieBanner;
    private Button buttonAddToList;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Initialize UI elements
        textViewMovieTitle = findViewById(R.id.textView_movie_title);
        textViewPopularity = findViewById(R.id.textView_popular);
        textViewOverview = findViewById(R.id.textView_overviews);
        imageViewMovieBanner = findViewById(R.id.imageView_movie_banner);
        buttonAddToList = findViewById(R.id.button_add_to_watch_list);

        // Populate the UI with movie data if available
        if (getIntent().hasExtra(DATA_KEY)) {
            movie = getIntent().getParcelableExtra(DATA_KEY);
            if (movie != null) {
                textViewMovieTitle.setText(movie.getTitle());
                textViewPopularity.setText(String.valueOf(movie.getPopularity()));
                textViewOverview.setText(movie.getOverview());
                setMovieBanner("https://image.tmdb.org/t/p/w1280" + movie.getBackdrop_path());
            }
        }

        // Toggle the visibility of the "Add to Watchlist" button
        if (getIntent().getBooleanExtra(CLICKABLE_OPTION, false)) {
            buttonAddToList.setVisibility(View.GONE);
        } else {
            buttonAddToList.setOnClickListener(v -> {
                Intent intent = new Intent();
                intent.putExtra(MainActivity.INTENT_KEY_ADD, movie);
                setResult(RESULT_OK, intent);
                finish();
            });
        }
    }

    // Method to set the movie banner image using the given URL
    private void setMovieBanner(String url) {
        ImageLoader imageLoader = Singleton.getInstance(getApplicationContext()).getImageLoader();
        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap bitmap = response.getBitmap();
                if (bitmap != null) {
                    imageViewMovieBanner.setImageBitmap(bitmap);
                } else {
                    // Placeholder image or error handling if bitmap is null
                    imageViewMovieBanner.setImageResource(R.drawable.placeholder_image); 
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MovieBannerError", "Error loading movie banner: " + error.getMessage());
                imageViewMovieBanner.setImageResource(R.drawable.error_image); // Set an error image
            }
        });
    }
}
