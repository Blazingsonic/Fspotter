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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.sonic.fspotter.R;

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
 * Created by sonic on 16.06.15.
 */
public class SettingsFragment extends Fragment {

    public static final String TAG = SettingsFragment.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private String mfragmentName;
    private String mInstantiationCount;
    private int mPageNumber;

    private OnFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;

    @InjectView(R.id.spinnerView) Spinner mSpinner;
    @InjectView(R.id.spinnerView2) Spinner mSpinner2;
    @InjectView(R.id.testPostButton) Button mTestPostButton;

    public SettingsFragment() {
    }

    public static SettingsFragment newInstance(String fragmentName, String instantiationCount, int pageNumber) {

        SettingsFragment fragment = new SettingsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM1, fragmentName);
        bundle.putString(ARG_PARAM2, instantiationCount);
        bundle.putInt(ARG_PARAM3, pageNumber);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.inject(this, view);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadItemsInSpinner2(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    //((MainActivity)getActivity()).updateKneipenData("spinner2", position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mTestPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyHttpPost().execute();
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

    public void loadItemsInSpinner2(int position) {
        ArrayList<String> list = new ArrayList<String>();

        switch (position) {
            case 0:
                Log.i(TAG, "Auswählen");
                list.add("Zuerst Kategorie wählen");
                break;
            case 1:
                Log.i(TAG, "Sterne ausgewählt");
                list.add("Filter auswählen");
                list.add("5");
                list.add("4");
                list.add("3");
                list.add("2");
                list.add("1");
                break;
            case 2:
                Log.i(TAG, "Entfernung ausgewählt");
                break;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner2.setAdapter(adapter);
    }

    private class MyHttpPost extends AsyncTask<String, Void, Boolean> {


        @Override
        protected Boolean doInBackground(String... arg0) {

            Log.v(TAG, "POOOOSSST");
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost("http://46.101.165.28/task_manager/v1/register");

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            nameValuePair.add(new BasicNameValuePair("name", "testPost"));
            nameValuePair.add(new BasicNameValuePair("email", "testEmail@web.de"));
            nameValuePair.add(new BasicNameValuePair("password", "testPassword"));

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
