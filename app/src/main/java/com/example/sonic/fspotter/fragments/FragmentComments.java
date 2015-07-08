package com.example.sonic.fspotter.fragments;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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
import com.example.sonic.fspotter.adapters.AdapterComments;
import com.example.sonic.fspotter.adapters.AdapterLocations;
import com.example.sonic.fspotter.database.DBLocations;
import com.example.sonic.fspotter.fspotter.MyApplication;
import com.example.sonic.fspotter.pojo.Comment;

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
public class FragmentComments extends Fragment {

    public static final String TAG = FragmentComments.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";

    private String mLocationIdToString;

    private OnFragmentInteractionListener mListener;
    private AdapterComments mAdapter;
    private RecyclerView mRecyclerComments;

    private EditText mCommentView;
    private Button mSubmitCommentButton;

    public FragmentComments() {
    }

    public static FragmentComments newInstance(String locationIdToString) {

        FragmentComments fragment = new FragmentComments();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM1, locationIdToString);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_comments, container, false);
        mCommentView = (EditText) layout.findViewById(R.id.commentInput);
        mSubmitCommentButton = (Button) layout.findViewById(R.id.submitCommentButton);
        mRecyclerComments = (RecyclerView) layout.findViewById(R.id.listComments);
        mRecyclerComments.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new AdapterComments(getActivity());
        mRecyclerComments.setAdapter(mAdapter);


        long locationIdToLong = Long.parseLong(mLocationIdToString);
        ArrayList<Comment> listComments = MyApplication.getWritableDatabase().getComments(DBLocations.LOCATIONS, locationIdToLong);
        for (int i = 0; i < listComments.size(); i++) {
            Log.v("LIST COMMENTS", listComments.get(i).getLocationName());
            Log.v("LIST COMMENTS", listComments.get(i).getComment());
        }
        ArrayList<Comment> cleanComments = new ArrayList<>();
        for (int i = 0; i < listComments.size(); i++) {
            if (!listComments.get(i).getComment().equals("NA")) {
                cleanComments.add(listComments.get(i));
            }
        }

        mAdapter.setComments(cleanComments);

        mSubmitCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!mCommentView.getText().toString().isEmpty()) {
                    ArrayList<Comment> newCommentList = new ArrayList<Comment>();
                    Comment newComment = new Comment();
                    long locationIdToLong = Long.parseLong(mLocationIdToString);
                    newComment.setId(locationIdToLong);
                    newComment.setLocationName("NA");
                    newComment.setComment(mCommentView.getText().toString());
                    newCommentList.add(newComment);
                    // Insert Comment
                    MyApplication.getWritableDatabase().insertComments(DBLocations.LOCATIONS, newCommentList, false);

                    // Get all comments
                    ArrayList<Comment> listAllComments = MyApplication.getWritableDatabase().getComments(DBLocations.LOCATIONS, locationIdToLong);
                    mAdapter.setComments(listAllComments);

                    new MyHttpPost().execute();
                    Toast.makeText(getActivity(), "Commented!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Write a comment first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return layout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mLocationIdToString = getArguments().getString(ARG_PARAM1);
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

            HttpPost httpPost = new HttpPost("http://46.101.165.28/task_manager/v1/comments");

            httpPost.setHeader("Authorization", "bf03bbb0455edb2e2b228d0b2d66b468");

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            nameValuePair.add(new BasicNameValuePair("location_id", mLocationIdToString));
            nameValuePair.add(new BasicNameValuePair("comment", mCommentView.getText().toString()));

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

