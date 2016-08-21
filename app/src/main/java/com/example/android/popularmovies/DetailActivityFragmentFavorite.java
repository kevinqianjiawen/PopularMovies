package com.example.android.popularmovies;

/**
 * Created by kevin on 8/12/2016.
 */

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragmentFavorite extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = DetailActivityFragmentFavorite.class.getSimpleName();

    static final String DETAIL_FAVORITE = "DetailFavortie";
    private static final int DETAIL_LOADER = 0;
    private static final int REVIEW_LOADER = 1;
    private static final int VIDEO_LOADER = 2;
    private Uri mUri;

    private static final String[] FAVORTIE_COLUMNS = {
            MovieContract.MovieEntry.COLUMN_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_DATE,
            MovieContract.MovieEntry.COLUMN_RATING,
            MovieContract.MovieEntry.COLUMN_DESCRIPTION,
            MovieContract.MovieEntry.COLUMN_IMAGE,


    };
    private static final String[] REVIEW_COLUMNS = {
            MovieContract.ReviewEntry.COLUMN_AUTHOR,
            MovieContract.ReviewEntry.COLUMN_REVIEW
    };

    private static final String[] VIDEO_COLUMNS = {
            MovieContract.VideoEntry.COLUMN_KEY,
            MovieContract.VideoEntry.COLUMN_TYPE,
            MovieContract.VideoEntry.COLUMN_NAME,
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_MOVIE_ID = 0;
    static final int COL_MOVIE_TITLE = 1;
    static final int COL_MOVIE_DATE = 2;
    static final int COL_MOVIE_RATING = 3;
    static final int COL_MOVIE_DESCRIPTION = 4;
    static final int COL_MOVIE_IMAGE = 5;

    static final int COL_REVIEW_AUTHOR = 0;
    static final int COL_REVIEW_CONTENT = 1;

    static final int COL_VIDEO_KEY = 0;
    static final int COL_VIDEO_TYPE = 1;
    static final int COL_VIDEO_NAME = 2;


    private TextView mTitleView;
    private ImageView mPosterView;
    private TextView mDateView;
    private TextView mRateView;
    private TextView mDescriptionView;

    private com.example.android.popularmovies.FloatingActionButton mButtonView;

    private ImageView mTopPreview;
    private RecyclerView videoList;
    private RecyclerView reviewList;

    private String previewKey;

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

        final Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.moviebar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        mPosterView = (ImageView)rootView.findViewById(R.id.detail_image);
        mTitleView = (TextView) rootView.findViewById(R.id.detail_title);
        mDateView = (TextView)rootView.findViewById(R.id.detail_release);
        mRateView = (TextView) rootView.findViewById(R.id.detail_rate);
        mDescriptionView = (TextView) rootView.findViewById(R.id.detail_overview);
        mButtonView = (com.example.android.popularmovies.FloatingActionButton) rootView.findViewById(R.id.action_button);

        mTopPreview = (ImageView) rootView.findViewById(R.id.preview);
        videoList = (RecyclerView) rootView.findViewById(R.id.recyclerview_video);
        reviewList = (RecyclerView) rootView.findViewById(R.id.recyclerview_review);


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        getLoaderManager().initLoader(REVIEW_LOADER, null, this);
        getLoaderManager().initLoader(VIDEO_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.v(LOG_TAG, "In onCreateLoader");

        if (mUri != null) {
            switch (i) {
                case 0:
                return new CursorLoader(getActivity(),
                        mUri,
                        FAVORTIE_COLUMNS,
                        null,
                        null,
                        null);
                case 1:
                    return new CursorLoader(getActivity(),
                            MovieContract.ReviewEntry.buildReviewUri(ContentUris.parseId(mUri)),
                            REVIEW_COLUMNS,
                            null,
                            null,
                            null);
                case 2:
                    return new CursorLoader(getActivity(),
                            MovieContract.VideoEntry.buildVideoUri(ContentUris.parseId(mUri)),
                            VIDEO_COLUMNS,
                            null,
                            null,
                            null);

            }
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
                        getContext().getContentResolver().delete(MovieContract.ReviewEntry.CONTENT_URI, "id=?", new String[]{String.valueOf(movieId)});
                        getContext().getContentResolver().delete(MovieContract.VideoEntry.CONTENT_URI, "id=?", new String[]{String.valueOf(movieId)});
                    Log.v("DATABASE", "Delete complete");
                }
                }});



            try{
                for (int i = 0; i <)
                previewKey = data.getString(COL_VIDEO_KEY);

                final String urlPreview = "http://img.youtube.com/vi/"+ previewKey + "/0.jpg";
                Picasso.with(getContext()).load(urlPreview).into(mTopPreview);

                mTopPreview.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + previewKey));
                        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                            getContext().startActivity(intent);
                        }
                    }
                });
            }catch (IndexOutOfBoundsException iobe){
                mTopPreview.setImageResource(R.drawable.novideo);
            }

        }

        //try to get the data from data base and set recycleview;

        List<AndroidFlavor.video> videoFromData = new ArrayList<>();
        List<AndroidFlavor.review> reviewFromData = new ArrayList<>();
        try {
            data.moveToPrevious();
            videoFromData.add(new AndroidFlavor.video(null, null, null));
            for (int i = 0; i < data.getCount() - 1; i++) {

                String keyNext = data.getString(COL_VIDEO_KEY);
                if (!previewKey.equals(keyNext)) {

                    videoFromData.add(new AndroidFlavor.video(data.getString(COL_VIDEO_NAME), data.getString(COL_VIDEO_KEY), data.getString(COL_VIDEO_TYPE)));

                    previewKey = keyNext;
                }
                data.moveToPrevious();

            }

            videoList.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            videoList.setLayoutManager(llm);
            videoList.setAdapter(new VideoAdapter(videoFromData));

        }catch (Exception cibe){
            Log.v("data", "don't have video data");
        }

        data.moveToLast();

        try {

            reviewFromData.add(new AndroidFlavor.review(data.getString(COL_REVIEW_AUTHOR), data.getString(COL_REVIEW_CONTENT)));

            for (int j = 0; j < data.getCount() - 1; j++) {
                String author = data.getString(COL_REVIEW_AUTHOR);
                String content = data.getString(COL_REVIEW_CONTENT);
                String authorNext = data.getString(COL_REVIEW_AUTHOR);
                if (!author.equals(authorNext)) {
                    reviewFromData.add(new AndroidFlavor.review(author, content));
                }
            }

            reviewList.setHasFixedSize(true);
            LinearLayoutManager llm2 = new LinearLayoutManager(getContext());
            reviewList.setLayoutManager(llm2);
            reviewList.setAdapter(new ReviewAdapter(reviewFromData));
        } catch (Exception cibe){

            Log.v("data", "don't have review data");
        }




    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }


}
