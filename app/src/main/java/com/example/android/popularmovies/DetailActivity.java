package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieContract;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

/**
 * Created by kevin on 8/12/2016.
 */


public class DetailActivity extends AppCompatActivity {


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailActivityFragmentFavorite())
                    .commit();
        }


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailActivityFragmentFavorite extends Fragment {
        private static final String LOG_TAG = DetailActivityFragmentFavorite.class.getSimpleName();


        private static final int DETAIL_LOADER = 0;

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


        public DetailActivityFragmentFavorite() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            Intent intent = getActivity().getIntent();


            if (intent != null && intent.hasExtra("movie")) {
                AndroidFlavor movie = (AndroidFlavor) intent.getParcelableExtra("movie");
                //Log.v("bad", movie.toString());

                final int idText = movie.getid();

                TextView title = (TextView) rootView.findViewById(R.id.detail_title);
                final String titleText = movie.titleToStr();
                title.setText(titleText);
                title.setTextSize(20);

                TextView release = (TextView) rootView.findViewById(R.id.detail_release);
                final String releaseText = movie.dateToStr();
                release.setText(releaseText);
                release.setTextSize(20);


                TextView rate = (TextView) rootView.findViewById(R.id.detail_rate);
                final double rateText = movie.rateGet();
                rate.setText("" + rateText);
                rate.setTextSize(20);
                ;
                TextView overview = (TextView) rootView.findViewById(R.id.detail_overview);
                final String descriptionText = movie.descriptionToStr();
                overview.setText(descriptionText);
                overview.setTextSize(20);

                final String url = "http://image.tmdb.org/t/p/w500" + movie.imageToStr();
                //Log.v("pop", url);
                ImageView iconView = (ImageView) rootView.findViewById(R.id.detail_image);
                Picasso.with(getActivity()).load(url).into(iconView);

                //a star check box, if the user click it, the favorite will add to the database;
                final CheckBox checkBox = (CheckBox) rootView.findViewById(R.id.star);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ContentValues movieValues = new ContentValues();

                            movieValues.put(MovieContract.MovieEntry.COLUMN_ID, idText);
                            movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, titleText);
                            movieValues.put(MovieContract.MovieEntry.COLUMN_DATE, releaseText);
                            movieValues.put(MovieContract.MovieEntry.COLUMN_RATING, rateText);
                            movieValues.put(MovieContract.MovieEntry.COLUMN_DESCRIPTION, descriptionText);
                            movieValues.put(MovieContract.MovieEntry.COLUMN_IMAGE, url);

                            getContext().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, movieValues);

                            Log.v("DATABASE", "Insert complete");
                        } else {
                            getContext().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, "movie_id=?", new String[]{String.valueOf(idText)});
                            Log.v("DATABASE", "Delete complete");
                        }
                    }
                });

            }


            return rootView;
        }


    }
}