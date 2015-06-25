package com.example.sonic.fspotter.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
                intent.putExtra("id", String.valueOf(mListLocations.get(position).getId()));
                intent.putExtra("locationName", mListLocations.get(position).getLocationName());
                intent.putExtra("description", mListLocations.get(position).getDescription());
                intent.putExtra("hints", mListLocations.get(position).getHints());
                intent.putExtra("latitude", String.valueOf(mListLocations.get(position).getLatitude()));
                intent.putExtra("longitude", String.valueOf(mListLocations.get(position).getLongitude()));
                intent.putExtra("mapIconId", mListLocations.get(position).getMapIconId());
                intent.putExtra("rating", String.valueOf(mListLocations.get(position).getRating()));
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
        holder.locationMapIconId.setText(currentLocation.getMapIconId());
        holder.locationRating.setText(String.valueOf(currentLocation.getRating()));

        // encode string to bitmap
        Bitmap bitmap = stringToBitMap(currentLocation.getImage());
        holder.locationImage.setImageBitmap(bitmap);

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

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public Bitmap stringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return mListLocations.size();
    }

    public static class ViewHolderLocation extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView locationThumbnail;
        TextView locationTitle;
        TextView locationMapIconId;
        RatingBar locationScore;
        TextView locationRatingBackground;
        TextView locationRating;
        ImageView locationImage;

        public IMyViewHolderClicks mListener;

        public ViewHolderLocation(View itemView, IMyViewHolderClicks listener) {
            super(itemView);
            locationThumbnail = (ImageView) itemView.findViewById(R.id.locationThumbnail);
            locationTitle = (TextView) itemView.findViewById(R.id.locationTitle);
            locationMapIconId = (TextView) itemView.findViewById(R.id.locationMapIconId);
            locationScore = (RatingBar) itemView.findViewById(R.id.locationScore);
            locationRating = (TextView) itemView.findViewById(R.id.locationRating);
            locationRatingBackground = (TextView) itemView.findViewById(R.id.locationRatingBackground);
            locationImage = (ImageView) itemView.findViewById(R.id.locationImage);
            mListener = listener;

            //locationRatingBackground.setShadowLayer(30, 0, 0, Color.BLACK);
            locationRatingBackground.bringToFront();
            locationRating.bringToFront();

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
