package com.example.sonic.fspotter.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.example.sonic.fspotter.logging.L;
import com.example.sonic.fspotter.pojo.Location;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Windows on 25-02-2015.
 */
public class DBLocations {
    public static final int BOX_OFFICE = 0;
    public static final int UPCOMING = 1;
    private LocationsHelper mHelper;
    private SQLiteDatabase mDatabase;

    public DBLocations(Context context) {
        mHelper = new LocationsHelper(context);
        mDatabase = mHelper.getWritableDatabase();
    }

    public void insertLocations(int table, ArrayList<Location> listMovies, boolean clearPrevious) {
        if (clearPrevious) {
            deleteLocations(table);
        }


        //create a sql prepared statement
        String sql = "INSERT INTO " + (table == BOX_OFFICE ? LocationsHelper.TABLE_BOX_OFFICE : LocationsHelper.TABLE_UPCOMING) + " VALUES (?,?,?,?,?,?,?,?,?,?);";
        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (int i = 0; i < listMovies.size(); i++) {
            Location currentLocation = listMovies.get(i);
            statement.clearBindings();
            //for a given column index, simply bind the data to be put inside that index
            statement.bindString(2, currentLocation.getTitle());
            statement.bindLong(3, currentLocation.getReleaseDateTheater() == null ? -1 : currentLocation.getReleaseDateTheater().getTime());
            statement.bindLong(4, currentLocation.getAudienceScore());
            statement.bindString(5, currentLocation.getSynopsis());
            statement.bindString(6, currentLocation.getUrlThumbnail());
            statement.bindString(7, currentLocation.getUrlSelf());
            statement.bindString(8, currentLocation.getUrlCast());
            statement.bindString(9, currentLocation.getUrlReviews());
            statement.bindString(10, currentLocation.getUrlSimilar());

            statement.execute();
        }
        //set the transaction as successful and end the transaction
        L.m("inserting entries " + listMovies.size() + new Date(System.currentTimeMillis()));
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public ArrayList<Location> readLocations(int table) {
        ArrayList<Location> listMovies = new ArrayList<>();

        //get a list of columns to be retrieved, we need all of them
        String[] columns = {LocationsHelper.COLUMN_UID,
                LocationsHelper.COLUMN_TITLE,
                LocationsHelper.COLUMN_RELEASE_DATE,
                LocationsHelper.COLUMN_AUDIENCE_SCORE,
                LocationsHelper.COLUMN_SYNOPSIS,
                LocationsHelper.COLUMN_URL_THUMBNAIL,
                LocationsHelper.COLUMN_URL_SELF,
                LocationsHelper.COLUMN_URL_CAST,
                LocationsHelper.COLUMN_URL_REVIEWS,
                LocationsHelper.COLUMN_URL_SIMILAR
        };
        Cursor cursor = mDatabase.query((table == BOX_OFFICE ? LocationsHelper.TABLE_BOX_OFFICE : LocationsHelper.TABLE_UPCOMING), columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            L.m("loading entries " + cursor.getCount() + new Date(System.currentTimeMillis()));
            do {

                //create a new location object and retrieve the data from the cursor to be stored in this location object
                Location location = new Location();
                //each step is a 2 part process, find the index of the column first, find the data of that column using
                //that index and finally set our blank location object to contain our data
                location.setTitle(cursor.getString(cursor.getColumnIndex(LocationsHelper.COLUMN_TITLE)));
                long releaseDateMilliseconds = cursor.getLong(cursor.getColumnIndex(LocationsHelper.COLUMN_RELEASE_DATE));
                location.setReleaseDateTheater(releaseDateMilliseconds != -1 ? new Date(releaseDateMilliseconds) : null);
                location.setAudienceScore(cursor.getInt(cursor.getColumnIndex(LocationsHelper.COLUMN_AUDIENCE_SCORE)));
                location.setSynopsis(cursor.getString(cursor.getColumnIndex(LocationsHelper.COLUMN_SYNOPSIS)));
                location.setUrlThumbnail(cursor.getString(cursor.getColumnIndex(LocationsHelper.COLUMN_URL_THUMBNAIL)));
                location.setUrlSelf(cursor.getString(cursor.getColumnIndex(LocationsHelper.COLUMN_URL_SELF)));
                location.setUrlCast(cursor.getString(cursor.getColumnIndex(LocationsHelper.COLUMN_URL_CAST)));
                location.setUrlReviews(cursor.getString(cursor.getColumnIndex(LocationsHelper.COLUMN_URL_REVIEWS)));
                location.setUrlSimilar(cursor.getString(cursor.getColumnIndex(LocationsHelper.COLUMN_URL_SIMILAR)));
                //add the location to the list of location objects which we plan to return
                listMovies.add(location);
            }
            while (cursor.moveToNext());
        }
        return listMovies;
    }

    public void deleteLocations(int table) {
        mDatabase.delete((table == BOX_OFFICE ? LocationsHelper.TABLE_BOX_OFFICE : LocationsHelper.TABLE_UPCOMING), null, null);
    }

    private static class LocationsHelper extends SQLiteOpenHelper {
        public static final String TABLE_UPCOMING = " movies_upcoming";
        public static final String TABLE_BOX_OFFICE = "movies_box_office";
        public static final String COLUMN_UID = "_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_AUDIENCE_SCORE = "audience_score";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_URL_THUMBNAIL = "url_thumbnail";
        public static final String COLUMN_URL_SELF = "url_self";
        public static final String COLUMN_URL_CAST = "url_cast";
        public static final String COLUMN_URL_REVIEWS = "url_reviews";
        public static final String COLUMN_URL_SIMILAR = "url_similar";
        private static final String CREATE_TABLE_BOX_OFFICE = "CREATE TABLE " + TABLE_BOX_OFFICE + " (" +
                COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TITLE + " TEXT," +
                COLUMN_RELEASE_DATE + " INTEGER," +
                COLUMN_AUDIENCE_SCORE + " INTEGER," +
                COLUMN_SYNOPSIS + " TEXT," +
                COLUMN_URL_THUMBNAIL + " TEXT," +
                COLUMN_URL_SELF + " TEXT," +
                COLUMN_URL_CAST + " TEXT," +
                COLUMN_URL_REVIEWS + " TEXT," +
                COLUMN_URL_SIMILAR + " TEXT" +
                ");";
        private static final String CREATE_TABLE_UPCOMING = "CREATE TABLE " + TABLE_UPCOMING + " (" +
                COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TITLE + " TEXT," +
                COLUMN_RELEASE_DATE + " INTEGER," +
                COLUMN_AUDIENCE_SCORE + " INTEGER," +
                COLUMN_SYNOPSIS + " TEXT," +
                COLUMN_URL_THUMBNAIL + " TEXT," +
                COLUMN_URL_SELF + " TEXT," +
                COLUMN_URL_CAST + " TEXT," +
                COLUMN_URL_REVIEWS + " TEXT," +
                COLUMN_URL_SIMILAR + " TEXT" +
                ");";
        private static final String DB_NAME = "movies_db";
        private static final int DB_VERSION = 1;
        private Context mContext;

        public LocationsHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE_BOX_OFFICE);
                db.execSQL(CREATE_TABLE_UPCOMING);
                L.m("create table box office executed");
            } catch (SQLiteException exception) {
                L.t(mContext, exception + "");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                L.m("upgrade table box office executed");
                db.execSQL(" DROP TABLE " + TABLE_BOX_OFFICE + " IF EXISTS;");
                db.execSQL(" DROP TABLE " + TABLE_UPCOMING + " IF EXISTS;");
                onCreate(db);
            } catch (SQLiteException exception) {
                L.t(mContext, exception + "");
            }
        }
    }
}
