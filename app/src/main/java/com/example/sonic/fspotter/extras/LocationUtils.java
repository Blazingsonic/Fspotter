package com.example.sonic.fspotter.extras;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.example.sonic.fspotter.database.DBLocations;
import com.example.sonic.fspotter.json.Endpoints;
import com.example.sonic.fspotter.json.Parser;
import com.example.sonic.fspotter.json.Requestor;
import com.example.sonic.fspotter.fspotter.MyApplication;
import com.example.sonic.fspotter.pojo.Location;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Windows on 02-03-2015.
 */
public class LocationUtils {
    public static ArrayList<Location> loadLocations(RequestQueue requestQueue) {
        JSONObject response = Requestor.requestLocationsJSON(requestQueue, Endpoints.getRequestUrlLocations(30));
        if (response != null) {
            Log.v("JSON RESPONSE", response.toString());
        }
        ArrayList<Location> listMovies = Parser.parseLocationsJSON(response);
        MyApplication.getWritableDatabase().insertLocations(DBLocations.BOX_OFFICE, listMovies, true);
        return listMovies;
    }

    public static ArrayList<Location> loadUpcomingMovies(RequestQueue requestQueue) {
        JSONObject response = Requestor.requestLocationsJSON(requestQueue, Endpoints.getRequestUrlUpcomingMovies(30));
        ArrayList<Location> listMovies = Parser.parseLocationsJSON(response);
        MyApplication.getWritableDatabase().insertLocations(DBLocations.UPCOMING, listMovies, true);
        return listMovies;
    }
}
