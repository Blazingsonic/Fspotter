package com.example.sonic.fspotter.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sonic.fspotter.R;
import com.example.sonic.fspotter.activities.ActivityMap;
import com.example.sonic.fspotter.database.DBLocations;
import com.example.sonic.fspotter.fspotter.MyApplication;
import com.example.sonic.fspotter.pojo.Location;
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

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sonic on 27.06.15.
 */
public class FragmentMapDetail extends Fragment {

    public static final String TAG = FragmentMapDetail.class.getSimpleName();

    MapView mMapView;
    private GoogleMap googleMap;

    private static final String ARG_PARAM1 = "locationName";
    private static final String ARG_PARAM2 = "locationLatitude";
    private static final String ARG_PARAM3 = "locationLongitude";
    private static final String ARG_PARAM4 = "locationMapIconId";

    private String mLocationName;
    private String mLocationLatitudeToString;
    private String mLocationLongitudeToString;
    private String mLocationMapIconId;

    private OnFragmentInteractionListener mListener;

    public FragmentMapDetail() {
    }

    public static FragmentMapDetail newInstance(String locationName, String locationLatitudeToString, String locationLongitudeToString, String locationMapIconId) {

        FragmentMapDetail fragment = new FragmentMapDetail();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM1, locationName);
        bundle.putString(ARG_PARAM2, locationLatitudeToString);
        bundle.putString(ARG_PARAM3, locationLongitudeToString);
        bundle.putString(ARG_PARAM4, locationMapIconId);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        // inflate and return the layout
        View v = inflater.inflate(R.layout.fragment_map_detail, container,
                false);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        initializeMap();


        return v;
    }

    private void initializeMap () {
        googleMap = mMapView.getMap();

        if (googleMap == null) {
            Toast.makeText(getActivity(),
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();
        } else {
            if (!mLocationName.isEmpty() && !mLocationLatitudeToString.isEmpty() && !mLocationLongitudeToString.isEmpty() && !mLocationMapIconId.isEmpty()) {
                initializeMarker();
            }
        }
    }

    private void initializeMarker() {

        // latitude and longitude
        double locationLatitudeToDouble = Double.parseDouble(mLocationLatitudeToString);
        double locationLongitudeToDouble = Double.parseDouble(mLocationLongitudeToString);

        // create marker
        if (mLocationMapIconId.equals("Baum")) {
            MarkerOptions marker = new MarkerOptions().position(new LatLng(locationLatitudeToDouble, locationLongitudeToDouble)).title(mLocationName).icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.baum));
            // adding marker
            googleMap.addMarker(marker);

        } else if (mLocationMapIconId.equals("Berg")) {
            MarkerOptions marker = new MarkerOptions().position(new LatLng(locationLatitudeToDouble, locationLongitudeToDouble)).title(mLocationName).icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.berg));
            // adding marker
            googleMap.addMarker(marker);

        } else if (mLocationMapIconId.equals("Kirche")) {
            MarkerOptions marker = new MarkerOptions().position(new LatLng(locationLatitudeToDouble, locationLongitudeToDouble)).title(mLocationName).icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.kirche));
            // adding marker
            googleMap.addMarker(marker);

        } else if (mLocationMapIconId.equals("Leuchtturm")) {
            MarkerOptions marker = new MarkerOptions().position(new LatLng(locationLatitudeToDouble, locationLongitudeToDouble)).title(mLocationName).icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.leuchtturm));
            // adding marker
            googleMap.addMarker(marker);

        } else if (mLocationMapIconId.equals("Wasserfall")) {
            MarkerOptions marker = new MarkerOptions().position(new LatLng(locationLatitudeToDouble, locationLongitudeToDouble)).title(mLocationName).icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.wasserfall));
            // adding marker
            googleMap.addMarker(marker);

        } else {
            MarkerOptions marker = new MarkerOptions().position(new LatLng(locationLatitudeToDouble, locationLongitudeToDouble)).title(mLocationName).icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.sonstiges));
            // adding marker
            googleMap.addMarker(marker);

        }

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(locationLatitudeToDouble, locationLongitudeToDouble)).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mLocationName = getArguments().getString(ARG_PARAM1);
            mLocationLatitudeToString = getArguments().getString(ARG_PARAM2);
            mLocationLongitudeToString = getArguments().getString(ARG_PARAM3);
            mLocationMapIconId = getArguments().getString(ARG_PARAM4);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}
