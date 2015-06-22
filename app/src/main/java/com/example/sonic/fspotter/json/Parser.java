package com.example.sonic.fspotter.json;

import android.util.Log;

import com.example.sonic.fspotter.extras.Constants;
import com.example.sonic.fspotter.pojo.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.sonic.fspotter.extras.Keys.EndpointLocations.KEY_AUDIENCE_SCORE;
import static com.example.sonic.fspotter.extras.Keys.EndpointLocations.KEY_CAST;
import static com.example.sonic.fspotter.extras.Keys.EndpointLocations.KEY_ID;
import static com.example.sonic.fspotter.extras.Keys.EndpointLocations.KEY_LINKS;
import static com.example.sonic.fspotter.extras.Keys.EndpointLocations.KEY_POSTERS;
import static com.example.sonic.fspotter.extras.Keys.EndpointLocations.KEY_RATINGS;
import static com.example.sonic.fspotter.extras.Keys.EndpointLocations.KEY_RELEASE_DATES;
import static com.example.sonic.fspotter.extras.Keys.EndpointLocations.KEY_REVIEWS;
import static com.example.sonic.fspotter.extras.Keys.EndpointLocations.KEY_SELF;
import static com.example.sonic.fspotter.extras.Keys.EndpointLocations.KEY_SIMILAR;
import static com.example.sonic.fspotter.extras.Keys.EndpointLocations.KEY_SYNOPSIS;
import static com.example.sonic.fspotter.extras.Keys.EndpointLocations.KEY_THEATER;
import static com.example.sonic.fspotter.extras.Keys.EndpointLocations.KEY_THUMBNAIL;

/**
 * Created by Windows on 02-03-2015.
 */
public class Parser {
    public static ArrayList<Location> parseLocationsJSON(JSONObject response) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<Location> listLocations = new ArrayList<>();
        if (response != null && response.length() > 0) {
            try {
                JSONArray arrayLocations = response.getJSONArray("tasks");
                Log.v("JSON TASKS", arrayLocations.toString());
                for (int i = 0; i < arrayLocations.length(); i++) {
                    long id = -1;
                    String title = Constants.NA;
                    String releaseDate = Constants.NA;
                    int audienceScore = -1;
                    String synopsis = Constants.NA;
                    String urlThumbnail = Constants.NA;
                    String urlSelf = Constants.NA;
                    String urlCast = Constants.NA;
                    String urlReviews = Constants.NA;
                    String urlSimilar = Constants.NA;

                    JSONObject currentMovie = arrayLocations.getJSONObject(i);
                    Log.v("JSON CURRENT", currentMovie.toString());
                    Log.v("JSON CURRENT TASK", currentMovie.getString("task"));
                    //get the id of the current location
                    if (Utils.contains(currentMovie, KEY_ID)) {
                        id = currentMovie.getLong(KEY_ID);
                    }
                    //get the title of the current location
                    if (Utils.contains(currentMovie, "task")) {
                        title = currentMovie.getString("task");
                    }

                    //get the date in theaters for the current location
                    if (Utils.contains(currentMovie, KEY_RELEASE_DATES)) {
                        JSONObject objectReleaseDates = currentMovie.getJSONObject(KEY_RELEASE_DATES);

                        if (Utils.contains(objectReleaseDates, KEY_THEATER)) {
                            releaseDate = objectReleaseDates.getString(KEY_THEATER);
                        }
                    }

                    //get the audience score for the current location

                    if (Utils.contains(currentMovie, KEY_RATINGS)) {
                        JSONObject objectRatings = currentMovie.getJSONObject(KEY_RATINGS);
                        if (Utils.contains(objectRatings, KEY_AUDIENCE_SCORE)) {
                            audienceScore = objectRatings.getInt(KEY_AUDIENCE_SCORE);
                        }
                    }

                    // get the synopsis of the current location
                    if (Utils.contains(currentMovie, KEY_SYNOPSIS)) {
                        synopsis = currentMovie.getString(KEY_SYNOPSIS);
                    }

                    //get the url for the thumbnail to be displayed inside the current location result
                    if (Utils.contains(currentMovie, KEY_POSTERS)) {
                        JSONObject objectPosters = currentMovie.getJSONObject(KEY_POSTERS);

                        if (Utils.contains(objectPosters, KEY_THUMBNAIL)) {
                            urlThumbnail = objectPosters.getString(KEY_THUMBNAIL);
                        }
                    }

                    //get the url of the related links
                    if (Utils.contains(currentMovie, KEY_LINKS)) {
                        JSONObject objectLinks = currentMovie.getJSONObject(KEY_LINKS);
                        if (Utils.contains(objectLinks, KEY_SELF)) {
                            urlSelf = objectLinks.getString(KEY_SELF);
                        }
                        if (Utils.contains(objectLinks, KEY_CAST)) {
                            urlCast = objectLinks.getString(KEY_CAST);
                        }
                        if (Utils.contains(objectLinks, KEY_REVIEWS)) {
                            urlReviews = objectLinks.getString(KEY_REVIEWS);
                        }
                        if (Utils.contains(objectLinks, KEY_SIMILAR)) {
                            urlSimilar = objectLinks.getString(KEY_SIMILAR);
                        }
                    }
                    Location location = new Location();
                    location.setId(id);
                    location.setTitle(title);
                    Date date = null;
                    try {
                        date = dateFormat.parse(releaseDate);
                    } catch (ParseException e) {
                        //a parse exception generated here will store null in the release date, be sure to handle it
                    }
                    location.setReleaseDateTheater(date);
                    location.setAudienceScore(audienceScore);
                    location.setSynopsis(synopsis);
                    location.setUrlThumbnail(urlThumbnail);
                    location.setUrlSelf(urlSelf);
                    location.setUrlCast(urlCast);
                    location.setUrlThumbnail(urlThumbnail);
                    location.setUrlReviews(urlReviews);
                    location.setUrlSimilar(urlSimilar);
//                    L.t(getActivity(), location + "");
                    //if (id != -1 && !title.equals(Constants.NA)) {
                    listLocations.add(location);
                    //}
                }

            } catch (JSONException e) {

            }
//                L.t(getActivity(), listLocations.size() + " rows fetched");
        }
        return listLocations;
    }


}
