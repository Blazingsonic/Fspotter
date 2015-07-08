package com.example.sonic.fspotter.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sonic.fspotter.R;
import com.example.sonic.fspotter.activities.ActivityMap;
import com.example.sonic.fspotter.adapters.AdapterCustomPager;
import com.example.sonic.fspotter.database.DBLocations;
import com.example.sonic.fspotter.fspotter.MyApplication;
import com.example.sonic.fspotter.pojo.Image;
import com.example.sonic.fspotter.pojo.Rating;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sonic on 24.06.15.
 */
public class FragmentInfo extends Fragment{

    public static final String TAG = FragmentInfo.class.getSimpleName();

    private static final String ARG_PARAM1 = "locationId";
    private static final String ARG_PARAM2 = "locationName";
    private static final String ARG_PARAM3 = "locationDescription";
    private static final String ARG_PARAM4 = "locationHints";
    private static final String ARG_PARAM5 = "locationLatitude";
    private static final String ARG_PARAM6 = "locationLongitude";
    private static final String ARG_PARAM7 = "locationMapIconId";
    private static final String ARG_PARAM8 = "locationRating";
    //private static final String ARG_PARAM9 = "locationImage";

    private String mLocationIdToString;
    private String mLocationName;
    private String mLocationDescription;
    private String mLocationHints;
    private String mLocationLatitudeToString;
    private String mLocationLongitudeToString;
    private String mLocationMapIconId;
    private String mLocationRatingToString;
    //private String mLocationImage;

    @InjectView(R.id.locationIdLabel) TextView mIdToStringView;
    @InjectView(R.id.locationNameLabel) TextView mLocationNameView;
    @InjectView(R.id.locationDescription) TextView mLocationDescriptionView;
    @InjectView(R.id.locationHints) TextView mLocationHintsView;
    @InjectView(R.id.locationLatitude) TextView mLocationLatitudeView;
    @InjectView(R.id.locationLongitude) TextView mLocationLongitudeView;
    @InjectView(R.id.locationMapIconId) TextView mLocationMapIconIdView;
    @InjectView(R.id.locationRating) TextView mLocationRatingView;
    @InjectView(R.id.pager) ViewPager mViewPager;

    AdapterCustomPager mCustomPagerAdapter;

    private OnFragmentInteractionListener mListener;

    public FragmentInfo() {
    }

    public static FragmentInfo newInstance(String locationIdToString, String locationName, String locationDescription, String locationHints,String locationLatitudeToString, String locationLongitudeToString, String locationMapIconId, String locationRatingToString) {

        FragmentInfo fragment = new FragmentInfo();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM1, locationIdToString);
        bundle.putString(ARG_PARAM2, locationName);
        bundle.putString(ARG_PARAM3, locationDescription);
        bundle.putString(ARG_PARAM4, locationHints);
        bundle.putString(ARG_PARAM5, locationLatitudeToString);
        bundle.putString(ARG_PARAM6, locationLongitudeToString);
        bundle.putString(ARG_PARAM7, locationMapIconId);
        bundle.putString(ARG_PARAM8, locationRatingToString);
        //bundle.putString(ARG_PARAM9, locationImage);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        ButterKnife.inject(this, view);

        mCustomPagerAdapter = new AdapterCustomPager(getActivity());

        ArrayList<Image> listImages = MyApplication.getWritableDatabase().getImages(DBLocations.LOCATIONS, Long.parseLong(mLocationIdToString));
        if (listImages != null) {
            mCustomPagerAdapter.setImages(listImages);
        }

        mViewPager.setAdapter(mCustomPagerAdapter);

        long locationIdToLong = Long.parseLong(mLocationIdToString);
        ArrayList<Rating> ratingOfId;
        ratingOfId = MyApplication.getWritableDatabase().getRatings(DBLocations.LOCATIONS, locationIdToLong);
        if (ratingOfId != null) {
            int sum = 0;
            long average;
            for (int j = 0; j < ratingOfId.size(); j++) {
                sum += ratingOfId.get(j).getRating();
            }
            if (sum != 0) {
                average = sum / ratingOfId.size();
                //MyApplication.getWritableDatabase().updateLocation(locationIdToLong, average);
                mLocationRatingView.setText(String.valueOf(average));
            }
        } else {
            mLocationRatingView.setText("0");
        }


        mIdToStringView.setText(mLocationIdToString);
        mLocationNameView.setText(mLocationName);
        mLocationDescriptionView.setText(mLocationDescription);
        mLocationHintsView.setText(mLocationHints);
        mLocationLatitudeView.setText(mLocationLatitudeToString);
        mLocationLongitudeView.setText(mLocationLongitudeToString);
        mLocationMapIconIdView.setText(mLocationMapIconId);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mLocationIdToString = getArguments().getString(ARG_PARAM1);
            mLocationName = getArguments().getString(ARG_PARAM2);
            mLocationDescription = getArguments().getString(ARG_PARAM3);
            mLocationHints = getArguments().getString(ARG_PARAM4);
            mLocationLatitudeToString = getArguments().getString(ARG_PARAM5);
            mLocationLongitudeToString = getArguments().getString(ARG_PARAM6);
            mLocationMapIconId = getArguments().getString(ARG_PARAM7);
            mLocationRatingToString = getArguments().getString(ARG_PARAM8);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /*public Bitmap stringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }*/

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}
