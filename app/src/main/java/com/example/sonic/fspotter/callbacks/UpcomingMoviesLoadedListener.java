package com.example.sonic.fspotter.callbacks;

import com.example.sonic.fspotter.pojo.Location;

import java.util.ArrayList;


/**
 * Created by Windows on 13-04-2015.
 */
public interface UpcomingMoviesLoadedListener {
    public void onUpcomingMoviesLoaded(ArrayList<Location> listMovies);
}
