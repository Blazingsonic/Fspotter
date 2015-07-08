package com.example.sonic.fspotter.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.sonic.fspotter.R;
import com.example.sonic.fspotter.anim.AnimationUtils;
import com.example.sonic.fspotter.extras.Constants;
import com.example.sonic.fspotter.network.VolleySingleton;
import com.example.sonic.fspotter.pojo.Comment;
import com.example.sonic.fspotter.pojo.Location;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by sonic on 27.06.15.
 */
public class AdapterComments extends RecyclerView.Adapter<AdapterComments.ViewHolderLocation>{
    //contains the list of movies
    private ArrayList<Comment> mListComments = new ArrayList<>();
    private LayoutInflater mInflater;
    private VolleySingleton mVolleySingleton;
    //keep track of the previous position for animations where scrolling down requires a different animation compared to scrolling up
    private int mPreviousPosition = 0;
    Context mContext;


    public AdapterComments(Context context) {
        mInflater = LayoutInflater.from(context);
        mVolleySingleton = VolleySingleton.getInstance();
        mContext = context;
    }

    public void setComments(ArrayList<Comment> listComments) {
        this.mListComments = listComments;
        //update the adapter to reflect the new set of movies
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderLocation onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.custom_comment, parent, false);
        ViewHolderLocation viewHolder = new ViewHolderLocation(view, new ViewHolderLocation.IMyViewHolderClicks() {

            public void onPotato(View caller, int position) {

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderLocation holder, int position) {
        Comment currentComment = mListComments.get(position);
        //one or more fields of the Location object may be null since they are fetched from the web
        holder.locationComment.setText(currentComment.getComment());

        if (position > mPreviousPosition) {
            AnimationUtils.animateSunblind(holder, true);
//            AnimationUtils.animateSunblind(holder, true);
//            AnimationUtils.animate1(holder, true);
//            AnimationUtils.animate(holder,true);
        } else {
            AnimationUtils.animateSunblind(holder, false);
//            AnimationUtils.animateSunblind(holder, false);
//            AnimationUtils.animate1(holder, false);
//            AnimationUtils.animate(holder, false);
        }
        mPreviousPosition = position;
    }


    @Override
    public int getItemCount() {
        return mListComments.size();
    }

    public static class ViewHolderLocation extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView locationComment;

        public IMyViewHolderClicks mListener;

        public ViewHolderLocation(View itemView, IMyViewHolderClicks listener) {
            super(itemView);
            locationComment = (TextView) itemView.findViewById(R.id.locationComment);
            mListener = listener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onPotato(view, getPosition());
        }

        public static interface IMyViewHolderClicks {
            public void onPotato(View caller, int position);
        }
    }
}
