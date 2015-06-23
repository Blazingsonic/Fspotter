package com.example.sonic.fspotter.json;

import android.util.Log;

import com.example.sonic.fspotter.extras.Constants;
import com.example.sonic.fspotter.pojo.Location;
import com.example.sonic.fspotter.pojo.Rating;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.sonic.fspotter.extras.Keys.EndpointLocations.KEY_HINTS;
import static com.example.sonic.fspotter.extras.Keys.EndpointLocations.KEY_ID;
import static com.example.sonic.fspotter.extras.Keys.EndpointLocations.KEY_LOCATIONS;
import static com.example.sonic.fspotter.extras.Keys.EndpointLocations.KEY_LOCATION_NAME;
import static com.example.sonic.fspotter.extras.Keys.EndpointLocations.KEY_MAP_ICON_ID;
import static com.example.sonic.fspotter.extras.Keys.EndpointLocations.KEY_LONGITUDE;
import static com.example.sonic.fspotter.extras.Keys.EndpointLocations.KEY_DESCRIPTION;
import static com.example.sonic.fspotter.extras.Keys.EndpointLocations.KEY_LATITUDE;

/**
 * Created by Windows on 02-03-2015.
 */
public class Parser {
    public static ArrayList<Location> parseLocationsJSON(JSONObject response) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<Location> listLocations = new ArrayList<>();
        if (response != null && response.length() > 0) {
            try {
                JSONArray arrayLocations = response.getJSONArray(KEY_LOCATIONS);
                Log.v("JSON TASKS", arrayLocations.toString());
                for (int i = 0; i < arrayLocations.length(); i++) {
                    long id = -1;
                    String locationName = Constants.NA;
                    String description = Constants.NA;
                    double latitude = 40;
                    double longitude = 80;
                    String hints = Constants.NA;
                    String mapIconId = Constants.NA;
                    long rating = -1;

                    JSONObject currentMovie = arrayLocations.getJSONObject(i);
                    Log.v("JSON CURRENT", currentMovie.toString());
                    //Log.v("JSON CURRENT TASK", currentMovie.getString("task"));
                    //get the id of the current location
                    if (Utils.contains(currentMovie, KEY_ID)) {
                        id = currentMovie.getLong(KEY_ID);
                    }
                    //get the title of the current location
                    if (Utils.contains(currentMovie, KEY_LOCATION_NAME)) {
                        locationName = currentMovie.getString(KEY_LOCATION_NAME);
                    }

                    //get the date in theaters for the current location
                    if (Utils.contains(currentMovie, KEY_DESCRIPTION)) {
                        description = currentMovie.getString(KEY_DESCRIPTION);

                    }

                    //get the audience score for the current location
                    if (Utils.contains(currentMovie, KEY_LATITUDE)) {
                        latitude = currentMovie.getDouble(KEY_LATITUDE);
                    }

                    //get the audience score for the current location
                    if (Utils.contains(currentMovie, KEY_LONGITUDE)) {
                        longitude = currentMovie.getDouble(KEY_LONGITUDE);
                    }

                    // get the synopsis of the current location
                    if (Utils.contains(currentMovie, KEY_HINTS)) {
                        hints = currentMovie.getString(KEY_HINTS);
                    }

                    //get the url for the thumbnail to be displayed inside the current location result
                    if (Utils.contains(currentMovie, KEY_MAP_ICON_ID)) {
                        mapIconId = currentMovie.getString(KEY_MAP_ICON_ID);
                    }

                    Location location = new Location();
                    location.setId(id);
                    location.setLocationName(locationName);
                    Date date = null;
                    try {
                        date = dateFormat.parse(description);
                    } catch (ParseException e) {
                        //a parse exception generated here will store null in the release date, be sure to handle it
                    }
                    location.setDescription(description);
                    location.setLatitude(latitude);
                    location.setLongitude(longitude);
                    location.setHints(hints);
                    location.setMapIconId(mapIconId);
                    location.setRating(rating);
//                    L.t(getActivity(), location + "");
                    //if (id != -1 && !title.equals(Constants.NA)) {
                    listLocations.add(location);
                    //}
                }

            } catch (JSONException e) {

            }
//                L.t(getActivity(), listLocations.size() + " rows fetched");
        }
        Log.v("LIST LOCATIONS", listLocations.toString());
        return listLocations;
    }

    public static ArrayList parseRatingsJSON(JSONObject response) {
        ArrayList<Rating> listRatings = new ArrayList<>();
        if (response != null && response.length() > 0) {
            try {
                JSONArray arrayRatings = response.getJSONArray("ratings");
                Log.v("JSON TASKS RATINGS", arrayRatings.toString());
                for (int i = 0; i < arrayRatings.length(); i++) {
                    long id = -1;
                    String locationName = Constants.NA;
                    long rating = -1;

                    JSONObject currentRating = arrayRatings.getJSONObject(i);
                    Log.v("JSON CURRENT RATINGS", currentRating.toString());

                    //Log.v("JSON CURRENT TASK", currentMovie.getString("task"));
                    //get the id of the current location
                    if (Utils.contains(currentRating, KEY_ID)) {
                        id = currentRating.getLong(KEY_ID);
                    }
                    //get the title of the current location
                    if (Utils.contains(currentRating, KEY_LOCATION_NAME)) {
                        Log.v("JSON LOCATIONNAME", currentRating.getString("locationName"));
                        locationName = currentRating.getString(KEY_LOCATION_NAME);
                    }

                    //get the date in theaters for the current location
                    if (Utils.contains(currentRating, "rating")) {
                        rating = currentRating.getInt("rating");

                    }

                    Rating ratingObject = new Rating();
                    ratingObject.setId(id);
                    ratingObject.setLocationName(locationName);
                    ratingObject.setRating(rating);

                    Log.v("JSON LOCATIONNAME", ratingObject.toString());

                    listRatings.add(ratingObject);
                }

            } catch (JSONException e) {

            }
//                L.t(getActivity(), listLocations.size() + " rows fetched");
        }
        Log.v("LIST LOCATIONS", listRatings.toString());
        return listRatings;
    }

}
