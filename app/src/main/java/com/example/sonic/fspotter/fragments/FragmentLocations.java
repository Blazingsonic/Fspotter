package com.example.sonic.fspotter.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.sonic.fspotter.R;
import com.example.sonic.fspotter.adapters.AdapterLocations;
import com.example.sonic.fspotter.callbacks.LocationsLoadedListener;
import com.example.sonic.fspotter.database.DBLocations;
import com.example.sonic.fspotter.extras.LocationSorter;
import com.example.sonic.fspotter.extras.SortListener;
import com.example.sonic.fspotter.logging.L;
import com.example.sonic.fspotter.fspotter.MyApplication;
import com.example.sonic.fspotter.pojo.Location;
import com.example.sonic.fspotter.task.TaskLoadLocations;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentLocations#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentLocations extends Fragment implements SortListener, LocationsLoadedListener, SwipeRefreshLayout.OnRefreshListener {

    //The key used to store arraylist of movie objects to and from parcelable
    private static final String STATE_MOVIES = "state_movies";
    //the arraylist containing our list of box office his
    private ArrayList<Location> mListLocations = new ArrayList<>();
    //the adapter responsible for displaying our movies within a RecyclerView
    private AdapterLocations mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    //the recyclerview containing showing all our movies
    private RecyclerView mRecyclerLocations;
    //the TextView containing error messages generated by Volley
    private TextView mTextError;
    //the sorter responsible for sorting our movie results based on choice made by the user in the FAB
    private LocationSorter mSorter = new LocationSorter();

    public FragmentLocations() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentLocations.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentLocations newInstance(String param1, String param2) {
        FragmentLocations fragment = new FragmentLocations();
        Bundle args = new Bundle();
        //put any extra arguments that you may want to supply to this fragment
        fragment.setArguments(args);
        return fragment;
    }

    public void onLocationsLoaded() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_locations, container, false);
        mTextError = (TextView) layout.findViewById(R.id.textVolleyError);
        mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipeLocations);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerLocations = (RecyclerView) layout.findViewById(R.id.listLocations);
        //set the layout manager before trying to display data
        mRecyclerLocations.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new AdapterLocations(getActivity());
        mRecyclerLocations.setAdapter(mAdapter);

        if (savedInstanceState != null) {
            //if this fragment starts after a rotation or configuration change, load the existing movies from a parcelable
            mListLocations = savedInstanceState.getParcelableArrayList(STATE_MOVIES);
        } else {
            //if this fragment starts for the first time, load the list of movies from a database
            mListLocations = MyApplication.getWritableDatabase().readLocations(DBLocations.LOCATIONS);
            //if the database is empty, trigger an AsycnTask to download movie list from the web
            if (mListLocations.isEmpty()) {
                L.m("FragmentLocations: executing task from fragment");
                new TaskLoadLocations(this).execute();
            }
        }
        //update your Adapter to containg the retrieved movies
        mAdapter.setLocations(mListLocations);
        return layout;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //save the movie list to a parcelable prior to rotation or configuration change
        outState.putParcelableArrayList(STATE_MOVIES, mListLocations);
    }


    private void handleVolleyError(VolleyError error) {
        //if any error occurs in the network operations, show the TextView that contains the error message
        mTextError.setVisibility(View.VISIBLE);
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            mTextError.setText(R.string.error_timeout);

        } else if (error instanceof AuthFailureError) {
            mTextError.setText(R.string.error_auth_failure);
            //TODO
        } else if (error instanceof ServerError) {
            mTextError.setText(R.string.error_auth_failure);
            //TODO
        } else if (error instanceof NetworkError) {
            mTextError.setText(R.string.error_network);
            //TODO
        } else if (error instanceof ParseError) {
            mTextError.setText(R.string.error_parser);
            //TODO
        }
    }

    /**
     * Called when the user chooses to sort results by name through the menu displayed inside FAB
     */
    @Override
    public void onSortByName() {
        mSorter.sortLocationsByName(mListLocations);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Called when the user chooses to sort results by date through the menu displayed inside FAB
     */
    @Override
    public void onSortByDate() {
        mSorter.sortLocationsByDate(mListLocations);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Called when the user chooses to sort results by rating through the menu displayed inside FAB
     */
    @Override
    public void onSortByRating() {
        mSorter.sortLocationsByRating(mListLocations);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Called when the AsyncTask finishes load the list of movies from the web
     */
    @Override
    public void onLocationsLoaded(ArrayList<Location> listLocations) {
        L.m("FragmentLocations: onLocationsLoaded Fragment");
        //update the Adapter to contain the new movies downloaded from the web
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        mAdapter.setLocations(listLocations);
    }

    @Override
    public void onRefresh() {
        L.t(getActivity(), "onRefresh");
        //load the whole feed again on refresh, dont try this at home :)
        new TaskLoadLocations(this).execute();

    }
}
