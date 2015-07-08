package com.example.sonic.fspotter.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sonic.fspotter.R;
import com.example.sonic.fspotter.activities.ActivitySetMarker;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class FragmentCreate extends Fragment {

    public static final String TAG = FragmentCreate.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private String mfragmentName;
    private String mInstantiationCount;
    private int mPageNumber;
    public String mImageString = "";
    public String vname;
    public String nname;

    public ArrayList<String> mStringArray = new ArrayList();

    private OnFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;

    @InjectView(R.id.spinnerView) Spinner mSpinner;
    @InjectView(R.id.setCoordinatesButton) Button mSetCoordinatesButton;
    @InjectView(R.id.locationNameInput) EditText mLocationNameInput;
    @InjectView(R.id.locationDescriptionInput) EditText mLocationDescription;
    @InjectView(R.id.locationHintsInput) EditText mLocationHints;
    @InjectView(R.id.addPictureButton) Button mAddPictureButton;
    @InjectView(R.id.chooseFromGalleryButton) Button mChooseFromGalleryButton;
    @InjectView(R.id.postButton) Button mPostButton;
    @InjectView(R.id.imagePreview) ImageView mImagePreview;
    @InjectView(R.id.imagePreview2) ImageView mImagePreview2;
    @InjectView(R.id.imagePreview3) ImageView mImagePreview3;
    @InjectView(R.id.setLatitudeView) TextView mSetLatitudeView;
    @InjectView(R.id.setLongitudeView) TextView mSetLongitudeView;

    public FragmentCreate() {
    }

    public static FragmentCreate newInstance(String fragmentName, String instantiationCount, int pageNumber) {

        FragmentCreate fragment = new FragmentCreate();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM1, fragmentName);
        bundle.putString(ARG_PARAM2, instantiationCount);
        bundle.putInt(ARG_PARAM3, pageNumber);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create, container, false);
        ButterKnife.inject(this, view);


        // Get intent
        Intent i = getActivity().getIntent();
        // Receiving the Data
        vname = i.getStringExtra("Latitude");
        nname = i.getStringExtra("Longitude");
        Log.e("zweite Activity", vname + "." + nname);

        // Displaying Received data
        mSetLatitudeView.setText(vname);
        mSetLongitudeView.setText(nname);


        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSetCoordinatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivitySetMarker.class);
                startActivity(intent);
            }
        });
        mAddPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1888);
            }
        });
        mChooseFromGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 1889);
            }
        });
        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mLocationNameInput.getText().toString().isEmpty() && !mLocationDescription.getText().toString().isEmpty() && !mLocationHints.getText().toString().isEmpty()
                        && !mLocationNameInput.getText().toString().isEmpty() && !mSpinner.getSelectedItem().toString().isEmpty() && !mImageString.isEmpty() && !vname.isEmpty() && !nname.isEmpty()) {
                    new MyHttpPost().execute();
                    new MyHttpPostImages().execute();
                    Toast.makeText(getActivity(), "Posted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Fill in all fiels and make at least one pic", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mfragmentName = getArguments().getString(ARG_PARAM1);
            mInstantiationCount = getArguments().getString(ARG_PARAM2);
            mPageNumber = getArguments().getInt(ARG_PARAM3);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1888 && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            if (mImagePreview.getDrawable() == null) {
                mImagePreview.setImageBitmap(photo);
            } else if (mImagePreview2.getDrawable() == null) {
                mImagePreview2.setImageBitmap(photo);
            } else if (mImagePreview3.getDrawable() == null) {
                mImagePreview3.setImageBitmap(photo);
            } else {
                Toast.makeText(getActivity(), "3 images max",
                        Toast.LENGTH_LONG).show();
            }

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
            byte [] byte_arr = stream.toByteArray();
            mImageString = Base64.encodeToString(byte_arr, Base64.DEFAULT);

            mStringArray.add(mImageString);

            Log.v("IMAGE STRING", mImageString);
        } else if (requestCode == 1889 && data != null) {
            Uri uri = data.getData();

            try {
                Bitmap photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                mImagePreview.setImageBitmap(photo);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
                byte [] byte_arr = stream.toByteArray();
                mImageString = Base64.encodeToString(byte_arr, Base64.DEFAULT);

                mStringArray.add(mImageString);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "You haven't picked an image",
                    Toast.LENGTH_LONG).show();
        }
    }

    private class MyHttpPost extends AsyncTask<String, Void, Boolean> {


        @Override
        protected Boolean doInBackground(String... arg0) {

            Log.v(TAG, vname + " " + nname);
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost("http://46.101.165.28/task_manager/v1/locations");

            httpPost.setHeader("Authorization", "bf03bbb0455edb2e2b228d0b2d66b468");

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            nameValuePair.add(new BasicNameValuePair("locationName", mLocationNameInput.getText().toString()));
            nameValuePair.add(new BasicNameValuePair("description", mLocationDescription.getText().toString()));
            nameValuePair.add(new BasicNameValuePair("hints", mLocationHints.getText().toString()));
            nameValuePair.add(new BasicNameValuePair("latitude", vname));
            nameValuePair.add(new BasicNameValuePair("longitude", nname));
            nameValuePair.add(new BasicNameValuePair("mapIconId", mSpinner.getSelectedItem().toString()));
            nameValuePair.add(new BasicNameValuePair("image", mImageString));

            //Encoding POST data
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));

            } catch (UnsupportedEncodingException e)

            {
                e.printStackTrace();
            }

            try {
                HttpResponse response = httpClient.execute(httpPost);

                //if (response.getStatusLine().getStatusCode() == 200) {
                    //Toast.makeText(getActivity(), "Posted!", Toast.LENGTH_SHORT).show();
                //}
                // if response != empty
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        }
    }

    private class MyHttpPostImages extends AsyncTask<String, Void, Boolean> {


        @Override
        protected Boolean doInBackground(String... arg0) {

            Log.v(TAG, "POOOOSSST");
            HttpClient httpClient = new DefaultHttpClient();

            for (int i = 0; i < mStringArray.size(); i++) {
                HttpPost httpPost = new HttpPost("http://46.101.165.28/task_manager/v1/images");

                httpPost.setHeader("Authorization", "bf03bbb0455edb2e2b228d0b2d66b468");

                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                nameValuePair.add(new BasicNameValuePair("locationName", mLocationNameInput.getText().toString()));
                nameValuePair.add(new BasicNameValuePair("image", mStringArray.get(i)));

                //Encoding POST data
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));

                } catch (UnsupportedEncodingException e)

                {
                    e.printStackTrace();
                }

                try {
                    HttpResponse response = httpClient.execute(httpPost);

                    //if (response.getStatusLine().getStatusCode() == 200) {
                    //Toast.makeText(getActivity(), "Posted!", Toast.LENGTH_SHORT).show();
                    //}
                    // if response != empty
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
    }
}
