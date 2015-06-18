package com.example.sonic.fspotter.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import com.example.sonic.fspotter.R;
import com.example.sonic.fspotter.extras.Blur;
import com.example.sonic.fspotter.extras.GifDataDownloader;
import com.felipecsl.gifimageview.library.GifImageView;
import com.koushikdutta.ion.Ion;

import java.io.IOException;
import java.io.InputStream;

public class AcivitySplashScreen extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    private GifImageView gifImageView;
    private Button btnToggle;
    private Button btnBlur;

    private boolean shouldBlur = false;
    Blur blur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_splash_screen);

        InputStream stream = null;
        try {
            stream = getAssets().open("loading_gif_400x400_final.gif");
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageView gif = (ImageView) findViewById(R.id.imageView);
        Ion.with(gif).load("android.resource://" + getPackageName() + "/" + R.drawable.loading_gif_400x400_final);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(AcivitySplashScreen.this, ActivityMain.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_acivity_splash_screen, menu);
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
