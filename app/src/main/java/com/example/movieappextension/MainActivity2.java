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

    private TextView textView_movie_title;
    private TextView textView_popular;
    private TextView textView_overviews;
    private ImageView imageView_movie_banner;
    private Button button_add_to_list;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        textView_movie_title = findViewById(R.id.textView_movie_title);
        textView_popular = findViewById(R.id.textView_popular);
        textView_overviews = findViewById(R.id.textView_overviews);
        imageView_movie_banner = findViewById(R.id.imageView_movie_banner);
        button_add_to_list = findViewById(R.id.button_add_to_watch_list);

        if (getIntent().hasExtra(DATA_KEY)) {
            movie = getIntent().getParcelableExtra(DATA_KEY);
            textView_movie_title.setText(movie.getTitle());
            textView_popular.setText(String.valueOf(movie.getPopularity()));
            textView_overviews.setText(movie.getOverview());
            setMovieBanner("https://image.tmdb.org/t/p/w1280"+movie.getBackdrop_path());
        }

        if (getIntent().getBooleanExtra(CLICKABLE_OPTION,false)) {
            button_add_to_list.setVisibility(View.GONE);
        }
        else {
            button_add_to_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(MainActivity.INTENT_KEY_ADD,movie);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            });
        }
    }

    private void setMovieBanner(String url) {
        ImageLoader imageLoader = Singleton.getInstance(getApplicationContext()).getImageLoader();
        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap bmp = response.getBitmap();
                imageView_movie_banner.setImageBitmap(bmp);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error load Movie Banner: ", error.getMessage());
            }
        });
    }
}