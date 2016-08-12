package com.example.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.example.android.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by kevin on 8/10/2016.
 */
public class movieAdapter extends CursorAdapter {
    public movieAdapter(Context context, Cursor c, int flags){
        super(context, c, flags);
    }

    /*
        This is ported from FetchDataTask --- but now we go straight from the cursor to the
        string.
     */
    private String convertCursorRowToUXFormat(Cursor cursor){
        //get row indices for our cursor
        int idx_id = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID);
        int idx_title = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
        int idx_date = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_DATE);
        int idx_rate = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING);
        int idx_description = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_DESCRIPTION);
        int idx_image = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE);




        return cursor.getString(idx_image);
    }

    @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View view = LayoutInflater.from(context).inflate(R.layout.image_item_movie, parent, false);

                        return view;
            }

    @Override
        public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.
        String url = "http://image.tmdb.org/t/p/w185" + convertCursorRowToUXFormat(cursor);
        ImageView iconView = (ImageView) view.findViewById(R.id.list_item_icon);
        Picasso.with(context).load(url).into(iconView);
    }
}
