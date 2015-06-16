package com.example.sonic.fspotter;

import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sonic on 14.06.15.
 */
public class ListFragment extends Fragment {

    public static final String TAG = ListFragment.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private String mfragmentName;
    private String mInstantiationCount;
    private int mPageNumber;

    public static ArrayList<Kneipe> mKneipenListFragment = null;
    public static ArrayList<Kneipe> mKneipenFilteredListFragment = null;

    private OnFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;

    public ListFragment() {
    }

    public static ListFragment newInstance(String fragmentName, String instantiationCount, int pageNumber) {

        ListFragment fragment = new ListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM1, fragmentName);
        bundle.putString(ARG_PARAM2, instantiationCount);
        bundle.putInt(ARG_PARAM3, pageNumber);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.listfrag, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        return rootView;
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        if (MainActivity.mKneipenFiltered != null && mKneipenFilteredListFragment == null) {
            mKneipenFilteredListFragment = MainActivity.mKneipenFiltered;
            Log.v(TAG, "hier ist das Fragment onViewCreated" + mKneipenFilteredListFragment.toString());

            updateDisplay(mKneipenFilteredListFragment);
        }

        super.onViewCreated(view, savedInstanceState);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mKneipenListFragment != null) {
            Log.v(TAG, "hier ist das Fragment onResume" + mKneipenListFragment.toString());
        }

        if (MainActivity.mKneipen != null && mKneipenListFragment == null) {
            mKneipenListFragment = MainActivity.mKneipen;
            Log.v(TAG, "hier ist das Fragment onResume" + mKneipenListFragment.toString());

            updateDisplay(mKneipenListFragment);
        }
    }

    private void updateDisplay(final ArrayList<Kneipe> kneipen) {
        Log.i(TAG, kneipen.toString());

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.setHasFixedSize(true); // Not always recommended, but in this case enhances performance

                ListAdapter mAdapter = new ListAdapter(kneipen, getActivity());
                mRecyclerView.setAdapter(mAdapter);
            }
        });
    }

}
