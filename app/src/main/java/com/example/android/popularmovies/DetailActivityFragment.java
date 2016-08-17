package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * Created by kevin on 8/12/2016.
 */
public class DetailActivityFragment extends Fragment{
    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();



    static final String DETAIL = "DETAIL";
    private AndroidFlavor movie;

    private static final String[] FAVORTIE_COLUMNS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_DATE,
            MovieContract.MovieEntry.COLUMN_RATING,
            MovieContract.MovieEntry.COLUMN_DESCRIPTION,
            MovieContract.MovieEntry.COLUMN_IMAGE
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_ID = 0;
    static final int COL_MOVIE_ID = 1;
    static final int COL_MOVIE_TITLE = 2;
    static final int COL_MOVIE_DATE = 3;
    static final int COL_MOVIE_RATING = 4;
    static final int COL_MOVIE_DESCRIPTION = 5;
    static final int COL_MOVIE_IMAGE = 6;


    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent = getActivity().getIntent();

        Bundle arguments = getArguments();
        if (arguments != null){
            movie = arguments.getParcelable(DetailActivityFragment.DETAIL);
        }

        if ((intent != null && intent.hasExtra("movie") ) || movie != null) {
            if(movie == null) {
                movie = (AndroidFlavor) intent.getParcelableExtra("movie");
                //Log.v("bad", movie.toString());
            }

            final int idText = movie.getid();

            TextView title = (TextView) rootView.findViewById(R.id.detail_title);
            final String titleText = movie.titleToStr();
            title.setText(titleText);

            TextView release = (TextView) rootView.findViewById(R.id.detail_release);
            final String releaseText = movie.dateToStr();
            release.setText(releaseText);


            TextView rate = (TextView) rootView.findViewById(R.id.detail_rate);
            final double rateText = movie.rateGet();
            rate.setText("" + rateText);

            TextView overview = (TextView) rootView.findViewById(R.id.detail_overview);
            final String descriptionText = movie.descriptionToStr();
            overview.setText(descriptionText);

            final String url = "http://image.tmdb.org/t/p/w500" + movie.imageToStr();
            //Log.v("pop", url);
            ImageView iconView = (ImageView) rootView.findViewById(R.id.detail_image);
            Picasso.with(getActivity()).load(url).into(iconView);

            //a star check box, if the user click it, the favorite will add to the database;
            final com.example.android.popularmovies.FloatingActionButton checkBox = (com.example.android.popularmovies.FloatingActionButton) rootView.findViewById(R.id.action_button);
            checkBox.setOnCheckedChangeListener(new com.example.android.popularmovies.FloatingActionButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(com.example.android.popularmovies.FloatingActionButton fabView, boolean isChecked) {
                    if (isChecked){

                        ContentValues movieValues = new ContentValues();

                        movieValues.put(MovieContract.MovieEntry.COLUMN_ID, idText);
                        movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, titleText);
                        movieValues.put(MovieContract.MovieEntry.COLUMN_DATE, releaseText);
                        movieValues.put(MovieContract.MovieEntry.COLUMN_RATING, rateText);
                        movieValues.put(MovieContract.MovieEntry.COLUMN_DESCRIPTION, descriptionText);
                        movieValues.put(MovieContract.MovieEntry.COLUMN_IMAGE, url);

                        getContext().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, movieValues);

                        Log.v("DATABASE", "Insert complete");



                    }else {

                        getContext().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, "movie_id=?", new String[]{String.valueOf(idText)});
                        Log.v("DATABASE", "Delete complete");

                    }
                }
                }
            );

        }


        return rootView;
    }


}
