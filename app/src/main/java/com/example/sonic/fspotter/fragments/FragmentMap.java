package com.example.sonic.fspotter.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sonic.fspotter.R;
import com.example.sonic.fspotter.callbacks.LocationsLoadedListener;
import com.example.sonic.fspotter.database.DBLocations;
import com.example.sonic.fspotter.fspotter.MyApplication;
import com.example.sonic.fspotter.logging.L;
import com.example.sonic.fspotter.pojo.Location;
import com.example.sonic.fspotter.task.TaskLoadLocations;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMap#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMap extends Fragment implements LocationsLoadedListener{
    public static final String TAG = FragmentMap.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    MapView mMapView;
    private GoogleMap googleMap;

    //The key used to store arraylist of movie objects to and from parcelable
    private static final String STATE_MOVIES = "state_movies";
    //the arraylist containing our list of box office his
    private ArrayList<Location> mListLocations = new ArrayList<>();

    public FragmentMap() {
    }

    public static FragmentMap newInstance(String param1, String param2) {

        FragmentMap fragment = new FragmentMap();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM1, param1);
        bundle.putString(ARG_PARAM2, param2);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Log.i(TAG, " " + mParam1 + ", " + mParam2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate and return the layout
        View v = inflater.inflate(R.layout.fragment_map, container,
                false);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //if this fragment starts for the first time, load the list of movies from a database
        mListLocations = MyApplication.getWritableDatabase().readLocations(DBLocations.LOCATIONS);
        //if the database is empty, trigger an AsycnTask to download movie list from the web

        if (!mListLocations.isEmpty()) {
            initializeMap(mListLocations);
        }

        return v;
    }

    private void initializeMap (ArrayList<Location> listLocations) {
        googleMap = mMapView.getMap();

        if (googleMap == null) {
            Toast.makeText(getActivity(),
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();
        } else {
            initializeMarkers(listLocations);
        }
    }

    private void initializeMarkers(ArrayList<Location> listLocations) {
        Log.v("MARKER", listLocations.toString());
        ArrayList<Marker> mMarkers = new ArrayList<>();

        for (int i = 0; i < listLocations.size(); i++) {
            BitmapDescriptor bitmapMarker;
            // latitude and longitude
            double locationLatitudeToDouble = listLocations.get(i).getLatitude();
            double locationLongitudeToDouble = listLocations.get(i).getLongitude();

            // create marker
            if (listLocations.get(i).getMapIconId().equals("Baum")) {
                bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.baum);

            } else if (listLocations.get(i).getMapIconId().equals("Berg")) {
                bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.berg);

            } else if (listLocations.get(i).getMapIconId().equals("Kirche")) {
                bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.kirche);

            } else if (listLocations.get(i).getMapIconId().equals("Leuchtturm")) {
                bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.leuchtturm);

            } else if (listLocations.get(i).getMapIconId().equals("Wasserfall")) {
                bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.wasserfall);

            } else {
                bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.sonstiges);
            }

            mMarkers.add(googleMap.addMarker(new MarkerOptions().position(new LatLng(listLocations.get(i).getLatitude(), listLocations.get(i).getLongitude())).title(listLocations.get(i).getLocationName()).icon(bitmapMarker)));
        }
        // CameraPosition should be our current location
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(42.82348923, 8.34987234)).zoom(7).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        mListLocations = MyApplication.getWritableDatabase().readLocations(DBLocations.LOCATIONS);
        if (!mListLocations.isEmpty()) {
            initializeMarkers(mListLocations);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    /**
     * Called when the AsyncTask finishes load the list of movies from the web
     */
    @Override
    public void onLocationsLoaded(ArrayList<Location> listLocations) {
        mListLocations = listLocations;
        initializeMarkers(mListLocations);
    }
}
