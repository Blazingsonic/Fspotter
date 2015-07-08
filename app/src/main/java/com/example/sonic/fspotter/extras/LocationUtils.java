package com.example.sonic.fspotter.extras;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.example.sonic.fspotter.database.DBLocations;
import com.example.sonic.fspotter.json.Endpoints;
import com.example.sonic.fspotter.json.Parser;
import com.example.sonic.fspotter.json.Requestor;
import com.example.sonic.fspotter.fspotter.MyApplication;
import com.example.sonic.fspotter.pojo.Comment;
import com.example.sonic.fspotter.pojo.Image;
import com.example.sonic.fspotter.pojo.Location;
import com.example.sonic.fspotter.pojo.Rating;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Windows on 02-03-2015.
 */
public class LocationUtils {
    public static ArrayList<Location> loadLocations(RequestQueue requestQueue) {

        /*
         * GET locations, ratings, comments
         */

        // Get locations
        JSONObject response = Requestor.requestLocationsJSON(requestQueue, Endpoints.getRequestUrlLocations(30));
        if (response != null) {
            Log.v("JSON RESPONSE", response.toString());
        }
        ArrayList<Location> listLocations = Parser.parseLocationsJSON(response);

        // Get ratings
        JSONObject responseRatings = Requestor.requestLocationsJSON(requestQueue, Endpoints.getRequestUrlRatings(30));
        if (responseRatings != null) {
            Log.v("JSON RESPONSE RATINGS", responseRatings.toString());
        }
        ArrayList<Rating> listRatings = Parser.parseRatingsJSON(responseRatings);

        // Get comments
        JSONObject responseComments = Requestor.requestLocationsJSON(requestQueue, Endpoints.getRequestUrlComments(30));
        if (responseRatings != null) {
            Log.v("JSON RESPONSE COMMENTS", responseComments.toString());
        }
        ArrayList<Comment> listComments = Parser.parseCommentsJSON(responseComments);

        // Get images
        JSONObject responseImages = Requestor.requestLocationsJSON(requestQueue, Endpoints.getRequestUrlImages(30));
        if (responseRatings != null) {
            Log.v("JSON RESPONSE COMMENTS", responseImages.toString());
        }
        ArrayList<Image> listImages = Parser.parseImagesJSON(responseImages);

        /*
         * INSERT ratings, comments, locations
         */

        // Insert ratings
        MyApplication.getWritableDatabase().insertRatings(DBLocations.LOCATIONS, listRatings, true);

        // Select ratings and add them to locations
        for (int i = 0; i < listLocations.size(); i++) {
            Location currentLocation = listLocations.get(i);
            ArrayList<Rating> ratingOfId;
            ratingOfId = MyApplication.getWritableDatabase().getRatings(DBLocations.LOCATIONS, currentLocation.getId());
            int sum = 0;
            int average;
            for (int j = 0; j < ratingOfId.size(); j++) {
                sum += ratingOfId.get(j).getRating();
                Log.v("RATING ID", String.valueOf(ratingOfId.get(j).getRating()));
            }
            if (sum != 0) {
                average = sum / ratingOfId.size();
                currentLocation.setRating(average);
                Log.v("RATING AVERAGE", String.valueOf(average));
            }
        }

        // Insert Comments
        MyApplication.getWritableDatabase().insertComments(DBLocations.LOCATIONS, listComments, true);

        // Insert Images
        MyApplication.getWritableDatabase().insertImages(DBLocations.LOCATIONS, listImages, true);

        // Insert Locations
        MyApplication.getWritableDatabase().insertLocations(DBLocations.LOCATIONS, listLocations, true);

        return listLocations;
    }

    public static ArrayList<Location> loadUpcomingMovies(RequestQueue requestQueue) {
        JSONObject response = Requestor.requestLocationsJSON(requestQueue, Endpoints.getRequestUrlUpcomingMovies(30));
        ArrayList<Location> listLocations = Parser.parseLocationsJSON(response);
        MyApplication.getWritableDatabase().insertLocations(DBLocations.UPCOMING, listLocations, true);
        return listLocations;
    }
}
