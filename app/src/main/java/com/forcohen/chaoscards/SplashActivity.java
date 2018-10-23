package com.forcohen.chaoscards;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Typeface gameFont = Typeface.createFromAsset(getAssets(), "fonts/gothic.ttf");

        TextView gameTitle = (TextView) findViewById(R.id.game_title);

//        if (gameFont != null)
//            gameTitle.setTypeface(gameFont);

        try {
            Log.i("assets", "showing");
            for (String f : getAssets().list("")){
                Log.i("assets", f);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

    }

}
