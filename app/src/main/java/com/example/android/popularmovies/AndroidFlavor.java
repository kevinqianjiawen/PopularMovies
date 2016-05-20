package com.example.android.popularmovies;

/**
 * Created by kevin on 5/15/2016.
 */
public class AndroidFlavor {
    String movieTitle;
    String movieDate;
    double movieRate;
    String movieDescription;
    String image;
    // drawable reference id

    public AndroidFlavor(String mTitle, String mDate, double mRate, String mDescription, String image)
    {
        this.movieTitle = mTitle;
        this.movieDate = mDate;
        this.movieRate = mRate;
        this.movieDescription = mDescription;
        this.image = image;
    }

    public String toString(){
        return movieTitle + "-" + movieDate + "-" + movieRate + "&" + movieDescription + "&" + image;
    }

}
