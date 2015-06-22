package com.example.sonic.fspotter.task;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.example.sonic.fspotter.callbacks.LocationsLoadedListener;
import com.example.sonic.fspotter.extras.LocationUtils;
import com.example.sonic.fspotter.network.VolleySingleton;
import com.example.sonic.fspotter.pojo.Location;

import java.util.ArrayList;


/**
 * Created by Windows on 02-03-2015.
 */
public class TaskLoadLocations extends AsyncTask<Void, Void, ArrayList<Location>> {
    private LocationsLoadedListener myComponent;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;


    public TaskLoadLocations(LocationsLoadedListener myComponent) {

        this.myComponent = myComponent;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
    }


    @Override
    protected ArrayList<Location> doInBackground(Void... params) {

        ArrayList<Location> listMovies = LocationUtils.loadLocations(requestQueue);
        return listMovies;
    }

    @Override
    protected void onPostExecute(ArrayList<Location> listMovies) {
        if (myComponent != null) {
            myComponent.onLocationsLoaded(listMovies);
        }
    }


}
