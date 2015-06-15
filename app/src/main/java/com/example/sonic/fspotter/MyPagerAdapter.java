package com.example.sonic.fspotter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.viewpagerindicator.IconPagerAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sonic on 14.06.15.
 */
public class MyPagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter {

    private Map<Integer, String> mFragmentsTags;
    private FragmentManager mFragmentManager;

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
        mFragmentsTags = new HashMap<Integer, String>();
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0: return MapFragment.newInstance("MapFragment", "Instance 1");
            case 1: return CreateFragment.newInstance("CreateFragment", "Instance 1");
            case 2: return ListFragment.newInstance("ListFragment", "Instance 1", 2);
            default: return ListFragment.newInstance("ListFragment", "Instance Default", 2);
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

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj = super.instantiateItem(container, position);
        if (obj instanceof Fragment) {
            Fragment f = (Fragment) obj;
            String tag = f.getTag();
            mFragmentsTags.put(position, tag);
        }
        return obj;
    }

    public Fragment getFragment(int position) {
        String tag = mFragmentsTags.get(position);
        if (tag == null) {
            return null;
        }
        return mFragmentManager.findFragmentByTag(tag);
    }
}
