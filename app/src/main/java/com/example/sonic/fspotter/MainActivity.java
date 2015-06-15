package com.example.sonic.fspotter;

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

    private ArrayList<Kneipe> mKneipen = null;
    private ArrayList<Kneipe> mKneipenFiltered = null;

    @InjectView(R.id.spinnerView) Spinner mSpinner;
    @InjectView(R.id.spinnerView2) Spinner mSpinner2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        fragmentManager = getSupportFragmentManager();
        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        //Bind the title indicator to the adapter
        TitlePageIndicator titleIndicator = (TitlePageIndicator)findViewById(R.id.indicator);
        titleIndicator.setViewPager(pager);

        setKneipenData();

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, String.format("value=%d", position));

                if (position != 0) {
                    loadItemsInSpinner2(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Filter
                if (position != 0) {
                    //updateKneipenData("spinner2", position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

                        if (kneipen != null) {
                            //updateDisplay(kneipen);
                        }

                        mKneipen = kneipen;
                        Log.v(TAG + " mKneipen", mKneipen.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*private void updateDisplay(final ArrayList<Kneipe> kneipen) {
        Log.i(TAG, kneipen.toString());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.setHasFixedSize(true); // Not always recommended, but in this case enhances performance

                mLayoutManager = new LinearLayoutManager(MainActivity.this);
                mRecyclerView.setLayoutManager(mLayoutManager);

                final Kneipenadapter mAdapter = new Kneipenadapter(kneipen, MainActivity.this);
                mRecyclerView.setAdapter(mAdapter);
            }
        });
    }*/

    private Kneipe makeKneipe(String name, String adresse, String typ, String bewertung) {
        Kneipe kneipe = new Kneipe();
        kneipe.setName(name);
        kneipe.setAdresse(adresse);
        kneipe.setTyp(typ);
        kneipe.setBewertung(bewertung);

        return kneipe;
    }

    public void loadItemsInSpinner2(int position) {
        ArrayList<String> list = new ArrayList<String>();

        switch (position) {
            case 0:
                Log.i(TAG, "Auswählen");
                list.add("Zuerst Kategorie wählen");
                break;
            case 1:
                Log.i(TAG, "Sterne ausgewählt");
                list.add("Filter auswählen");
                list.add("5");
                list.add("4");
                list.add("3");
                list.add("2");
                list.add("1");
                break;
            case 2:
                Log.i(TAG, "Entfernung ausgewählt");
                break;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner2.setAdapter(adapter);
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
