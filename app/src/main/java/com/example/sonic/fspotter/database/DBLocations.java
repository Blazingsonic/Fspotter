package com.example.sonic.fspotter.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.example.sonic.fspotter.logging.L;
import com.example.sonic.fspotter.pojo.Comment;
import com.example.sonic.fspotter.pojo.Image;
import com.example.sonic.fspotter.pojo.Location;
import com.example.sonic.fspotter.pojo.Rating;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Windows on 25-02-2015.
 */
public class DBLocations {
    public static final int LOCATIONS = 0;
    public static final int UPCOMING = 1;
    private LocationsHelper mHelper;
    private SQLiteDatabase mDatabase;

    public DBLocations(Context context) {
        mHelper = new LocationsHelper(context);
        mDatabase = mHelper.getWritableDatabase();
    }

    public void insertLocations(int table, ArrayList<Location> listLocations, boolean clearPrevious) {
        if (clearPrevious) {
            deleteLocations(table);
        }

        //create a sql prepared statement
        String sql = "INSERT INTO " + (table == LOCATIONS ? LocationsHelper.TABLE_LOCATIONS : LocationsHelper.TABLE_UPCOMING) + " VALUES (?,?,?,?,?,?,?,?,?,?,?);";
        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (int i = 0; i < listLocations.size(); i++) {
            Location currentLocation = listLocations.get(i);
            statement.clearBindings();
            //for a given column index, simply bind the data to be put inside that index
            statement.bindLong(2, currentLocation.getId());
            statement.bindString(3, currentLocation.getLocationName());
            statement.bindString(4, currentLocation.getDescription());
            statement.bindDouble(5, currentLocation.getLatitude());
            statement.bindDouble(6, currentLocation.getLongitude());
            statement.bindString(7, currentLocation.getHints());
            statement.bindString(8, currentLocation.getMapIconId());
            statement.bindLong(9, currentLocation.getRating());
            statement.bindString(10, currentLocation.getImage());
            statement.bindLong(11, currentLocation.getCreatedAt() == null ? -1 : currentLocation.getCreatedAt().getTime());

            statement.execute();
        }
        //set the transaction as successful and end the transaction
        L.m("inserting entries " + listLocations.size() + new Date(System.currentTimeMillis()));
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public void insertRatings(int table, ArrayList<Rating> listRatings, boolean clearPrevious) {
        if (clearPrevious) {
            deleteRatings();
        }

        //create a sql prepared statement
        String sql = "INSERT INTO location_ratings(id, location_name, rating) VALUES (?,?,?);";
        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (int i = 0; i < listRatings.size(); i++) {
            Rating currentRating = listRatings.get(i);
            statement.clearBindings();
            //for a given column index, simply bind the data to be put inside that index
            statement.bindLong(1, currentRating.getId());
            statement.bindString(2, currentRating.getLocationName());
            statement.bindLong(3, currentRating.getRating());

            statement.execute();
        }
        //set the transaction as successful and end the transaction
        L.m("inserting entries " + listRatings.size() + new Date(System.currentTimeMillis()));
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public void insertComments(int table, ArrayList<Comment> listComments, boolean clearPrevious) {
        if (clearPrevious) {
            deleteComments();
        }

        //create a sql prepared statement
        String sql = "INSERT INTO location_comments(id, location_name, comment) VALUES (?,?,?);";
        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (int i = 0; i < listComments.size(); i++) {
            Comment currentComment = listComments.get(i);
            statement.clearBindings();
            //for a given column index, simply bind the data to be put inside that index
            statement.bindLong(1, currentComment.getId());
            statement.bindString(2, currentComment.getLocationName());
            statement.bindString(3, currentComment.getComment());

            statement.execute();
        }
        //set the transaction as successful and end the transaction
        L.m("inserting entries " + listComments.size() + new Date(System.currentTimeMillis()));
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public void insertImages(int table, ArrayList<Image> listImages, boolean clearPrevious) {
        if (clearPrevious) {
            deleteImages();
        }

        //create a sql prepared statement
        String sql = "INSERT INTO location_images(id, location_name, image) VALUES (?,?,?);";
        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (int i = 0; i < listImages.size(); i++) {
            Image currentImage = listImages.get(i);
            statement.clearBindings();
            //for a given column index, simply bind the data to be put inside that index
            statement.bindLong(1, currentImage.getId());
            statement.bindString(2, currentImage.getLocationName());
            statement.bindString(3, currentImage.getImage());

            statement.execute();
        }
        //set the transaction as successful and end the transaction
        L.m("inserting entries " + listImages.size() + new Date(System.currentTimeMillis()));
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public void updateLocation (long id, long rating) {
        String sql = "UPDATE locations SET rating = ? WHERE id = ?";
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();

        statement.clearBindings();
        //for a given column index, simply bind the data to be put inside that index
        statement.bindLong(1, rating);
        statement.bindLong(2, id);
        statement.execute();

        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public ArrayList<Location> readLocations(int table) {
        ArrayList<Location> listLocations = new ArrayList<>();

        //get a list of columns to be retrieved, we need all of them
        String[] columns = {LocationsHelper.COLUMN_UID,
                LocationsHelper.COLUMN_ID,
                LocationsHelper.COLUMN_LOCATION_NAME,
                LocationsHelper.COLUMN_DESCRIPTION,
                LocationsHelper.COLUMN_LATITUDE,
                LocationsHelper.COLUMN_LONGITUDE,
                LocationsHelper.COLUMN_HINTS,
                LocationsHelper.COLUMN_MAP_ICON_ID,
                LocationsHelper.COLUMN_RATING,
                LocationsHelper.COLUMN_IMAGE,
                LocationsHelper.COLUMN_CREATED_AT
        };
        Cursor cursor = mDatabase.query((table == LOCATIONS ? LocationsHelper.TABLE_LOCATIONS : LocationsHelper.TABLE_UPCOMING), columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            L.m("loading entries " + cursor.getCount() + new Date(System.currentTimeMillis()));
            do {

                //create a new location object and retrieve the data from the cursor to be stored in this location object
                Location location = new Location();
                //each step is a 2 part process, find the index of the column first, find the data of that column using
                //that index and finally set our blank location object to contain our data
                location.setId(cursor.getLong(cursor.getColumnIndex(LocationsHelper.COLUMN_ID)));
                location.setLocationName(cursor.getString(cursor.getColumnIndex(LocationsHelper.COLUMN_LOCATION_NAME)));
                location.setDescription(cursor.getString(cursor.getColumnIndex(LocationsHelper.COLUMN_DESCRIPTION)));
                location.setLatitude(cursor.getDouble(cursor.getColumnIndex(LocationsHelper.COLUMN_LATITUDE)));
                location.setLongitude(cursor.getDouble(cursor.getColumnIndex(LocationsHelper.COLUMN_LONGITUDE)));
                location.setHints(cursor.getString(cursor.getColumnIndex(LocationsHelper.COLUMN_HINTS)));
                location.setMapIconId(cursor.getString(cursor.getColumnIndex(LocationsHelper.COLUMN_MAP_ICON_ID)));
                location.setRating(cursor.getLong(cursor.getColumnIndex(LocationsHelper.COLUMN_RATING)));
                location.setImage(cursor.getString(cursor.getColumnIndex(LocationsHelper.COLUMN_IMAGE)));
                long releaseDateMilliseconds = cursor.getLong(cursor.getColumnIndex(LocationsHelper.COLUMN_CREATED_AT));
                location.setCreatedAt(releaseDateMilliseconds != -1 ? new Date(releaseDateMilliseconds) : null);

                //add the location to the list of location objects which we plan to return
                listLocations.add(location);
            }
            while (cursor.moveToNext());
        }
        return listLocations;
    }

    public ArrayList<Rating> getRatings(int table, long id) {
        ArrayList<Rating> listRatings = new ArrayList<>();

        //get a list of columns to be retrieved, we need all of them
        String[] columns = {LocationsHelper.COLUMN_UID,
                LocationsHelper.COLUMN_ID,
                LocationsHelper.COLUMN_LOCATION_NAME,
                LocationsHelper.COLUMN_RATING
        };
        Cursor cursor = mDatabase.query(LocationsHelper.TABLE_LOCATION_RATINGS, new String[] { LocationsHelper.COLUMN_ID,
                        LocationsHelper.COLUMN_LOCATION_NAME, LocationsHelper.COLUMN_RATING }, LocationsHelper.COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            L.m("loading entries " + cursor.getCount() + new Date(System.currentTimeMillis()));
            do {

                //create a new location object and retrieve the data from the cursor to be stored in this location object
                Rating rating = new Rating();
                //each step is a 2 part process, find the index of the column first, find the data of that column using
                //that index and finally set our blank location object to contain our data
                rating.setId(cursor.getLong(cursor.getColumnIndex(LocationsHelper.COLUMN_ID)));
                rating.setLocationName(cursor.getString(cursor.getColumnIndex(LocationsHelper.COLUMN_LOCATION_NAME)));
                rating.setRating(cursor.getLong(cursor.getColumnIndex(LocationsHelper.COLUMN_RATING)));

                //add the location to the list of location objects which we plan to return
                listRatings.add(rating);
            }
            while (cursor.moveToNext());
        }
        return listRatings;
    }

    public ArrayList<Comment> getComments(int table, long id) {
        ArrayList<Comment> listComments = new ArrayList<>();

        //get a list of columns to be retrieved, we need all of them
        String[] columns = {LocationsHelper.COLUMN_UID,
                LocationsHelper.COLUMN_ID,
                LocationsHelper.COLUMN_LOCATION_NAME,
                LocationsHelper.COLUMN_RATING
        };
        Cursor cursor = mDatabase.query(LocationsHelper.TABLE_LOCATION_COMMENTS, new String[] { LocationsHelper.COLUMN_ID,
                        LocationsHelper.COLUMN_LOCATION_NAME, LocationsHelper.COLUMN_COMMENT }, LocationsHelper.COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            L.m("loading entries " + cursor.getCount() + new Date(System.currentTimeMillis()));
            do {

                //create a new location object and retrieve the data from the cursor to be stored in this location object
                Comment comment = new Comment();
                //each step is a 2 part process, find the index of the column first, find the data of that column using
                //that index and finally set our blank location object to contain our data
                comment.setId(cursor.getLong(cursor.getColumnIndex(LocationsHelper.COLUMN_ID)));
                comment.setLocationName(cursor.getString(cursor.getColumnIndex(LocationsHelper.COLUMN_LOCATION_NAME)));
                comment.setComment(cursor.getString(cursor.getColumnIndex(LocationsHelper.COLUMN_COMMENT)));

                //add the location to the list of location objects which we plan to return
                listComments.add(comment);
            }
            while (cursor.moveToNext());
        }
        return listComments;
    }

    public ArrayList<Image> getImages(int table, long id) {
        ArrayList<Image> listImages = new ArrayList<>();

        //get a list of columns to be retrieved, we need all of them
        String[] columns = {LocationsHelper.COLUMN_UID,
                LocationsHelper.COLUMN_ID,
                LocationsHelper.COLUMN_LOCATION_NAME,
                LocationsHelper.COLUMN_IMAGE
        };
        Cursor cursor = mDatabase.query(LocationsHelper.TABLE_LOCATION_IMAGES, new String[] { LocationsHelper.COLUMN_ID,
                        LocationsHelper.COLUMN_LOCATION_NAME, LocationsHelper.COLUMN_IMAGE }, LocationsHelper.COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            L.m("loading entries " + cursor.getCount() + new Date(System.currentTimeMillis()));
            do {

                //create a new location object and retrieve the data from the cursor to be stored in this location object
                Image image = new Image();
                //each step is a 2 part process, find the index of the column first, find the data of that column using
                //that index and finally set our blank location object to contain our data
                image.setId(cursor.getLong(cursor.getColumnIndex(LocationsHelper.COLUMN_ID)));
                image.setLocationName(cursor.getString(cursor.getColumnIndex(LocationsHelper.COLUMN_LOCATION_NAME)));
                image.setImage(cursor.getString(cursor.getColumnIndex(LocationsHelper.COLUMN_IMAGE)));

                //add the location to the list of location objects which we plan to return
                listImages.add(image);
            }
            while (cursor.moveToNext());
        }
        return listImages;
    }

    public ArrayList<Location> getFilteredLocations(int table, String mapIconId) {
        ArrayList<Location> listLocations = new ArrayList<>();

        //get a list of columns to be retrieved, we need all of them
        String[] columns = {LocationsHelper.COLUMN_UID,
                LocationsHelper.COLUMN_ID,
                LocationsHelper.COLUMN_LOCATION_NAME,
                LocationsHelper.COLUMN_DESCRIPTION,
                LocationsHelper.COLUMN_LATITUDE,
                LocationsHelper.COLUMN_LONGITUDE,
                LocationsHelper.COLUMN_HINTS,
                LocationsHelper.COLUMN_MAP_ICON_ID,
                LocationsHelper.COLUMN_RATING,
                LocationsHelper.COLUMN_IMAGE,
                LocationsHelper.COLUMN_CREATED_AT
        };
        Cursor cursor = mDatabase.query(LocationsHelper.TABLE_LOCATIONS, new String[] { LocationsHelper.COLUMN_ID,
                        LocationsHelper.COLUMN_LOCATION_NAME, LocationsHelper.COLUMN_DESCRIPTION, LocationsHelper.COLUMN_LATITUDE, LocationsHelper.COLUMN_LONGITUDE,
                        LocationsHelper.COLUMN_HINTS, LocationsHelper.COLUMN_MAP_ICON_ID, LocationsHelper.COLUMN_RATING, LocationsHelper.COLUMN_IMAGE, LocationsHelper.COLUMN_CREATED_AT }, LocationsHelper.COLUMN_MAP_ICON_ID + "=?",
                new String[] { String.valueOf(mapIconId) }, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            L.m("loading entries " + cursor.getCount() + new Date(System.currentTimeMillis()));
            do {

                Location location = new Location();
                //each step is a 2 part process, find the index of the column first, find the data of that column using
                //that index and finally set our blank location object to contain our data
                location.setId(cursor.getLong(cursor.getColumnIndex(LocationsHelper.COLUMN_ID)));
                location.setLocationName(cursor.getString(cursor.getColumnIndex(LocationsHelper.COLUMN_LOCATION_NAME)));
                location.setDescription(cursor.getString(cursor.getColumnIndex(LocationsHelper.COLUMN_DESCRIPTION)));
                location.setLatitude(cursor.getDouble(cursor.getColumnIndex(LocationsHelper.COLUMN_LATITUDE)));
                location.setLongitude(cursor.getDouble(cursor.getColumnIndex(LocationsHelper.COLUMN_LONGITUDE)));
                location.setHints(cursor.getString(cursor.getColumnIndex(LocationsHelper.COLUMN_HINTS)));
                location.setMapIconId(cursor.getString(cursor.getColumnIndex(LocationsHelper.COLUMN_MAP_ICON_ID)));
                location.setRating(cursor.getLong(cursor.getColumnIndex(LocationsHelper.COLUMN_RATING)));
                location.setImage(cursor.getString(cursor.getColumnIndex(LocationsHelper.COLUMN_IMAGE)));
                long releaseDateMilliseconds = cursor.getLong(cursor.getColumnIndex(LocationsHelper.COLUMN_CREATED_AT));
                location.setCreatedAt(releaseDateMilliseconds != -1 ? new Date(releaseDateMilliseconds) : null);

                //add the location to the list of location objects which we plan to return
                listLocations.add(location);
            }
            while (cursor.moveToNext());
        }
        return listLocations;
    }

    public void deleteLocations(int table) {
        mDatabase.delete((table == LOCATIONS ? LocationsHelper.TABLE_LOCATIONS : LocationsHelper.TABLE_UPCOMING), null, null);
    }

    public void deleteRatings() {
        mDatabase.execSQL("delete from location_ratings");
    }

    public void deleteComments() {
        mDatabase.execSQL("delete from location_comments");
    }

    public void deleteImages() {
        mDatabase.execSQL("delete from location_images");
    }

    private static class LocationsHelper extends SQLiteOpenHelper {
        public static final String TABLE_UPCOMING = " movies_upcoming";
        public static final String TABLE_LOCATIONS = "locations";
        public static final String TABLE_LOCATION_RATINGS = "location_ratings";
        public static final String TABLE_LOCATION_COMMENTS = "location_comments";
        public static final String TABLE_LOCATION_IMAGES = "location_images";
        public static final String COLUMN_UID = "_id";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_LOCATION_NAME = "location_name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_HINTS = "hints";
        public static final String COLUMN_MAP_ICON_ID = "map_icon_id";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_COMMENT = "comment";
        public static final String COLUMN_CREATED_AT = "created_at";

        private static final String CREATE_TABLE_LOCATIONS = "CREATE TABLE " + TABLE_LOCATIONS + " (" +
                COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_ID + " LONG," +
                COLUMN_LOCATION_NAME + " TEXT," +
                COLUMN_DESCRIPTION + " TEXT," +
                COLUMN_LATITUDE + " DOUBLE," +
                COLUMN_LONGITUDE + " DOUBLE," +
                COLUMN_HINTS + " TEXT," +
                COLUMN_MAP_ICON_ID + " TEXT," +
                COLUMN_RATING + " DOUBLE," +
                COLUMN_IMAGE + " String," +
                COLUMN_CREATED_AT + " LONG" +
                ");";
        private static final String CREATE_TABLE_LOCATION_RATINGS = "CREATE TABLE " + TABLE_LOCATION_RATINGS + " (" +
                COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_ID + " LONG," +
                COLUMN_LOCATION_NAME + " TEXT," +
                COLUMN_RATING + " DOUBLE" +
                ");";
        private static final String CREATE_TABLE_LOCATION_COMMENTS = "CREATE TABLE " + TABLE_LOCATION_COMMENTS + " (" +
                COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_ID + " LONG," +
                COLUMN_LOCATION_NAME + " TEXT," +
                COLUMN_COMMENT + " TEXT" +
                ");";
        private static final String CREATE_TABLE_LOCATION_IMAGES = "CREATE TABLE " + TABLE_LOCATION_IMAGES + " (" +
                COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_ID + " LONG," +
                COLUMN_LOCATION_NAME + " TEXT," +
                COLUMN_IMAGE + " String" +
                ");";
        private static final String CREATE_TABLE_UPCOMING = "CREATE TABLE " + TABLE_UPCOMING + " (" +
                COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_ID + " LONG," +
                COLUMN_LOCATION_NAME + " TEXT," +
                COLUMN_DESCRIPTION + " TEXT," +
                COLUMN_LATITUDE + " DOUBLE," +
                COLUMN_LONGITUDE + " DOUBLE," +
                COLUMN_HINTS + " TEXT," +
                COLUMN_MAP_ICON_ID + " TEXT," +
                COLUMN_RATING + " DOUBLE," +
                COLUMN_IMAGE + " String," +
                COLUMN_CREATED_AT + " LONG" +
                ");";
        private static final String DB_NAME = "locations_db";
        private static final int DB_VERSION = 1;
        private Context mContext;

        public LocationsHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE_LOCATIONS);
                db.execSQL(CREATE_TABLE_UPCOMING);
                db.execSQL(CREATE_TABLE_LOCATION_RATINGS);
                db.execSQL(CREATE_TABLE_LOCATION_COMMENTS);
                db.execSQL(CREATE_TABLE_LOCATION_IMAGES);
                L.m("create table locations executed");
            } catch (SQLiteException exception) {
                L.t(mContext, exception + "");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                L.m("upgrade table box office executed");
                db.execSQL(" DROP TABLE " + TABLE_LOCATIONS + " IF EXISTS;");
                db.execSQL(" DROP TABLE " + TABLE_UPCOMING + " IF EXISTS;");
                onCreate(db);
            } catch (SQLiteException exception) {
                L.t(mContext, exception + "");
            }
        }
    }
}
