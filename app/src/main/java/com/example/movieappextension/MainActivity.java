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
    private ArrayList<Movie> watchList = new ArrayList<>();
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private MovieDatabase movieDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieDatabase = new MovieDatabase(this, 2);
        movieDatabase.removeAllMovies(); // Consider removing this if not needed in production

        viewPager = findViewById(R.id.viewpager);
        viewPagerAdapter = new ViewPagerAdapter(
                getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                getString(R.string.api_key), 
                this
        );
        viewPager.setAdapter(viewPagerAdapter);

        // Initialize watchlist on start
        fetchMoviesFromDB();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchMoviesFromDB();
    }

    private void fetchMoviesFromDB() {
        if (watchList == null) {
            watchList = new ArrayList<>();
        } else {
            watchList.clear();
        }
        watchList.addAll(movieDatabase.getAllMovies());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == REQUEST_ADD && resultCode == RESULT_OK && data != null) {
            Movie movie = data.getParcelableExtra(INTENT_KEY_ADD);
            if (movie != null && !inWatchList(movie.getId())) {
                movieDatabase.addAMovie(movie);
                fetchMoviesFromDB();  // Refresh the movie list
                viewPagerAdapter.notifyDataSetChanged();  // Only notify adapter instead of resetting it
                viewPager.setCurrentItem(2);
            }
        }
    }

    @Override
    public boolean inWatchList(int movieID) {
        for (Movie movie : watchList) {
            if (movieID == movie.getId()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ArrayList<Movie> getWatchList() {
        return new ArrayList<>(watchList); // Return a new list to avoid external modification
    }
}
