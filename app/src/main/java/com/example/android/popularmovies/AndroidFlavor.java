package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.DecimalFormat;

/**
 * Created by kevin on 5/15/2016.
 */
public class AndroidFlavor implements Parcelable{
    int id;
    String movieTitle;
    String movieDate;
    double movieRate;
    String movieDescription;
    String image;
    // drawable reference id

    public AndroidFlavor(int id, String mTitle, String mDate, double mRate, String mDescription, String image)
    {
        this.id = id;
        this.movieTitle = mTitle;
        this.movieDate = mDate;
        this.movieRate = mRate;
        this.movieDescription = mDescription;
        this.image = image;
    }

    public String toString(){
        return movieTitle + "-" + movieDate + "-" + movieRate + "&" + movieDescription + "&" + image;
    }

    public int getid(){ return id; }

    public String titleToStr(){
        return movieTitle;
    }

    public String dateToStr(){
        return movieDate;
    }

    public double rateGet(){
        //DecimalFormat oneDec = new DecimalFormat();
        //oneDec.setDecimalSeparatorAlwaysShown(false);
        return movieRate;
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
        dest.writeInt(id);
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
        id = in.readInt();
        movieTitle = in.readString();
        movieDate = in.readString();
        movieRate = in.readDouble();
        movieDescription = in.readString();
        image = in.readString();
    }
}

