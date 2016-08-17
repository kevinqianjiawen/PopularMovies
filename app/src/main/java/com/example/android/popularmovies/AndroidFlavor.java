package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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
    List<video> videoList;
    List<review> reviewList;
    // drawable reference id

    public AndroidFlavor(int id, String mTitle, String mDate, double mRate, String mDescription, String image, List<video> videoList, List<review> reviewList)
    {
        this.id = id;
        this.movieTitle = mTitle;
        this.movieDate = mDate;
        this.movieRate = mRate;
        this.movieDescription = mDescription;
        this.image = image;
        this.videoList = videoList;
        this.reviewList = reviewList;


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
        dest.writeTypedList(videoList);
        dest.writeTypedList(reviewList);
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
        this.videoList = new ArrayList<video>();
        in.readTypedList(videoList,video.CREATOR);
        this.reviewList = new ArrayList<review>();
        in.readTypedList(reviewList, review.CREATOR);
    }

    //a inside class for storing video data
    static class video implements  Parcelable{
        String name;
        String key;

        video(Parcel in){
            this.name = in.readString();
            this.key = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeString(key);
        }
        static final Parcelable.Creator<video> CREATOR = new Parcelable.Creator<video>(){
            public video createFromParcel(Parcel in){
                return new video(in);
            }

            public video[] newArray(int size){
                return new video[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        public String getName(){
            return name;
        }

        public String getKey(){
            return key;
        }
    }

    //store review data
    static class review implements  Parcelable{
        String author;
        String content;

        review(Parcel in){
            this.author = in.readString();
            this.content = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(author);
            dest.writeString(content);
        }
        static final Parcelable.Creator<review> CREATOR = new Parcelable.Creator<review>(){
            public review createFromParcel(Parcel in){
                return new review(in);
            }

            public review[] newArray(int size){
                return new review[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        public String getAuthor(){
            return author;
        }

        public String getContent(){
            return content;
        }
    }
}

