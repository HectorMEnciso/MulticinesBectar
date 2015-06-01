package com.example.hector.multicinesbectar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

/**
 * Created by Hector on 19/05/2015.
 */
public class SplashScreen extends Activity {

    private static int SPLASH_TIME_OUT = 2000;// Splash screen timer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);

                finish();// close this activity
            }
        }, SPLASH_TIME_OUT);

        TextView title = (TextView) findViewById(R.id.titleBectar1);
        Typeface font = Typeface.createFromAsset(
                this.getAssets(),
                "fonts/Roboto-BlackItalic.ttf");
        title.setTypeface(font);

        TextView title2 = (TextView) findViewById(R.id.titleBectar2);
        Typeface font2 = Typeface.createFromAsset(
                this.getAssets(),
                "fonts/Roboto-BlackItalic.ttf");
        title2.setTypeface(font2);
    }

}