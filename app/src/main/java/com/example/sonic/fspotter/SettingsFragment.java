package com.example.sonic.fspotter;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

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
        return super.onCreateView(inflater, container, savedInstanceState);
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
}