package com.example.android.popularmovies;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieContract;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class DetailActivityFavorite extends AppCompatActivity {


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
                    .add(R.id.container, new DetailActivityFragment())
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
    public static class DetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
        private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();


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


        public DetailActivityFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_favoritedetail, container, false);

            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            Intent intent = getActivity().getIntent();
                getLoaderManager().initLoader(DETAIL_LOADER, null, this);
                super.onActivityCreated(savedInstanceState);
        }

        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            Log.v(LOG_TAG, "In onCreateLoader");

            Intent intent = getActivity().getIntent();
            if (intent == null) {
                return null;
            }

            return new CursorLoader(getActivity(),
                    intent.getData(),
                    FAVORTIE_COLUMNS,
                    null,
                    null,
                    null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor data) {
            Log.v(LOG_TAG, "In onLoadFinished");
            if (!data.moveToFirst()) { return; }

            int movieId = data.getInt(COL_MOVIE_ID);

            String movieTitle = data.getString(COL_MOVIE_TITLE);

            double movieRate = data.getDouble(COL_MOVIE_RATING);

            String movieDescription = data.getString(COL_MOVIE_DESCRIPTION);

            String movieDate = data.getString(COL_MOVIE_DATE);

            String movieImage = data.getString(COL_MOVIE_IMAGE);

            TextView detailTextView = (TextView)getView().findViewById(R.id.detail__favorite_title);
            detailTextView.setText(movieTitle);



        }

        @Override
        public void onLoaderReset(Loader<Cursor> cursorLoader) {
        }


    }


}