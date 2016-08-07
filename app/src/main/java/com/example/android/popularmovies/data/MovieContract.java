package com.example.android.popularmovies.data;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract{

	public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies";

	public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


	//store the title, poster, synopsis, user rating, and release data.
	public static final class MovieEntry implements BaseColumns{
		// table name
		public static final String TABLE_NAME = "movie";
		// columns
		public static final String _ID = "_id";
		public static final String COLUMN_TITLE = "title";
		public static final String COLUMN_DESCRIPTION = "description";
		public static final String COLUMN_RATING = "rating";
		public static final String COLUMN_DATE = "release date";


		// create content uri
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
			.appendPath(TABLE_NAME).build();
		// create cursor of base type directory for multiple entries
		public static final String CONTENT_DIR_TYPE =
				ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
		// create cursor of base type item for single entry
		public static final String CONTENT_ITEM_TYPE =
				ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

		// for building URIs on insertion
		public static Uri buildMovieUri(long id){
        		return ContentUris.withAppendedId(CONTENT_URI, id);
		}
	}
}
