package com.example.sonic.fspotter.extras;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.example.sonic.fspotter.database.DBLocations;
import com.example.sonic.fspotter.json.Endpoints;
import com.example.sonic.fspotter.json.Parser;
import com.example.sonic.fspotter.json.Requestor;
import com.example.sonic.fspotter.fspotter.MyApplication;
import com.example.sonic.fspotter.pojo.Location;
import com.example.sonic.fspotter.pojo.Rating;

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
        ArrayList<Location> listLocations = Parser.parseLocationsJSON(response);

        JSONObject responseRatings = Requestor.requestLocationsJSON(requestQueue, Endpoints.getRequestUrlRatings(30));
        if (responseRatings != null) {
            Log.v("JSON RESPONSE RATINGS", responseRatings.toString());
        }
        ArrayList<Rating> listRatings = Parser.parseRatingsJSON(responseRatings);

        // Average the ratings
        ArrayList<Rating> averagedRatings = RatingAverager.averageRatings(listRatings);

        // Add ratings to locations
        ArrayList<Location> updatedLocations = Comparator.updateLocationsWithRatings(listLocations, averagedRatings);

        Log.v("UPDATED LOCATIONS", updatedLocations.toString());
        MyApplication.getWritableDatabase().insertLocations(DBLocations.LOCATIONS, updatedLocations, true);
        return updatedLocations;
    }

    public static ArrayList<Location> loadUpcomingMovies(RequestQueue requestQueue) {
        JSONObject response = Requestor.requestLocationsJSON(requestQueue, Endpoints.getRequestUrlUpcomingMovies(30));
        ArrayList<Location> listLocations = Parser.parseLocationsJSON(response);
        MyApplication.getWritableDatabase().insertLocations(DBLocations.UPCOMING, listLocations, true);
        return listLocations;
    }
}
