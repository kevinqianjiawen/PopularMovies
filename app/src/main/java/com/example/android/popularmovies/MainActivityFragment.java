package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
 * Created by kevin on 5/15/2016.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private AndroidFlavorAdapter flavorAdapter;
    private movieAdapter favoriteAdapter;

    private GridView gridView;
    private GridView gridViewFavorite;
    private View rootView;

    private static final int FAVORITE_LOADER = 0;



    private static final String[] FAVORTIE_COLUMNS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_IMAGE
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_ID = 0;
    static final int COL_MOVIE_IMAGE = 1;



    public MainActivityFragment() {
    }

    public void onCreate(Bundle savedInstanceState){
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
        if(id == R.id.action_refresh){
            updateMovie();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovie();
    }

    public void updateMovie(){
        if(isOnline()) {
            FetchDataTask movieTask = new FetchDataTask(getActivity(), flavorAdapter);
            String sort = PreferenceManager.getDefaultSharedPreferences(getActivity())
                    .getString(getString(R.string.pref_key),
                            getString(R.string.pref_key_popular));

            movieTask.execute(sort);
        }else {
            Toast.makeText(getContext(),"The wifi is unavilable",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String sort_by = Utility.getSortBy(getContext());
        if (sort_by.equals(getString(R.string.pref_key_favortie))) {
            //set the gridview as favorite
            rootView = inflater.inflate(R.layout.fragment_favorite,container,false);

            gridViewFavorite = (GridView) rootView.findViewById(R.id.gridview_favorite);

            favoriteAdapter = new movieAdapter(getActivity(), null, 0);
            gridViewFavorite.setAdapter(favoriteAdapter);


            gridViewFavorite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                    Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                    if (cursor != null) {
                        Intent intent = new Intent(getActivity(), DetailActivityFavorite.class).setData(MovieContract.MovieEntry.CONTENT_URI);
                        startActivity(intent);
                    }
                }
            });

        }else {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);

            //inflate menu
            List<AndroidFlavor> movieList = new ArrayList<AndroidFlavor>();


            flavorAdapter = new AndroidFlavorAdapter(getActivity(), movieList);

            // Get a reference to the ListView, and attach this adapter to it.
            gridView = (GridView) rootView.findViewById(R.id.gridview_poster);
            gridView.setAdapter(flavorAdapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    //Toast.makeText(getActivity(), "debug", Toast.LENGTH_SHORT).show();

                    AndroidFlavor movieIntent = flavorAdapter.getItem(position);

                    Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra("movie", movieIntent);
                    startActivity(intent);
                }
            });

        }



        return rootView;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(FAVORITE_LOADER, null, this);
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



}
