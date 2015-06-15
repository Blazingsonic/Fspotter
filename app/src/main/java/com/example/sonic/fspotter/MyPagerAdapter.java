package com.example.sonic.fspotter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by sonic on 14.06.15.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0: return MapFragment.newInstance("MapFragment", "Instance 1");
            //case 1: return PhotoFragment.newInstance("PhotoFragment", "Instance 1");
            case 1: return ListFragment.newInstance("ListFragment", "Instance 1");
            default: return ListFragment.newInstance("ListFragment", "Instance Default");
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
