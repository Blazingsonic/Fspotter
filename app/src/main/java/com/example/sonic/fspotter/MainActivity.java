package com.example.sonic.fspotter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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


public class MainActivity extends FragmentActivity {
    
    public static final String TAG = MainActivity.class.getSimpleName();

    private String mCouchUrl = "https://gowdy.iriscouch.com/gowdy/_design/gowdy/_view/alleKneipen";
    public static FragmentManager fragmentManager;

    public static ArrayList<Kneipe> mKneipen = null;
    public static ArrayList<Kneipe> mKneipenFiltered = null;

    @InjectView(R.id.viewPager) ViewPager pager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        createViewPager();
        setKneipenData();
    }

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
                Fragment fragment = ((MyPagerAdapter)pager.getAdapter()).getFragment(position);

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

    private Kneipe makeKneipe(String name, String adresse, String typ, String bewertung) {
        Kneipe kneipe = new Kneipe();
        kneipe.setName(name);
        kneipe.setAdresse(adresse);
        kneipe.setTyp(typ);
        kneipe.setBewertung(bewertung);

        return kneipe;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
