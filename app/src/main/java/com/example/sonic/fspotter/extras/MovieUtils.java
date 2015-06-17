package com.example.sonic.fspotter.extras;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.example.sonic.fspotter.database.DBMovies;
import com.example.sonic.fspotter.json.Endpoints;
import com.example.sonic.fspotter.json.Parser;
import com.example.sonic.fspotter.json.Requestor;
import com.example.sonic.fspotter.materialtest.MyApplication;
import com.example.sonic.fspotter.pojo.Movie;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Windows on 02-03-2015.
 */
public class MovieUtils {
    public static ArrayList<Movie> loadBoxOfficeMovies(RequestQueue requestQueue) {
        JSONObject response = Requestor.requestMoviesJSON(requestQueue, Endpoints.getRequestUrlBoxOfficeMovies(30));
        if (response != null) {
            Log.v("JSON RESPONSE", response.toString());
        }
        ArrayList<Movie> listMovies = Parser.parseMoviesJSON(response);
        MyApplication.getWritableDatabase().insertMovies(DBMovies.BOX_OFFICE, listMovies, true);
        return listMovies;
    }

    public static ArrayList<Movie> loadUpcomingMovies(RequestQueue requestQueue) {
        JSONObject response = Requestor.requestMoviesJSON(requestQueue, Endpoints.getRequestUrlUpcomingMovies(30));
        ArrayList<Movie> listMovies = Parser.parseMoviesJSON(response);
        MyApplication.getWritableDatabase().insertMovies(DBMovies.UPCOMING, listMovies, true);
        return listMovies;
    }
}
