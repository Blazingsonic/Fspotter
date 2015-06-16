package com.example.sonic.fspotter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sonic on 14.06.15.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<Kneipe> mLocations;
    private Context mContext;

    public ListAdapter(List<Kneipe> locations, Context context) {
        mLocations = locations;
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mLocationLabel;
        public IMyViewHolderClicks mListener;

        public TextView mName;
        public TextView mAdresse;
        public TextView mTyp;
        public TextView mBewertung;

        public ViewHolder(View itemView, IMyViewHolderClicks listener) {
            super(itemView);

            mName = (TextView) itemView.findViewById(R.id.locationNameLabel);

            mListener = listener;

            mListener = listener;
            //mLocationLabel =

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onPotato(view, this.getAdapterPosition());
        }

        public static interface IMyViewHolderClicks {
            public void onPotato(View caller, int position);
        }
    }

    @Override
    public int getItemCount() {
        return mLocations.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Assign values to the views
        holder.mName.setText(mLocations.get(position).getName());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.fragment_list_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view, new ListAdapter.ViewHolder.IMyViewHolderClicks() {

            public void onPotato(View caller, int position) {
                // Start new intent
                Intent intent = new Intent(mContext, LocationDetailActivity.class);
                intent.putExtra("Name", mLocations.get(position).getName());
                mContext.startActivity(intent);
            }
        });

        return holder;
    }
}
