package com.example.movieappextension;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private final String url_now_playing;
    private final String url_up_coming;
    private final String[] tab_title = {"NOW SHOWING","UPCOMING MOVIES","WATCH LIST"};
    private final MyAdapter.DbInteractionListener dbInteractionListener;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, String api_key, MyAdapter.DbInteractionListener dbInteractionListener) {
        super(fm, behavior);
        url_now_playing = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + api_key;
        url_up_coming = "https://api.themoviedb.org/3/movie/upcoming?api_key=" + api_key;
        this.dbInteractionListener = dbInteractionListener;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        FragmentOne fragmentOne = null;
        switch (position) {
            case 0:
                fragmentOne = FragmentOne.newInstance(url_now_playing);
                break;
            case 1:
                fragmentOne = FragmentOne.newInstance(url_up_coming);
                break;
            case 2:
                fragmentOne = FragmentOne.newInstance("null");
                break;
        }
        if (fragmentOne != null) { fragmentOne.SetDbInteractionListener(dbInteractionListener); }
        return fragmentOne;
    }

    @Override
    public int getCount() {
        return tab_title.length;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tab_title[position];
    }
}
