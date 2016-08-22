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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kevin on 8/12/2016.
 */
public class DetailActivityFragment extends Fragment{
    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    @BindView(R.id.moviebar) Toolbar toolbar;
    @BindView(R.id.detail_title) TextView title;
    @BindView(R.id.detail_release) TextView release;
    @BindView(R.id.detail_rate) TextView rate;
    @BindView(R.id.detail_overview) TextView overview;
    @BindView(R.id.detail_image) ImageView iconView;
    @BindView(R.id.preview) ImageView topPreview;
    @BindView(R.id.recyclerview_video) RecyclerView videoList;
    @BindView(R.id.recyclerview_review) RecyclerView reviewList;
    @BindView(R.id.action_button) FloatingActionButton checkBox;



    static final String DETAIL = "DETAIL";
    private AndroidFlavor movie;



    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        ButterKnife.bind(this, rootView);

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
            }


            final int idText = movie.getid();

            final String titleText = movie.titleToStr();
            title.setText(titleText);

            final String releaseText = movie.dateToStr();
            release.setText(releaseText);

            final double rateText = movie.rateGet();
            rate.setText("" + rateText);

            final String descriptionText = movie.descriptionToStr();
            overview.setText(descriptionText);

            final String url = "http://image.tmdb.org/t/p/w500" + movie.imageToStr();

            Picasso.with(getActivity()).load(url).into(iconView);

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
                topPreview.setImageResource(R.drawable.no_video);
            }



            videoList.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            videoList.setLayoutManager(llm);
            videoList.setAdapter(new VideoAdapter(movie.getVideoList()));


            reviewList.setHasFixedSize(true);
            LinearLayoutManager llm2 = new LinearLayoutManager(getContext());
            reviewList.setLayoutManager(llm2);
            reviewList.setAdapter(new ReviewAdapter(movie.getReviewList()));










            //a star check box, if the user click it, the favorite will add to the database;
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

                        int videoSize = movie.getVideoList().size();
                        Vector<ContentValues> videoValueList = new Vector<ContentValues>(videoSize);

                        for(int i = 0; i<videoSize; i++) {
                            ContentValues videoValue = new ContentValues();
                            videoValue.put(MovieContract.VideoEntry.COLUMN_KEY,movie.getVideoList().get(i).getKey());
                            videoValue.put(MovieContract.VideoEntry.COLUMN_NAME,movie.getVideoList().get(i).getName());
                            videoValue.put(MovieContract.VideoEntry.COLUMN_TYPE,movie.getVideoList().get(i).getType());
                            videoValue.put(MovieContract.VideoEntry.COLUMN_MOVIE_ID,idText);
                            videoValueList.add(videoValue);
                        }

                        // add to database
                        if ( videoValueList.size() > 0 ) {
                            ContentValues[] videoArray = new ContentValues[videoValueList.size()];
                            videoValueList.toArray(videoArray);
                            getContext().getContentResolver().bulkInsert(MovieContract.VideoEntry.CONTENT_URI, videoArray);
                        }


                        int reviewSize = movie.getReviewList().size();
                        Vector<ContentValues> reviewValueList = new Vector<ContentValues>(reviewSize);

                        for(int i = 0; i<reviewSize; i++) {
                            ContentValues reviewValue = new ContentValues();
                            reviewValue.put(MovieContract.ReviewEntry.COLUMN_AUTHOR,movie.getReviewList().get(i).getAuthor());
                            reviewValue.put(MovieContract.ReviewEntry.COLUMN_REVIEW, movie.getReviewList().get(i).getContent());
                            reviewValue.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID,idText);
                            reviewValueList.add(reviewValue);
                        }


                        // add to database
                        if ( reviewValueList.size() > 0 ) {
                            ContentValues[] reviewArray = new ContentValues[reviewValueList.size()];
                            reviewValueList.toArray(reviewArray);
                            getContext().getContentResolver().bulkInsert(MovieContract.ReviewEntry.CONTENT_URI, reviewArray);
                        }



                        Log.v("DATABASE", "Insert complete");




                    }else {

                        getContext().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, "movie_id=?", new String[]{String.valueOf(idText)});
                        getContext().getContentResolver().delete(MovieContract.ReviewEntry.CONTENT_URI, "id=?", new String[]{String.valueOf(idText)});
                        getContext().getContentResolver().delete(MovieContract.VideoEntry.CONTENT_URI, "id=?", new String[]{String.valueOf(idText)});
                        Log.v("DATABASE", "Delete complete");

                    }
                }
                }
            );

        }


        return rootView;
    }




}
