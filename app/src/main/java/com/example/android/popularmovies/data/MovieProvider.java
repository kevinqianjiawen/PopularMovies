package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import java.security.Provider;


public class MovieProvider extends ContentProvider{
	private static final String LOG_TAG = MovieProvider.class.getSimpleName();
	private static final UriMatcher sUriMatcher = buildUriMatcher();
	private MovieDBHelper mOpenHelper;

	// Codes for the UriMatcher //////
	private static final int MOVIE = 100;
	private static final int MOVIE_WITH_ID = 200;
	private static final int VIDEO = 300;
	private static final int MOVIE_VIDEO_WITH__ID = 400;
	private static final int REVIEW = 500;
	private static final int MOVIE_REVIEW_WITH_ID = 450;
	private static final int MOVIE_REVIEW_VIDEO_WITH_ID = 600;


	static SQLiteQueryBuilder joinTable(String movietable, String reviewtable, String videotable,  String movieid, String reviewid, String videoid) {
		SQLiteQueryBuilder MovieVideoQueryBuilder = new SQLiteQueryBuilder();

		//This is an inner join which looks like
		//weather INNER JOIN location ON weather.location_id = location._id
		MovieVideoQueryBuilder.setTables(
				movietable + " LEFT OUTER JOIN " +
						reviewtable +
						" ON " + movietable +
						"." + movieid +
						" = " + reviewtable +
						"." + reviewid +
						" LEFT OUTER JOIN " +
						videotable +
						" ON " + movietable +
						"." + movieid +
						" = " +videotable +
						"." + videoid);
		MovieVideoQueryBuilder.setDistinct(true);
		return MovieVideoQueryBuilder;
	}

	////////

	private static UriMatcher buildUriMatcher(){
		// Build a UriMatcher by adding a specific code to return based on a match
		// It's common to use NO_MATCH as the code for this case.
		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		final String authority = MovieContract.CONTENT_AUTHORITY;

		// add a code for each type of URI you want
		matcher.addURI(authority, MovieContract.MovieEntry.TABLE_NAME, MOVIE);
		matcher.addURI(authority, MovieContract.MovieEntry.TABLE_NAME + "/#", MOVIE_WITH_ID);
		matcher.addURI(authority, MovieContract.VideoEntry.TABLE_NAME , VIDEO);
		matcher.addURI(authority, MovieContract.MovieEntry.TABLE_NAME +"/*/#", MOVIE_VIDEO_WITH__ID);
		matcher.addURI(authority, MovieContract.ReviewEntry.TABLE_NAME , REVIEW);
		matcher.addURI(authority, MovieContract.MovieEntry.TABLE_NAME + "/*/*/#", MOVIE_REVIEW_VIDEO_WITH_ID);

		return matcher;
	}

	@Override
	public boolean onCreate(){
		mOpenHelper = new MovieDBHelper(getContext());

		return true;
	}

	@Override
	public String getType(Uri uri){
		final int match = sUriMatcher.match(uri);

		switch (match){
			case MOVIE:{
				return MovieContract.MovieEntry.CONTENT_DIR_TYPE;
			}
			case MOVIE_WITH_ID:{
				return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
			}
			case VIDEO:{
				return MovieContract.VideoEntry.CONTENT_DIR_TYPE;
			}
			case MOVIE_VIDEO_WITH__ID:{
				return MovieContract.MovieEntry.CONTENT_DIR_TYPE;
			}
			case REVIEW:{
				return MovieContract.VideoEntry.CONTENT_DIR_TYPE;
			}
			case MOVIE_REVIEW_VIDEO_WITH_ID:{
				return MovieContract.MovieEntry.CONTENT_DIR_TYPE;
			}
			default:{
				throw new UnsupportedOperationException("Unknown uri: " + uri);
			}
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
		Cursor retCursor;
		switch(sUriMatcher.match(uri)){
			// All Flavors selected
			case MOVIE:{
				retCursor = mOpenHelper.getReadableDatabase().query(
						MovieContract.MovieEntry.TABLE_NAME,
						projection,
						selection,
						selectionArgs,
						null,
						null,
						sortOrder);
				break;
			}
			// Individual flavor based on Id selected
			case MOVIE_WITH_ID:{
				retCursor = mOpenHelper.getReadableDatabase().query(
						MovieContract.MovieEntry.TABLE_NAME,
						projection,
						MovieContract.MovieEntry.COLUMN_ID + " = ?",
						new String[] {String.valueOf(ContentUris.parseId(uri))},
						MovieContract.VideoEntry.COLUMN_KEY,
						null,
						sortOrder);
				break;
			}
			case REVIEW:{
				retCursor = mOpenHelper.getReadableDatabase().query(
						MovieContract.ReviewEntry.TABLE_NAME,
						projection,
						selection,
						selectionArgs,
						null,
						null,
						sortOrder);
				break;
			}case VIDEO: {
				retCursor = mOpenHelper.getReadableDatabase().query(
						MovieContract.VideoEntry.TABLE_NAME,
						projection,
						selection,
						selectionArgs,
						null,
						null,
						sortOrder);
				break;
			}
			case MOVIE_REVIEW_VIDEO_WITH_ID:{
				retCursor = joinTable(MovieContract.MovieEntry.TABLE_NAME, MovieContract.ReviewEntry.TABLE_NAME, MovieContract.VideoEntry.TABLE_NAME, MovieContract.MovieEntry.COLUMN_ID, MovieContract.ReviewEntry.COLUMN_MOVIE_ID, MovieContract.VideoEntry.COLUMN_MOVIE_ID).query(
						mOpenHelper.getReadableDatabase(),
						projection,
						MovieContract.MovieEntry.TABLE_NAME+"."+ MovieContract.MovieEntry.COLUMN_ID+" = ? ",
						new String[]{String.valueOf(ContentUris.parseId(uri))},
						null,
						null,
						sortOrder);
				break;

			}
			default:{
				// By default, we assume a bad URI
				throw new UnsupportedOperationException("Unknown uri: " + uri);
			}
		}

		retCursor.setNotificationUri(getContext().getContentResolver(), uri);
		return retCursor;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values){
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		Uri returnUri;
		switch (sUriMatcher.match(uri)) {
			case MOVIE: {
				long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
				// insert unless it is already contained in the database
				if (_id > 0) {
					returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
				} else {
					throw new android.database.SQLException("Failed to insert row into: " + uri);
				}
				break;
			}

			case REVIEW:{
				long _id = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, values);
				// insert unless it is already contained in the database
				if (_id > 0) {
					returnUri = MovieContract.ReviewEntry.buildReviewUri(_id);
				} else {
					throw new android.database.SQLException("Failed to insert row into: " + uri);
				}
				break;
			}

			case VIDEO:{
				long _id = db.insert(MovieContract.VideoEntry.TABLE_NAME, null, values);
				// insert unless it is already contained in the database
				if (_id > 0) {
					returnUri = MovieContract.VideoEntry.buildVideoUri(_id);
				} else {
					throw new android.database.SQLException("Failed to insert row into: " + uri);
				}
				break;
			}

			default: {
				throw new UnsupportedOperationException("Unknown uri: " + uri);

			}
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return returnUri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs){
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);
		int numDeleted;
		switch(match){
			case MOVIE:
				numDeleted = db.delete(
						MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
				// reset _ID
				db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
						MovieContract.MovieEntry.TABLE_NAME + "'");
				break;
			case MOVIE_WITH_ID:
				numDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME,
						MovieContract.MovieEntry.COLUMN_ID + " = ?",
						new String[]{String.valueOf(ContentUris.parseId(uri))});
				// reset _ID
				db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
						MovieContract.MovieEntry.TABLE_NAME + "'");

				break;

			case REVIEW:
				numDeleted = db.delete(
						MovieContract.ReviewEntry.TABLE_NAME, selection, selectionArgs);
				// reset _ID
				db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
						MovieContract.ReviewEntry.TABLE_NAME + "'");
				break;

			case VIDEO:
				numDeleted = db.delete(
						MovieContract.VideoEntry.TABLE_NAME, selection, selectionArgs);
				// reset _ID
				db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
						MovieContract.VideoEntry.TABLE_NAME + "'");
				break;

			case MOVIE_REVIEW_WITH_ID:
				numDeleted = db.delete(
						MovieContract.ReviewEntry.TABLE_NAME, MovieContract.ReviewEntry.COLUMN_MOVIE_ID + "= ?", new String[]{String.valueOf(ContentUris.parseId(uri))});
				// reset _ID
				db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
						MovieContract.ReviewEntry.TABLE_NAME + "'");
				break;

			case MOVIE_VIDEO_WITH__ID:
				numDeleted = db.delete(
						MovieContract.VideoEntry.TABLE_NAME, MovieContract.VideoEntry.COLUMN_MOVIE_ID + "= ?", new String[]{String.valueOf(ContentUris.parseId(uri))});
				// reset _ID
				db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
						MovieContract.VideoEntry.TABLE_NAME + "'");
				break;
			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}

		return numDeleted;
	}

	@Override
	public int bulkInsert(Uri uri, ContentValues[] values){
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);

		// keep track of successful inserts
		int numInserted = 0;

		switch(match){
			case MOVIE:
				// allows for multiple transactions
				db.beginTransaction();

				try{
					for(ContentValues value : values){
						if (value == null){
							throw new IllegalArgumentException("Cannot have null content values");
						}
						long _id = -1;
						try{
							_id = db.insertOrThrow(MovieContract.MovieEntry.TABLE_NAME,
									null, value);
						}catch(SQLiteConstraintException e) {
							Log.w(LOG_TAG, "Attempting to insert " +
									value.getAsString(
											MovieContract.MovieEntry._ID)
									+ " but value is already in database.");
						}
						if (_id != -1){
							numInserted++;
						}
					}
					if(numInserted > 0){
						// If no errors, declare a successful transaction.
						// database will not populate if this is not called
						db.setTransactionSuccessful();
					}
				} finally {
					// all transactions occur at once
					db.endTransaction();
				}
				if (numInserted > 0){
					// if there was successful insertion, notify the content resolver that there
					// was a change
					getContext().getContentResolver().notifyChange(uri, null);
				}
				return numInserted;

			case REVIEW:
				// allows for multiple transactions
				db.beginTransaction();

				try{
					for(ContentValues value : values){
						if (value == null){
							throw new IllegalArgumentException("Cannot have null content values");
						}
						long _id = -1;
						try{
							_id = db.insertOrThrow(MovieContract.ReviewEntry.TABLE_NAME,
									null, value);
						}catch(SQLiteConstraintException e) {
							Log.w(LOG_TAG, "Attempting to insert " +
									value.getAsString(
											MovieContract.ReviewEntry._ID)
									+ " but value is already in database.");
						}
						if (_id != -1){
							numInserted++;
						}
					}
					if(numInserted > 0){
						// If no errors, declare a successful transaction.
						// database will not populate if this is not called
						db.setTransactionSuccessful();
					}
				} finally {
					// all transactions occur at once
					db.endTransaction();
				}
				if (numInserted > 0){
					// if there was successful insertion, notify the content resolver that there
					// was a change
					getContext().getContentResolver().notifyChange(uri, null);
				}
				return numInserted;

			case VIDEO:
				// allows for multiple transactions
				db.beginTransaction();

				try{
					for(ContentValues value : values){
						if (value == null){
							throw new IllegalArgumentException("Cannot have null content values");
						}
						long _id = -1;
						try{
							_id = db.insertOrThrow(MovieContract.VideoEntry.TABLE_NAME,
									null, value);
						}catch(SQLiteConstraintException e) {
							Log.w(LOG_TAG, "Attempting to insert " +
									value.getAsString(
											MovieContract.VideoEntry._ID)
									+ " but value is already in database.");
						}
						if (_id != -1){
							numInserted++;
						}
					}
					if(numInserted > 0){
						// If no errors, declare a successful transaction.
						// database will not populate if this is not called
						db.setTransactionSuccessful();
					}
				} finally {
					// all transactions occur at once
					db.endTransaction();
				}
				if (numInserted > 0){
					// if there was successful insertion, notify the content resolver that there
					// was a change
					getContext().getContentResolver().notifyChange(uri, null);
				}
				return numInserted;

			default:
				return super.bulkInsert(uri, values);
		}
	}

	@Override
	public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs){
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int numUpdated = 0;

		if (contentValues == null){
			throw new IllegalArgumentException("Cannot have null content values");
		}

		switch(sUriMatcher.match(uri)){
			case MOVIE:{
				numUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME,
						contentValues,
						selection,
						selectionArgs);
				break;
			}
			case MOVIE_WITH_ID: {
				numUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME,
						contentValues,
						MovieContract.MovieEntry.COLUMN_ID+ " = ?",
						new String[] {String.valueOf(ContentUris.parseId(uri))});
				break;
			}

			case REVIEW:{
				numUpdated = db.update(MovieContract.ReviewEntry.TABLE_NAME,
						contentValues,
						selection,
						selectionArgs);
				break;
			}

			case VIDEO:{
				numUpdated = db.update(MovieContract.VideoEntry.TABLE_NAME,
						contentValues,
						selection,
						selectionArgs);
				break;
			}

			case MOVIE_REVIEW_WITH_ID: {
				numUpdated = db.update(MovieContract.ReviewEntry.TABLE_NAME,
						contentValues,
						MovieContract.ReviewEntry.COLUMN_MOVIE_ID + " = ?",
						new String[] {String.valueOf(ContentUris.parseId(uri))});
				break;
			}

			case MOVIE_VIDEO_WITH__ID: {
				numUpdated = db.update(MovieContract.VideoEntry.TABLE_NAME,
						contentValues,
						MovieContract.VideoEntry.COLUMN_MOVIE_ID + " = ?",
						new String[] {String.valueOf(ContentUris.parseId(uri))});
				break;
			}


			default:{
				throw new UnsupportedOperationException("Unknown uri: " + uri);
			}
		}

		if (numUpdated > 0){
			getContext().getContentResolver().notifyChange(uri, null);
		}

		return numUpdated;
	}

}
