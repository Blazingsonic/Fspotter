package com.example.sonic.fspotter.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.sonic.fspotter.R;
import com.example.sonic.fspotter.activities.ActivityDetail;
import com.example.sonic.fspotter.anim.AnimationUtils;
import com.example.sonic.fspotter.extras.Constants;
import com.example.sonic.fspotter.network.VolleySingleton;
import com.example.sonic.fspotter.pojo.Location;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * Created by Windows on 12-02-2015.
 */
public class AdapterLocations extends RecyclerView.Adapter<AdapterLocations.ViewHolderLocation> {

    //contains the list of movies
    private ArrayList<Location> mListLocations = new ArrayList<>();
    private LayoutInflater mInflater;
    private VolleySingleton mVolleySingleton;
    private ImageLoader mImageLoader;
    //formatter for parsing the dates in the specified format below
    private DateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd");
    //keep track of the previous position for animations where scrolling down requires a different animation compared to scrolling up
    private int mPreviousPosition = 0;
    Context mContext;


    public AdapterLocations(Context context) {
        mInflater = LayoutInflater.from(context);
        mVolleySingleton = VolleySingleton.getInstance();
        mImageLoader = mVolleySingleton.getImageLoader();
        mContext = context;
    }

    public void setLocations(ArrayList<Location> listLocations) {
        this.mListLocations = listLocations;
        //update the adapter to reflect the new set of movies
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderLocation onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.custom_location, parent, false);
        ViewHolderLocation viewHolder = new ViewHolderLocation(view, new ViewHolderLocation.IMyViewHolderClicks() {

            public void onPotato(View caller, int position) {
                // Start new intent
                Intent intent = new Intent(mContext, ActivityDetail.class);
                intent.putExtra("Name", mListLocations.get(position).getLocationName());
                mContext.startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderLocation holder, int position) {
        Location currentLocation = mListLocations.get(position);
        //one or more fields of the Location object may be null since they are fetched from the web
        holder.locationTitle.setText(currentLocation.getLocationName());
        holder.locationDescription.setText(currentLocation.getDescription());

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

        String urlThumnail = currentLocation.getHints();
        loadImages(urlThumnail, holder);

    }


    private void loadImages(String urlThumbnail, final ViewHolderLocation holder) {
        if (!urlThumbnail.equals(Constants.NA)) {
            mImageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    holder.locationThumbnail.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mListLocations.size();
    }

    public static class ViewHolderLocation extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView locationThumbnail;
        TextView locationTitle;
        TextView locationDescription;
        RatingBar locationScore;

        public IMyViewHolderClicks mListener;

        public ViewHolderLocation(View itemView, IMyViewHolderClicks listener) {
            super(itemView);
            locationThumbnail = (ImageView) itemView.findViewById(R.id.locationThumbnail);
            locationTitle = (TextView) itemView.findViewById(R.id.locationTitle);
            locationDescription = (TextView) itemView.findViewById(R.id.locationDescription);
            locationScore = (RatingBar) itemView.findViewById(R.id.locationScore);
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
