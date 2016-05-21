package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;

/**
 * Created by kevin on 5/15/2016.
 */
public class MainActivityFragment extends Fragment {

    private AndroidFlavorAdapter flavorAdapter;


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
            FetchDataTask movieTask = new FetchDataTask();
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
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //inflate menu
        List<AndroidFlavor> movieList = new ArrayList<AndroidFlavor>();


        flavorAdapter = new AndroidFlavorAdapter(getActivity(), movieList);

        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_poster);
        gridView.setAdapter(flavorAdapter);

        //add a click listener
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(), "debug", Toast.LENGTH_SHORT).show();
                AndroidFlavor movieIntent = flavorAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra("movie", movieIntent);
                startActivity(intent);
            }
        });


        return rootView;
    }

    public class FetchDataTask extends AsyncTask<String, Void, ArrayList<AndroidFlavor>> {



        protected ArrayList<AndroidFlavor> doInBackground(String... params) {
            //get the data from the internet
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            //store the Json string from the internet
            String movieJsonStr = null;


                try {
                    String baseUrl = "https://api.themoviedb.org/3/movie/";
                    String API_PARAM = "api_key";

                    Uri uri = Uri.parse(baseUrl).buildUpon().appendPath(params[0])
                            .appendQueryParameter(API_PARAM, BuildConfig.POPULAR_MOVIES_API_KEY)
                            .build();

                    URL url = new URL(uri.toString());

                    Log.v("Popular movie", uri.toString());

                    //Create a request to the moviedb, open the connection
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    //read data
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {
                        return null;
                    }

                    movieJsonStr = buffer.toString();
                    Log.v("MovieFragment", movieJsonStr);
                } catch (IOException e) {
                    return null;
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }

                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e("MovieFragment", "Error closing stream", e);
                        }
                    }
                }

                try {
                    return getData(movieJsonStr);
                } catch (JSONException e) {
                    Log.e("Popular Movie", e.getMessage(), e);
                    e.printStackTrace();
                }
                return null;
            }


        @Override
        protected void onPostExecute(ArrayList<AndroidFlavor> result) {
            if(result != null){
                flavorAdapter.clear();
                for(AndroidFlavor movieStr : result){
                    flavorAdapter.add(movieStr);
                }
            }

        }
    }

    private ArrayList<AndroidFlavor> getData(String movieData) throws JSONException{
        final String DATA_RESULT ="results";
        final String DATA_TITLE ="title";
        final String DATA_DATE="release_date";
        final String DATA_RATE="vote_average";
        final String DATA_OVERVIEW="overview";
        final String DATA_POSTER="poster_path";

        JSONObject movieJSON = new JSONObject(movieData);
        JSONArray movieArray = movieJSON.getJSONArray(DATA_RESULT);

        ArrayList<AndroidFlavor> result = new ArrayList<>();

        for(int i = 0; i<movieArray.length();i++){
            String title;
            String release;
            double rate;
            String overview;
            String posterPath;

            //get the json object
            JSONObject movie = movieArray .getJSONObject(i);

            //get the title
            title = movie.getString(DATA_TITLE);

            //get the release;
            release = movie.getString(DATA_DATE);

            //get rate;

            rate = movie.getDouble(DATA_RATE);

            //get description
            overview = movie.getString(DATA_OVERVIEW);

            //get image link
            posterPath = movie.getString(DATA_POSTER);


            result.add(new AndroidFlavor(title,  release, rate, overview, posterPath));

        }
        for(AndroidFlavor a : result){
            Log.v("Popular movie", a.toString());
        }
        return result;
    }


}
