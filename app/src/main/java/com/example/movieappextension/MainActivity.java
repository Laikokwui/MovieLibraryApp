package com.example.movieappextension;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyAdapter.DbInteractionListener {
    public static final int REQUEST_ADD = 5;
    public static final String INTENT_KEY_ADD = "AddMovie";
    public ArrayList<Movie> watch_list;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private MovieDatabase movieDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieDatabase = new MovieDatabase(this,2);
        movieDatabase.removeAllMovies();
        viewPager = findViewById(R.id.viewpager);
        viewPagerAdapter = new ViewPagerAdapter(
                getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                getString(R.string.api_key), this
        );
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetMoviesFromDB();
    }

    private void GetMoviesFromDB() {
        if (watch_list != null) { watch_list.clear(); }
        watch_list = movieDatabase.GetAllMovies();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD) {
            if (resultCode == RESULT_OK) {
                Movie movie = data.getParcelableExtra(INTENT_KEY_ADD);
                if (movie != null) {
                    movieDatabase.AddAMovie(movie);
                    viewPager.setAdapter(viewPagerAdapter);
                    viewPager.setCurrentItem(2);
                }
            }
        }
    }

    @Override
    public boolean inWatchList(int movieID) {
        for (Movie movie : watch_list) {
            if (movieID == movie.getId()) { return true; }
        }
        return false;
    }

    @Override
    public ArrayList<Movie> getWatchList() {
        GetMoviesFromDB();
        return watch_list;
    }
}