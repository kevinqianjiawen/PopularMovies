package com.jiawenqian.android.popularmovies;

import android.app.Application;

import com.facebook.stetho.Stetho;


/**
 * Created by kevin on 8/6/2016.
 */
public class MyApplication extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
