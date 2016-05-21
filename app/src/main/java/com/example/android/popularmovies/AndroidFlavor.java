package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by kevin on 5/15/2016.
 */
public class AndroidFlavor implements Parcelable{
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

    public String titleToStr(){
        return movieTitle;
    }

    public String dateToStr(){
        return movieDate;
    }

    public String rateToStr(){
        String rate = Double.toString(Math.round(movieRate));
        return rate;
    }

    public String descriptionToStr(){
        return movieDescription;
    }

    public String imageToStr(){
        return image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieTitle);
        dest.writeString(movieDate);
        dest.writeDouble(movieRate);
        dest.writeString(movieDescription);
        dest.writeString(image);
    }

    public static final Parcelable.Creator<AndroidFlavor> CREATOR = new Parcelable.Creator<AndroidFlavor>() {
        public AndroidFlavor createFromParcel(Parcel in) {
            return new AndroidFlavor(in);
        }

        public AndroidFlavor[] newArray(int size) {
            return new AndroidFlavor[size];
        }
    };


    private AndroidFlavor(Parcel in) {
        movieTitle = in.readString();
        movieRate = in.readDouble();
        movieDate = in.readString();
        movieDescription = in.readString();
        image = in.readString();
    }
}

