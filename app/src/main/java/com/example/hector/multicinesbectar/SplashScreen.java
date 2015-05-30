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
    private DBController controller;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        controller = new DBController(this);
       // controller.deleteAll();
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

        TextView title=(TextView)findViewById(R.id.titleBectar1);
        Typeface font = Typeface.createFromAsset(
                this.getAssets(),
                "fonts/Roboto-BlackItalic.ttf");
        title .setTypeface(font);

        TextView title2=(TextView)findViewById(R.id.titleBectar2);
        Typeface font2 = Typeface.createFromAsset(
                this.getAssets(),
                "fonts/Roboto-BlackItalic.ttf");
        title2.setTypeface(font2);
    }

}