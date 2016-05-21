package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailActivityFragment())
                    .commit();
        }


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
    public static class DetailActivityFragment extends Fragment {

        public DetailActivityFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            Intent intent = getActivity().getIntent();
            ((TextView) rootView.findViewById(R.id.detail_title))
                    .setText("hahahahaha");

            if (intent != null && intent.hasExtra("movie")) {
                AndroidFlavor movie = (AndroidFlavor) intent.getParcelableExtra("movie");
                Log.v("bad", movie.toString());
              //  ((TextView) rootView.findViewById(R.id.detail_title))
               //         .setText("hahahahaha");
                Log.v("bad", movie.titleToStr());
                //((TextView) rootView.findViewById(R.id.detail_release))
                //       .setText(movie.titleToStr());

                //((TextView) rootView.findViewById(R.id.detail_rate))
                //        .setText(movie.rateToStr());
           ;
                //((TextView) rootView.findViewById(R.id.detail_overview))
                 //       .setText(movie.descriptionToStr());

                //String url = "http://image.tmdb.org/t/p/w185" + movie.imageToStr();
                //Log.v("pop", url);
                //ImageView iconView = (ImageView) rootView.findViewById(R.id.detail_image);
                //Picasso.with(getActivity()).load(url).into(iconView);
            }
            return rootView;
        }
    }

}