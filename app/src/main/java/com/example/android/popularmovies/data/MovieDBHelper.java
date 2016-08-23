package com.example.android.popularmovies.data;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class MovieDBHelper extends SQLiteOpenHelper {
	public static final String LOG_TAG = MovieDBHelper.class.getSimpleName();

	//name & version
	private static final String DATABASE_NAME = "movie.db";
	private static final int DATABASE_VERSION = 18;

	public MovieDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Create the database
	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
				MovieContract.MovieEntry.TABLE_NAME + "(" + MovieContract.MovieEntry._ID +
				" INTEGER PRIMARY KEY AUTOINCREMENT, " +
				MovieContract.MovieEntry.COLUMN_ID + " INTEGER NOT NULL, " +
				MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
				MovieContract.MovieEntry.COLUMN_DESCRIPTION +
				" TEXT NOT NULL, " +
				MovieContract.MovieEntry.COLUMN_RATING +
				" REAL NOT NULL, " +
				MovieContract.MovieEntry.COLUMN_DATE +
				" DATE NOT NULL, " +
				MovieContract.MovieEntry.COLUMN_IMAGE +
				" TEXT NOT NULL"
				+");";

		final String SQL_CREATE_VIDEO_TABLE = "CREATE TABLE " +
				MovieContract.VideoEntry.TABLE_NAME + "(" + MovieContract.VideoEntry._ID +
				" INTEGER PRIMARY KEY AUTOINCREMENT, " +
				MovieContract.VideoEntry.COLUMN_NAME + " Text NOT NULL, " +
				MovieContract.VideoEntry.COLUMN_KEY + " TEXT NOT NULL, " +
				MovieContract.VideoEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
				MovieContract.VideoEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
				" FOREIGN KEY (" + MovieContract.VideoEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
				MovieContract.MovieEntry.TABLE_NAME + " (" + MovieContract.MovieEntry.COLUMN_ID + ")"

				+");";

        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " +
                MovieContract.ReviewEntry.TABLE_NAME + "(" + MovieContract.ReviewEntry._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.ReviewEntry.COLUMN_AUTHOR + " Text NOT NULL, " +
                MovieContract.ReviewEntry.COLUMN_REVIEW + " TEXT NOT NULL, " +
				MovieContract.ReviewEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
				" FOREIGN KEY (" + MovieContract.ReviewEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
				MovieContract.MovieEntry.TABLE_NAME + " (" + MovieContract.MovieEntry.COLUMN_ID + ")"

                +");";

		sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
		sqLiteDatabase.execSQL(SQL_CREATE_VIDEO_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEW_TABLE);
	}

	// Upgrade database when version is changed.
	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
		Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
				newVersion + ". OLD DATA WILL BE DESTROYED");
		// Drop the table
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.VideoEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.ReviewEntry.TABLE_NAME);



		// re-create database
		onCreate(sqLiteDatabase);
	}
}
