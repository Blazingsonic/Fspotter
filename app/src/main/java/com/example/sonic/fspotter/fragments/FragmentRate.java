package com.example.sonic.fspotter.fragments;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sonic.fspotter.R;
import com.example.sonic.fspotter.database.DBLocations;
import com.example.sonic.fspotter.fspotter.MyApplication;
import com.example.sonic.fspotter.pojo.Comment;
import com.example.sonic.fspotter.pojo.Location;
import com.example.sonic.fspotter.pojo.Rating;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sonic on 24.06.15.
 */
public class FragmentRate extends Fragment {

    public static final String TAG = FragmentRate.class.getSimpleName();

    private static final String ARG_PARAM1 = "locationId";
    private static final String ARG_PARAM2 = "locationRating";

    private String mLocationIdToString;
    private String mLocationRatingToString;

    private OnFragmentInteractionListener mListener;

    @InjectView(R.id.locationRatingInput) EditText mLocationRatingView;
    @InjectView(R.id.submitRatingButton) Button mSubmitButtonView;
    @InjectView(R.id.locationRating) TextView mLocationRatingScoreView;

    public FragmentRate() {
    }

    public static FragmentRate newInstance(String locationIdToString, String locationRatingToString) {

        FragmentRate fragment = new FragmentRate();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM1, locationIdToString);
        bundle.putString(ARG_PARAM2, locationRatingToString);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rate, container, false);
        ButterKnife.inject(this, view);

        long locationIdToLong = Long.parseLong(mLocationIdToString);
        ArrayList<Rating> ratingOfId;
        ratingOfId = MyApplication.getWritableDatabase().getRatings(DBLocations.LOCATIONS, locationIdToLong);
        int sum = 0;
        long average;
        for (int j = 0; j < ratingOfId.size(); j++) {
            sum += ratingOfId.get(j).getRating();
        }
        if (sum != 0) {
            average = sum / ratingOfId.size();
            MyApplication.getWritableDatabase().updateLocation(locationIdToLong, average);
            mLocationRatingScoreView.setText(String.valueOf(average));
        }

        mSubmitButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!mLocationRatingView.getText().toString().isEmpty()) {
                    long checksum = Long.parseLong(mLocationRatingView.getText().toString());
                    if (checksum > 100) {
                        Toast.makeText(getActivity(), "You cant give more points than 100!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Insert ratings
                        ArrayList<Rating> newRatingList = new ArrayList<Rating>();
                        Rating newRating = new Rating();
                        long locationIdToLong = Long.parseLong(mLocationIdToString);
                        newRating.setId(locationIdToLong);
                        newRating.setLocationName("NA");
                        long newRatingToLong = Long.parseLong(mLocationRatingView.getText().toString());
                        newRating.setRating(newRatingToLong);
                        Log.v("NEW RATING", String.valueOf(newRating.getRating()));
                        newRatingList.add(newRating);
                        MyApplication.getWritableDatabase().insertRatings(DBLocations.LOCATIONS, newRatingList, false);

                        ArrayList<Rating> ratingOfId;
                        ratingOfId = MyApplication.getWritableDatabase().getRatings(DBLocations.LOCATIONS, locationIdToLong);
                        int sum = 0;
                        long average;
                        for (int j = 0; j < ratingOfId.size(); j++) {
                            sum += ratingOfId.get(j).getRating();
                        }
                        if (sum != 0) {
                            average = sum / ratingOfId.size();
                            MyApplication.getWritableDatabase().updateLocation(locationIdToLong, average);
                            mLocationRatingScoreView.setText(String.valueOf(average));
                        }

                        new MyHttpPost().execute();
                        Toast.makeText(getActivity(), "Rated!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Fill in the rating field first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mLocationIdToString = getArguments().getString(ARG_PARAM1);
            mLocationRatingToString = getArguments().getString(ARG_PARAM2);
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


    private class MyHttpPost extends AsyncTask<String, Void, Boolean> {


        @Override
        protected Boolean doInBackground(String... arg0) {

            Log.v(TAG, "POOOOSSST");
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost("http://46.101.165.28/task_manager/v1/ratings");

            httpPost.setHeader("Authorization", "bf03bbb0455edb2e2b228d0b2d66b468");

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            nameValuePair.add(new BasicNameValuePair("location_id", mLocationIdToString));
            nameValuePair.add(new BasicNameValuePair("rating", mLocationRatingView.getText().toString()));

            //Encoding POST data
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));

            } catch (UnsupportedEncodingException e)

            {
                e.printStackTrace();
            }

            try {

                HttpResponse response = httpClient.execute(httpPost);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
    }
}