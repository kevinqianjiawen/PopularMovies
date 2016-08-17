package com.example.android.popularmovies;

/**
 * Created by kevin on 8/12/2016.
 */

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragmentFavorite extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = DetailActivityFragmentFavorite.class.getSimpleName();

    static final String DETAIL_FAVORITE = "DetailFavortie";
    private static final int DETAIL_LOADER = 0;
    private Uri mUri;

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

    private TextView mTitleView;
    private ImageView mPosterView;
    private TextView mDateView;
    private TextView mRateView;
    private TextView mDescriptionView;
    private com.example.android.popularmovies.FloatingActionButton mButtonView;


    public DetailActivityFragmentFavorite() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if(arguments != null){
            mUri = arguments.getParcelable(DetailActivityFragmentFavorite.DETAIL_FAVORITE);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mPosterView = (ImageView)rootView.findViewById(R.id.detail_image);
        mTitleView = (TextView) rootView.findViewById(R.id.detail_title);
        mDateView = (TextView)rootView.findViewById(R.id.detail_release);
        mRateView = (TextView) rootView.findViewById(R.id.detail_rate);
        mDescriptionView = (TextView) rootView.findViewById(R.id.detail_overview);
        mButtonView = (com.example.android.popularmovies.FloatingActionButton) rootView.findViewById(R.id.action_button);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.v(LOG_TAG, "In onCreateLoader");

        if (mUri != null) {
            return new CursorLoader(getActivity(),
                    mUri,
                    FAVORTIE_COLUMNS,
                    null,
                    null,
                    null);
        }
        return  null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor data) {
        Log.v(LOG_TAG, "In onLoadFinished");
        if (data.moveToFirst() && data != null) {



        final int movieId = data.getInt(COL_MOVIE_ID);

        final String movieTitle = data.getString(COL_MOVIE_TITLE);
        mTitleView.setText(movieTitle);

        final Double movieRate = data.getDouble(COL_MOVIE_RATING);
        mRateView.setText("" + movieRate);

        final String movieDescription = data.getString(COL_MOVIE_DESCRIPTION);
        mDescriptionView.setText(movieDescription);

        final String movieDate = data.getString(COL_MOVIE_DATE);
        mDateView.setText(movieDate);

        final String movieImage = data.getString(COL_MOVIE_IMAGE);
        final String url = "http://image.tmdb.org/t/p/w500" + movieImage;
        Picasso.with(getActivity()).load(url).into(mPosterView);
            mButtonView.setChecked(true);

            mButtonView.setOnCheckedChangeListener(new com.example.android.popularmovies.FloatingActionButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(com.example.android.popularmovies.FloatingActionButton fabView, boolean isChecked) {
                    if(isChecked){
                    ContentValues movieValues = new ContentValues();

                    movieValues.put(MovieContract.MovieEntry.COLUMN_ID, movieId);
                    movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movieTitle);
                    movieValues.put(MovieContract.MovieEntry.COLUMN_DATE, movieDate);
                    movieValues.put(MovieContract.MovieEntry.COLUMN_RATING, movieRate);
                    movieValues.put(MovieContract.MovieEntry.COLUMN_DESCRIPTION, movieDescription);
                    movieValues.put(MovieContract.MovieEntry.COLUMN_IMAGE, url);

                    getContext().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, movieValues);

                    Log.v("DATABASE", "Insert complete");
                }else {
                    getContext().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, "movie_id=?", new String[]{String.valueOf(movieId)});
                    Log.v("DATABASE", "Delete complete");
                }
                }});




        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }


}
