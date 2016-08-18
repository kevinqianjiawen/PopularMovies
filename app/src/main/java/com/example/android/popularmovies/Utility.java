package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by kevin on 8/12/2016.
 */
public class Utility {
    public static String getSortBy(Context context){
        // Gets back the choice selected by the user to sort the movies
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String sort_by = sharedPrefs.getString(context.getString(R.string.pref_key), context.getString(R.string.pref_key_popular));
        return sort_by;
    }




}


