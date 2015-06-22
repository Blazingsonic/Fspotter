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
        String sql = "INSERT INTO " + (table == LOCATIONS ? LocationsHelper.TABLE_LOCATIONS : LocationsHelper.TABLE_UPCOMING) + " VALUES (?,?,?,?,?,?,?);";
        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (int i = 0; i < listLocations.size(); i++) {
            Location currentLocation = listLocations.get(i);
            statement.clearBindings();
            //for a given column index, simply bind the data to be put inside that index
            statement.bindString(2, currentLocation.getLocationName());
            statement.bindString(3, currentLocation.getDescription());
            statement.bindDouble(4, currentLocation.getLatitude());
            statement.bindDouble(5, currentLocation.getLongitude());
            statement.bindString(6, currentLocation.getHints());
            statement.bindString(7, currentLocation.getMapIconId());

            statement.execute();
        }
        //set the transaction as successful and end the transaction
        L.m("inserting entries " + listLocations.size() + new Date(System.currentTimeMillis()));
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public ArrayList<Location> readLocations(int table) {
        ArrayList<Location> listLocations = new ArrayList<>();

        //get a list of columns to be retrieved, we need all of them
        String[] columns = {LocationsHelper.COLUMN_UID,
                LocationsHelper.COLUMN_LOCATION_NAME,
                LocationsHelper.COLUMN_DESCRIPTION,
                LocationsHelper.COLUMN_LATITUDE,
                LocationsHelper.COLUMN_LONGITUDE,
                LocationsHelper.COLUMN_HINTS,
                LocationsHelper.COLUMN_MAP_ICON_ID
        };
        Cursor cursor = mDatabase.query((table == LOCATIONS ? LocationsHelper.TABLE_LOCATIONS : LocationsHelper.TABLE_UPCOMING), columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            L.m("loading entries " + cursor.getCount() + new Date(System.currentTimeMillis()));
            do {

                //create a new location object and retrieve the data from the cursor to be stored in this location object
                Location location = new Location();
                //each step is a 2 part process, find the index of the column first, find the data of that column using
                //that index and finally set our blank location object to contain our data
                location.setLocationName(cursor.getString(cursor.getColumnIndex(LocationsHelper.COLUMN_LOCATION_NAME)));
                location.setDescription(cursor.getString(cursor.getColumnIndex(LocationsHelper.COLUMN_DESCRIPTION)));
                location.setLatitude(cursor.getDouble(cursor.getColumnIndex(LocationsHelper.COLUMN_LATITUDE)));
                location.setLongitude(cursor.getDouble(cursor.getColumnIndex(LocationsHelper.COLUMN_LONGITUDE)));
                location.setHints(cursor.getString(cursor.getColumnIndex(LocationsHelper.COLUMN_HINTS)));
                location.setMapIconId(cursor.getString(cursor.getColumnIndex(LocationsHelper.COLUMN_MAP_ICON_ID)));

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

    private static class LocationsHelper extends SQLiteOpenHelper {
        public static final String TABLE_UPCOMING = " movies_upcoming";
        public static final String TABLE_LOCATIONS = "locations";
        public static final String COLUMN_UID = "_id";
        public static final String COLUMN_LOCATION_NAME = "location_name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_HINTS = "hints";
        public static final String COLUMN_MAP_ICON_ID = "map_icon_id";

        private static final String CREATE_TABLE_LOCATIONS = "CREATE TABLE " + TABLE_LOCATIONS + " (" +
                COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_LOCATION_NAME + " TEXT," +
                COLUMN_DESCRIPTION + " TEXT," +
                COLUMN_LATITUDE + " DOUBLE," +
                COLUMN_LONGITUDE + " DOUBLE," +
                COLUMN_HINTS + " TEXT," +
                COLUMN_MAP_ICON_ID + " TEXT" +
                ");";
        private static final String CREATE_TABLE_UPCOMING = "CREATE TABLE " + TABLE_UPCOMING + " (" +
                COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_LOCATION_NAME + " TEXT," +
                COLUMN_DESCRIPTION + " TEXT," +
                COLUMN_LATITUDE + " DOUBLE," +
                COLUMN_LONGITUDE + " DOUBLE," +
                COLUMN_HINTS + " TEXT," +
                COLUMN_MAP_ICON_ID + " TEXT" +
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
