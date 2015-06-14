package com.example.sonic.fspotter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sonic on 14.06.15.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<String> mLocations;
    private Context mContext;

    public ListAdapter(List<String> locations, Context context) {
        mLocations = locations;
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mLocationLabel;
        public IMyViewHolderClicks mListener;

        public ViewHolder(View itemView, IMyViewHolderClicks listener) {
            super(itemView);

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
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.listfrag_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view, new ListAdapter.ViewHolder.IMyViewHolderClicks() {

            public void onPotato(View caller, int position) {
                Log.i("Klick", mLocations.get(position));
            }
        });

        return holder;
    }
}
