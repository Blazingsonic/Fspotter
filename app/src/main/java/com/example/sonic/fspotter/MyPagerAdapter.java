package com.example.sonic.fspotter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;

/**
 * Created by sonic on 14.06.15.
 */
public class MyPagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter {

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0: return MapFragment.newInstance("MapFragment", "Instance 1");
            case 1: return CreateFragment.newInstance("CreateFragment", "Instance 1");
            case 2: return ListFragment.newInstance("ListFragment", "Instance 1");
            default: return ListFragment.newInstance("ListFragment", "Instance Default");
        }
    }

    @Override
    public int getIconResId(int i) {
        return 0;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch(position){
            case 0:
                title = "Map";
                break;
            case 1:
                title = "Create";
                break;
            case 2:
                title = "List";
                break;
        }
        return title;
    }
}
