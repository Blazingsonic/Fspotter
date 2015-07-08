package com.example.sonic.fspotter.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.sonic.fspotter.R;
import com.example.sonic.fspotter.pojo.Image;
import com.example.sonic.fspotter.pojo.Location;

import java.util.ArrayList;

/**
 * Created by sonic on 03.07.15.
 */
public class AdapterCustomPager extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<Image> mListImages;

    int[] mResources = {
            R.drawable.first,
            R.drawable.second,
            R.drawable.third
    };

    public AdapterCustomPager(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setImages(ArrayList<Image> listImages) {
        this.mListImages = listImages;
        //update the adapter to reflect the new set of movies
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mListImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);

        Log.v("LIST IMAGES INSTANT", mListImages.toString());
        if (mListImages != null) {
            Bitmap bitmap = stringToBitMap(mListImages.get(position).getImage());
            imageView.setImageBitmap(bitmap);

        } else {
            imageView.setImageResource(mResources[position]);
        }

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

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
}
