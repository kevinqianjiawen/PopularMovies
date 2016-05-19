package com.example.android.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by kevin on 5/15/2016.
 */
public class MainActivityFragment extends Fragment {

    private AndroidFlavorAdapter flavorAdapter;

    AndroidFlavor[] androidFlavors = {
            new AndroidFlavor("Cupcake", "1.5", R.drawable.sample_0),
            new AndroidFlavor("Donut", "1.6", R.drawable.sample_1),
            new AndroidFlavor("Eclair", "2.0-2.1", R.drawable.sample_2),
            new AndroidFlavor("Froyo", "2.2-2.2.3", R.drawable.sample_3),
            new AndroidFlavor("GingerBread", "2.3-2.3.7", R.drawable.sample_4),
            new AndroidFlavor("Honeycomb", "3.0-3.2.6", R.drawable.sample_5),
            new AndroidFlavor("Ice Cream Sandwich", "4.0-4.0.4", R.drawable.sample_6),
            new AndroidFlavor("Jelly Bean", "4.1-4.3.1", R.drawable.sample_7)
    };

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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //inflate menu

        flavorAdapter = new AndroidFlavorAdapter(getActivity(), Arrays.asList(androidFlavors));

        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_poster);
        gridView.setAdapter(flavorAdapter);




        return rootView;
    }

    public class FetchDataTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params){
            //get the data from the internet
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            //store the Json string from the internet
            String movieJsonStr = null;

            try{
                String baseUrl = "https://api.themoviedb.org/3/movie/popular?";
                String apiKey = "&api_key=" + BuildConfig.POPULAR_MOVIES_API_KEY;
                URL url = new URL(baseUrl.concat(apiKey));

                //Create a request to the moviedb, open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //read data
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line = reader.readLine()) != null){
                    buffer.append(line+"\n");
                }

                if(buffer.length()==0){
                    return null;
                }

                movieJsonStr = buffer.toString();
                Log.v("MovieFragment", movieJsonStr);
            } catch(IOException e){
                return null;
            } finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }

                if(reader != null){
                    try{
                        reader.close();
                    } catch (final IOException e){
                        Log.e("MovieFragment", "Error closing stream", e);
                    }
                }
            }
            return null;
        }
    }


}
