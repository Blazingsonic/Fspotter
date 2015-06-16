package com.example.sonic.fspotter;

import android.content.Context;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.viewpagerindicator.TitlePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {

    /* * * * * * * * * * * * * * * * * * * *
     * VARIABLES
     * * * * * * * * * * * * * * * * * * * */

    public static final String TAG = MainActivity.class.getSimpleName();

    private String mCouchUrl = "https://gowdy.iriscouch.com/gowdy/_design/gowdy/_view/alleKneipen";
    public static FragmentManager fragmentManager;

    public static ArrayList<Kneipe> mKneipen = null;
    public static ArrayList<Kneipe> mKneipenFiltered = null;

    @InjectView(R.id.viewPager) ViewPager pager;

    // Navigation
    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();


    /* * * * * * * * * * * * * * * * * * * *
     * METHODS
     * * * * * * * * * * * * * * * * * * * */

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        createViewPager();
        setKneipenData();

        // Navigation
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.burger);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mNavItems.add(new NavItem("Home", "Get photographin'", R.mipmap.home));
        mNavItems.add(new NavItem("Other", "Some cool feature", R.mipmap.business));
        mNavItems.add(new NavItem("About", "Get to know about us", R.mipmap.info));

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });

        // DrawerToggle
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close)  {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d(TAG, "onDrawerClosed: " + getTitle());

                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    /* * * * * * * * * * * * * * * * * * * *
     * GET REQUESTS + UPDATE
     * * * * * * * * * * * * * * * * * * * */

    /*
     * Requests Kneipen objects from couchdb
     * */
    private void setKneipenData() {

        Log.v(TAG, "hello");

        final ArrayList<Kneipe> kneipen = new ArrayList<Kneipe>();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(mCouchUrl).build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                // Error
                Log.e(TAG, e.toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                // Success
                String jsonData = response.body().string();
                try {
                    JSONObject jsonMain = new JSONObject(jsonData);
                    Log.v(TAG, jsonMain.toString());

                    JSONArray jsonRows = jsonMain.getJSONArray("rows");
                    Log.v(TAG, jsonRows.toString());

                    for (int i = 0; i < jsonRows.length(); i++) {
                        Log.v(TAG + " KneipenArray", jsonRows.get(i).toString());

                        JSONObject jsonKneipe = jsonRows.getJSONObject(i);

                        String value = jsonKneipe.getString("value");

                        JSONObject jsonValue = new JSONObject(value);
                        Log.v(TAG, jsonValue.toString());

                        String name = jsonValue.getString("name");
                        String adresse = jsonValue.getString("adresse");
                        String typ = jsonValue.getString("kneipen_typ");
                        String bewertung = jsonValue.getString("bewertung");

                        Kneipe kneipe = makeKneipe(name, adresse, typ, bewertung);
                        Log.v(TAG, kneipe.toString());

                        kneipen.add(kneipe);
                        Log.v(TAG, kneipen.toString());

                        mKneipen = kneipen;
                        Log.v(TAG + " mKneipen", mKneipen.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*
     * Gets called when the list of Kneipen objects gets filtered
     * */
    public void updateKneipenData(String caller, int position) {
        mKneipenFiltered = new ArrayList<Kneipe>();

        switch (position) {
            case 0:
                mKneipenFiltered = mKneipen;
                break;
            case 1:
                for (int i = 0; i < mKneipen.size(); i++) {
                    if (mKneipen.get(i).getBewertung().equals("5")) {
                        Log.i(TAG, "This is 5 stars");
                        mKneipenFiltered.add(mKneipen.get(i));
                    }
                }
                break;
            case 2:
                for (int i = 0; i < mKneipen.size(); i++) {
                    if (mKneipen.get(i).getBewertung().equals("4")) {
                        Log.i(TAG, "This is 4 stars");
                        mKneipenFiltered.add(mKneipen.get(i));
                    }
                }
                break;
            case 3:
                for (int i = 0; i < mKneipen.size(); i++) {
                    if (mKneipen.get(i).getBewertung().equals("3")) {
                        Log.i(TAG, "This is 3 stars");
                        mKneipenFiltered.add(mKneipen.get(i));
                    }
                }
                break;
            case 4:
                for (int i = 0; i < mKneipen.size(); i++) {
                    if (mKneipen.get(i).getBewertung().equals("2")) {
                        Log.i(TAG, "This is 2 stars");
                        mKneipenFiltered.add(mKneipen.get(i));
                    }
                }
                break;
            case 5:
                for (int i = 0; i < mKneipen.size(); i++) {
                    if (mKneipen.get(i).getBewertung().equals("1")) {
                        Log.i(TAG, "This is 1 star");
                        mKneipenFiltered.add(mKneipen.get(i));
                    }
                }
                break;
        }
        createViewPager();
    }

    /*
     * Build an new Kneipen object which gets put into the Kneipen array
     * */
    private Kneipe makeKneipe(String name, String adresse, String typ, String bewertung) {
        Kneipe kneipe = new Kneipe();
        kneipe.setName(name);
        kneipe.setAdresse(adresse);
        kneipe.setTyp(typ);
        kneipe.setBewertung(bewertung);

        return kneipe;
    }


    /* * * * * * * * * * * * * * * * * * * *
     * VIEWPAGER
     * * * * * * * * * * * * * * * * * * * */

    /*
     * Creates new instances of the ViewPager and its fragments
     * gets called initially and everytime the Kneipen list is updated
     * */
    private void createViewPager() {
        fragmentManager = getSupportFragmentManager();
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(pager);

        tabsStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment fragment = ((MyPagerAdapter) pager.getAdapter()).getFragment(position);

                if (position == 2 && fragment != null) {
                    //fragment.onResume();
                    Log.v(TAG, "hier gehts ab");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    /* * * * * * * * * * * * * * * * * * * *
     * NAVIGATION
     * * * * * * * * * * * * * * * * * * * */

    class NavItem {
        String mTitle;
        String mSubtitle;
        int mIcon;

        public NavItem(String title, String subtitle, int icon) {
            mTitle = title;
            mSubtitle = subtitle;
            mIcon = icon;
        }
    }

    class DrawerListAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<NavItem> mNavItems;

        public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
            mContext = context;
            mNavItems = navItems;
        }

        @Override
        public int getCount() {
            return mNavItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mNavItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.drawer_item, null);
            }
            else {
                view = convertView;
            }

            TextView titleView = (TextView) view.findViewById(R.id.title);
            TextView subtitleView = (TextView) view.findViewById(R.id.subTitle);
            ImageView iconView = (ImageView) view.findViewById(R.id.icon);

            titleView.setText( mNavItems.get(position).mTitle );
            subtitleView.setText( mNavItems.get(position).mSubtitle );
            iconView.setImageResource(mNavItems.get(position).mIcon);

            return view;
        }
    }

    /*
     * Called when a particular item from the navigation drawer
     * is selected.
     * */
    private void selectItemFromDrawer(int position) {
        PreferencesFragment fragment = new PreferencesFragment();

        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.mainContent, fragment)
                .commit();

        mDrawerList.setItemChecked(position, true);
        setTitle(mNavItems.get(position).mTitle);

        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }


    /* * * * * * * * * * * * * * * * * * * *
     * OPTIONSMENU
     * * * * * * * * * * * * * * * * * * * */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle
        // If it returns true, then it has handled
        // the nav drawer indicator touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }*/

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
}
