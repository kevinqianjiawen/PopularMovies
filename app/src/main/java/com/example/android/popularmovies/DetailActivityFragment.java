package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * Created by kevin on 8/12/2016.
 */
public class DetailActivityFragment extends Fragment{
    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();



    static final String DETAIL = "DETAIL";
    private AndroidFlavor movie;



    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        final Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.moviebar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getActivity().getIntent();

        Bundle arguments = getArguments();
        if (arguments != null){
            movie = arguments.getParcelable(DetailActivityFragment.DETAIL);
        }

        if ((intent != null && intent.hasExtra("movie") ) || movie != null) {
            if(movie == null) {
                movie = (AndroidFlavor) intent.getParcelableExtra("movie");
                //Log.v("bad", movie.toString());
            }


            final int idText = movie.getid();

            TextView title = (TextView) rootView.findViewById(R.id.detail_title);
            final String titleText = movie.titleToStr();
            title.setText(titleText);

            TextView release = (TextView) rootView.findViewById(R.id.detail_release);
            final String releaseText = movie.dateToStr();
            release.setText(releaseText);


            TextView rate = (TextView) rootView.findViewById(R.id.detail_rate);
            final double rateText = movie.rateGet();
            rate.setText("" + rateText);

            TextView overview = (TextView) rootView.findViewById(R.id.detail_overview);
            final String descriptionText = movie.descriptionToStr();
            overview.setText(descriptionText);

            final String url = "http://image.tmdb.org/t/p/w500" + movie.imageToStr();
            //Log.v("pop", url);
            ImageView iconView = (ImageView) rootView.findViewById(R.id.detail_image);
            Picasso.with(getActivity()).load(url).into(iconView);

            ImageView topPreview = (ImageView) rootView.findViewById(R.id.preview);
            try{
            final String previewKey = movie.getVideoList().get(0).getKey();

            final String urlPreview = "http://img.youtube.com/vi/"+ previewKey + "/0.jpg";
            Picasso.with(getContext()).load(urlPreview).into(topPreview);

            topPreview.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + previewKey));
                    if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                        getContext().startActivity(intent);
                    }
                }
            });}catch (IndexOutOfBoundsException iobe){
                topPreview.setImageResource(R.drawable.novideo);
            }

            RecyclerView videoList = (RecyclerView) rootView.findViewById(R.id.recyclerview_video);
            videoList.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            videoList.setLayoutManager(llm);
            videoList.setAdapter(new VideoAdapter(movie.getVideoList()));

//            TextView test = (TextView) rootView.findViewById(R.id.author);
//            final String auother = movie.getReviewList().get(0).getAuthor();
//            test.setText(auother);
//
//
//            TextView test2 = (TextView) rootView.findViewById(R.id.review);
//            final String content = movie.getReviewList().get(0).getContent().substring(0, 100);
//            test2.setText(content);

            RecyclerView reviewList = (RecyclerView) rootView.findViewById(R.id.recyclerview_review);
            reviewList.setHasFixedSize(true);
            LinearLayoutManager llm2 = new LinearLayoutManager(getContext());
            reviewList.setLayoutManager(llm2);
            reviewList.setAdapter(new ReviewAdapter(movie.getReviewList()));












            //a star check box, if the user click it, the favorite will add to the database;
            final com.example.android.popularmovies.FloatingActionButton checkBox = (com.example.android.popularmovies.FloatingActionButton) rootView.findViewById(R.id.action_button);
            checkBox.setOnCheckedChangeListener(new com.example.android.popularmovies.FloatingActionButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(com.example.android.popularmovies.FloatingActionButton fabView, boolean isChecked) {
                    if (isChecked){

                        ContentValues movieValues = new ContentValues();

                        movieValues.put(MovieContract.MovieEntry.COLUMN_ID, idText);
                        movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, titleText);
                        movieValues.put(MovieContract.MovieEntry.COLUMN_DATE, releaseText);
                        movieValues.put(MovieContract.MovieEntry.COLUMN_RATING, rateText);
                        movieValues.put(MovieContract.MovieEntry.COLUMN_DESCRIPTION, descriptionText);
                        movieValues.put(MovieContract.MovieEntry.COLUMN_IMAGE, url);

                        getContext().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, movieValues);

                        Log.v("DATABASE", "Insert complete");



                    }else {

                        getContext().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, "movie_id=?", new String[]{String.valueOf(idText)});
                        Log.v("DATABASE", "Delete complete");

                    }
                }
                }
            );

        }


        return rootView;
    }




}
