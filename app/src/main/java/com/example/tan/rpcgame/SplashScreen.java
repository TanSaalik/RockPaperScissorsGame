package com.example.tan.rpcgame;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Intent that'll start the MainActivity
                Intent mainActivity = new Intent(SplashScreen.this,
                        MainActivity.class);
                SplashScreen.this.startActivity(mainActivity);
                SplashScreen.this.finish();
            }

        }, SPLASH_DISPLAY_LENGTH);
    }
}
