package com.example.sonic.fspotter.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.sonic.fspotter.R;
import com.example.sonic.fspotter.database.DBLocations;
import com.example.sonic.fspotter.fragments.FragmentComments;
import com.example.sonic.fspotter.fragments.FragmentCreate;
import com.example.sonic.fspotter.fragments.FragmentDrawer;
import com.example.sonic.fspotter.fragments.FragmentDrawerDetail;
import com.example.sonic.fspotter.fragments.FragmentInfo;
import com.example.sonic.fspotter.fragments.FragmentLocations;
import com.example.sonic.fspotter.fragments.FragmentMapDetail;
import com.example.sonic.fspotter.fragments.FragmentRate;
import com.example.sonic.fspotter.fspotter.MyApplication;
import com.example.sonic.fspotter.pojo.Comment;
import com.example.sonic.fspotter.pojo.Image;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;


public class ActivityDetail extends ActionBarActivity implements MaterialTabListener {


    //int representing our 0th tab corresponding to the Fragment where search results are dispalyed
    public static final int TAB_INFO = 0;
    //int corresponding to our 1st tab corresponding to the Fragment where box office hits are dispalyed
    public static final int TAB_COMMENTS = 1;
    //int corresponding to our 2nd tab corresponding to the Fragment where upcoming movies are displayed
    public static final int TAB_RATING = 2;
    public static final int TAB_MAP = 3;
    //int corresponding to the number of tabs in our Activity
    public static final int TAB_COUNT = 4;

    public static final String TAG = ActivityDetail.class.getSimpleName();
    //int containing the duration of the animation run when items are added or removed from the RecyclerView
    public static final int ANIMATION_DURATION = 2000;
    //edit text letting the user type item name to be added to the recylcerview
    private EditText mInput;
    //recyclerview showing all items added by the user
    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private ViewPagerAdapter mAdapter;
    private ViewGroup mContainerToolbar;
    private MaterialTabHost mTabHost;
    private ViewPager mPager;
    private FragmentDrawerDetail mDrawerFragment;

    private String mIdToString;
    private String mLocationName;
    private String mLocationDescription;
    private String mLocationHints;
    private String mLocationLatitudeToString;
    private String mLocationLongitudeToString;
    private String mLocationMapIconId;
    private String mLocationRatingToString;
    //private String mLocationImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        setupTabs();
        setupToolbar();
        //setupDrawer();

        // Get data from intent
        Intent intent = getIntent();

        mIdToString = intent.getStringExtra("id");
        mLocationName = intent.getStringExtra("locationName");
        mLocationDescription = intent.getStringExtra("description");
        mLocationHints = intent.getStringExtra("hints");
        mLocationLatitudeToString = intent.getStringExtra("latitude");
        mLocationLongitudeToString = intent.getStringExtra("longitude");
        mLocationMapIconId = intent.getStringExtra("mapIconId");
        mLocationRatingToString = intent.getStringExtra("rating");
        //mLocationImage = intent.getStringExtra("image");

    }

    private void setupDrawer() {
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        mContainerToolbar = (ViewGroup) findViewById(R.id.container_app_bar);
        //set the Toolbar as ActionBar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //setup the NavigationDrawer
        mDrawerFragment = (FragmentDrawerDetail)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        mDrawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
    }

    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setupTabs() {
        mTabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);
        mPager = (ViewPager) findViewById(R.id.viewPager);
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        //when the page changes in the ViewPager, update the Tabs accordingly
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mTabHost.setSelectedNavigationItem(position);

            }
        });
        //Add all the Tabs to the TabHost
        for (int i = 0; i < mAdapter.getCount(); i++) {
            mTabHost.addTab(
                    mTabHost.newTab()
                            .setIcon(mAdapter.getIcon(i))
                            .setTabListener(this));
        }
    }

    @Override
    public void onTabSelected(MaterialTab materialTab) {
        //when a Tab is selected, update the ViewPager to reflect the changes
        mPager.setCurrentItem(materialTab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {
    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        int icons[] = {R.drawable.info,
                R.drawable.comments,
                R.drawable.rating,
                R.drawable.map
        };

        FragmentManager fragmentManager;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentManager = fm;
        }

        public Fragment getItem(int num) {
            Fragment fragment = null;
//            L.m("getItem called for " + num);
            switch (num) {
                case TAB_INFO:
                    fragment = FragmentInfo.newInstance(mIdToString, mLocationName, mLocationDescription, mLocationHints, mLocationLatitudeToString, mLocationLongitudeToString, mLocationMapIconId, mLocationRatingToString);
                    break;
                case TAB_COMMENTS:
                    fragment = FragmentComments.newInstance(mIdToString);
                    break;
                case TAB_RATING:
                    fragment = FragmentRate.newInstance(mIdToString, mLocationRatingToString);
                    break;
                case TAB_MAP:
                    fragment = FragmentMapDetail.newInstance(mLocationName, mLocationLatitudeToString, mLocationLongitudeToString, mLocationMapIconId);
                    break;
            }
            return fragment;

        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getStringArray(R.array.tabs)[position];
        }

        private Drawable getIcon(int position) {
            return getResources().getDrawable(icons[position]);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_recycler_item_animations, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (android.R.id.home == id) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
