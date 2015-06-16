package com.example.sonic.fspotter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.viewpagerindicator.IconPagerAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sonic on 14.06.15.
 */
public class MyPagerAdapter extends FragmentStatePagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

    private Map<Integer, String> mFragmentsTags;
    private FragmentManager mFragmentManager;
    private int tabIcons[] = {R.mipmap.marker, R.mipmap.diaphragm, R.mipmap.list, R.mipmap.settings};

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
            case 3: return SettingsFragment.newInstance("SettingsFragment", "Instance 1", 3);
            default: return CreateFragment.newInstance("ListFragment", "Instance Default");
        }
    }

    @Override
    public int getPageIconResId(int i) {
        return tabIcons[i];
    }

    @Override
    public int getCount() {
        return 4;
    }

    /*@Override
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
    }*/

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
