package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.IntegerRes;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.android.popularmovies.data.MovieContract;

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
import java.util.List;

/**
 * Created by kevin on 8/6/2016.
 */
public class FetchDataTask extends AsyncTask<String, Void, ArrayList<AndroidFlavor>> {

    private AndroidFlavorAdapter flavorAdapter;
    private final Context mContext;



    public FetchDataTask(Context context, AndroidFlavorAdapter mflavorAdapter) {
        mContext = context;
        flavorAdapter = mflavorAdapter;
    }

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

            //Log.v("Popular movie", uri.toString());

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
            //Log.v("MovieFragment", movieJsonStr);
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
        if (result != null) {
            flavorAdapter.clear();
            for (AndroidFlavor movieStr : result) {
                flavorAdapter.add(movieStr);
            }
        }

    }


    private ArrayList<AndroidFlavor> getData(String movieData) throws JSONException {
        final String DATA_RESULT = "results";
        final String DATA_ID = "id";
        final String DATA_TITLE = "title";
        final String DATA_DATE = "release_date";
        final String DATA_RATE = "vote_average";
        final String DATA_OVERVIEW = "overview";
        final String DATA_POSTER = "poster_path";

        JSONObject movieJSON = new JSONObject(movieData);
        JSONArray movieArray = movieJSON.getJSONArray(DATA_RESULT);

        ArrayList<AndroidFlavor> result = new ArrayList<>();

        for (int i = 0; i < movieArray.length(); i++) {

            String title;
            String release;
            double rate;
            String overview;
            String posterPath;

            //get the json object
            JSONObject movie = movieArray.getJSONObject(i);

            //get the movie id
            int id = movie.getInt(DATA_ID);

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

            List<AndroidFlavor.video> videoList = FetchVideoData(Integer.toString(id));
            List<AndroidFlavor.review> reviewList = FetchReviewData(Integer.toString(id));




            result.add(new AndroidFlavor(id, title, release, rate, overview, posterPath, videoList, reviewList));

        }
        return result;
    }


    //get video data
    public List<AndroidFlavor.video> FetchVideoData(String id) {
        //get the data from the internet

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            //store the Json string from the internet
            String movieJsonStr = null;


            try {
                String baseUrl = "https://api.themoviedb.org/3/movie/"+ id + "/";
                String API_PARAM = "api_key";

                Uri uri = Uri.parse(baseUrl).buildUpon().appendPath("videos")
                        .appendQueryParameter(API_PARAM, BuildConfig.POPULAR_MOVIES_API_KEY)
                        .build();

                URL url = new URL(uri.toString());

                //Log.v("Popular movie", uri.toString());

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
                //Log.v("MovieFragment", movieJsonStr);
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
                    return getVideoData(movieJsonStr);

                //return getData(movieJsonStr);
            } catch (JSONException e) {
                Log.e("Popular Movie", e.getMessage(), e);
                e.printStackTrace();
            }

        return null;
    }

    //get video data
    public List<AndroidFlavor.review> FetchReviewData(String id) {
        //get the data from the internet
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            //store the Json string from the internet
            String movieJsonStr = null;


            try {
                String baseUrl = "https://api.themoviedb.org/3/movie/" + id + "/";
                String API_PARAM = "api_key";

                Uri uri = Uri.parse(baseUrl).buildUpon().appendPath("reviews")
                        .appendQueryParameter(API_PARAM, BuildConfig.POPULAR_MOVIES_API_KEY)
                        .build();

                URL url = new URL(uri.toString());

                //Log.v("Popular movie", uri.toString());

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
                //Log.v("MovieFragment", movieJsonStr);
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
                return getReviewData(movieJsonStr);

                //return getData(movieJsonStr);
            } catch (JSONException e) {
                Log.e("Popular Movie", e.getMessage(), e);
                e.printStackTrace();
            }

        return null;
    }

    private ArrayList<AndroidFlavor.video> getVideoData(String movieData) throws JSONException {
        final String DATA_RESULT = "results";
        final String DATA_KEY = "key";
        final String DATA_NAME = "name";
        final String DATA_TYPE ="type";

        JSONObject movieJSON = new JSONObject(movieData);
        JSONArray movieArray = movieJSON.getJSONArray(DATA_RESULT);

        ArrayList<AndroidFlavor.video> result = new ArrayList<>();


        String video;
        String name;
        String type;

        for (int i = 0; i < movieArray.length(); i++) {
            //get the json object
            JSONObject movieVideo = movieArray.getJSONObject(i);

            name = movieVideo.getString(DATA_NAME);

            video = movieVideo.getString(DATA_KEY);

            type = movieVideo.getString(DATA_TYPE);


            result.add(new AndroidFlavor.video(name, video, type));
        }
        return result;

    }

    private ArrayList<AndroidFlavor.review> getReviewData(String movieData) throws JSONException {
        final String DATA_RESULT = "results";
        final String DATA_AUTHOR = "author";
        final String DATA_CONTENT = "content";

        JSONObject movieJSON = new JSONObject(movieData);
        JSONArray movieArray = movieJSON.getJSONArray(DATA_RESULT);

        ArrayList<AndroidFlavor.review> result = new ArrayList<>();


        String author;
        String content;

        for (int i = 0; i < movieArray.length(); i++) {
            //get the json object
            JSONObject movieVideo = movieArray.getJSONObject(i);

            author = movieVideo.getString(DATA_AUTHOR);

            content = movieVideo.getString(DATA_CONTENT);


            result.add(new AndroidFlavor.review(author, content));
        }

        return result;

    }



}