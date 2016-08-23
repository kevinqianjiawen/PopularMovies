package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.popularmovies.data.MovieContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 8/22/2016.
 */
public class MainActivityFragmentFavorite extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    private MovieAdapter favoriteAdapter;
    private GridView gridViewFavorite;
    private View rootView;



    private static final int FAVORITE_LOADER = 0;


    private static final String[] FAVORTIE_COLUMNS = {
            MovieContract.MovieEntry._ID,

            MovieContract.MovieEntry.COLUMN_IMAGE,

            MovieContract.MovieEntry.COLUMN_ID,
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_ID = 0;
    static final int COL_MOVIE_IMAGE = 1;

    static final int COL_MOVIE_ID = 2;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
//        public void onItemSelected(AndroidFlavor androidFlavor);

        //DetailFragmentFavorite call back for when an item has been selected.
        public void onItemSelected(Uri uri);
    }


    public MainActivityFragmentFavorite() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh && !Utility.getSortBy(getContext()).equals("favorite")) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        sort_by = Utility.getSortBy(getContext());

        //set the gridview as favorite
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.main_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        gridViewFavorite = (GridView) rootView.findViewById(R.id.gridview_poster);

        favoriteAdapter = new MovieAdapter(getActivity(), null, 0);
        gridViewFavorite.setAdapter(favoriteAdapter);


        gridViewFavorite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    //Intent intent = new Intent(getActivity(), DetailActivity.class).setData(MovieContract.MovieEntry.CONTENT_URI);
                    //startActivity(intent);
                    ((Callback) getActivity()).onItemSelected(MovieContract.MovieEntry.buildMovieUri(cursor.getInt(COL_MOVIE_ID)));
                }

            }
        });


        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (getLoaderManager().getLoader(FAVORITE_LOADER) != null) {
            getLoaderManager().restartLoader(FAVORITE_LOADER, null, this);
        } else {
            getLoaderManager().initLoader(FAVORITE_LOADER, null, this);
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri movieUri = MovieContract.MovieEntry.CONTENT_URI;

        return new CursorLoader(getActivity(),
                movieUri,
                FAVORTIE_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        //because I use two adapter to get the data from the internet or sql
        if (favoriteAdapter != null) {
            favoriteAdapter.swapCursor(cursor);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        if (favoriteAdapter != null) {
            favoriteAdapter.swapCursor(null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String sort_by = Utility.getSortBy(getContext());
        if (!sort_by.equals(getString(R.string.pref_key_favortie))) {
            // Create fragment and give it an argument specifying the article it should show
            MainActivityFragment newFragment = new MainActivityFragment();
            FragmentManager manager = getFragmentManager();

            FragmentTransaction transaction = manager.beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);

// Commit the transaction
            transaction.commit();
        }
    }
}




