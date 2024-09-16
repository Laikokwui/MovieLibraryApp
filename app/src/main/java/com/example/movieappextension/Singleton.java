package com.example.movieappextension;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class Singleton {
    private static Singleton mySingleton;
    private final ImageLoader imageLoader;

    private Singleton(Context context) {
        int cacheSize = 4 * 1024 * 1024; // 4MiB or adjust dynamically
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<>(cacheSize);
    
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }
    
            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public static synchronized Singleton getInstance(Context context){
        if (mySingleton == null) { mySingleton = new Singleton(context); }
        return mySingleton;
    }

    public ImageLoader getImageLoader(){
        return imageLoader;
    }
}
