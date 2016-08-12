package com.example.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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

    public static class FavortieViewHolder {
        public final ImageView iconView;
        public final TextView titleView;
        public final ImageView posterView;
        public final TextView dateView;
        public final TextView rateView;
        public final CheckBox starView;
        public final TextView descriptionView;


        public FavortieViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            titleView = (TextView) view.findViewById(R.id.detail__favorite_title);
            posterView = (ImageView) view.findViewById(R.id.detail_favorite_image);
            dateView = (TextView) view.findViewById(R.id.detail_favorite_release);
            rateView = (TextView) view.findViewById(R.id.detail_favorite_rate);
            starView = (CheckBox) view.findViewById(R.id.favorite_star);
            descriptionView = (TextView) view.findViewById(R.id.detail_favorite_overview);

        }
    }
    public movieAdapter(Context context, Cursor c, int flags){
        super(context, c, flags);
    }

    /*
        This is ported from FetchDataTask --- but now we go straight from the cursor to the
        string.
     */
    private String convertCursorRowToUXFormat(Cursor cursor){

        return cursor.getString(MainActivityFragment.COL_MOVIE_IMAGE);
    }

    @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View view = LayoutInflater.from(context).inflate(R.layout.image_item_movie, parent, false);
        FavortieViewHolder viewHolder = new FavortieViewHolder(view);
        view.setTag(viewHolder);
                        return view;
            }

    @Override
        public void bindView(View view, Context context, Cursor cursor) {

        FavortieViewHolder viewHolder= (FavortieViewHolder)view.getTag();
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.
        String url = "http://image.tmdb.org/t/p/w185" + convertCursorRowToUXFormat(cursor);
        //ImageView iconView = (ImageView) view.findViewById(R.id.list_item_icon);
        Picasso.with(context).load(url).into(viewHolder.iconView);
    }
}
