package com.example.suriya.spotdrivers.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.introslide.prefManager.orbitowelcome.OrbitoWelcome;

public class SplashScreen extends AppCompatActivity implements Animation.AnimationListener{
    private Animation animFadein, blink;
    private ImageView imageView, imageView1;
    private static int SPLASH_TIME_OUT = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        blink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView1 = (ImageView) findViewById(R.id.imageView2);
        imageView1.setAnimation(blink);
        imageView.setAnimation(animFadein);
        animFadein.setAnimationListener(this);
        blink.setAnimationListener(this);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                startActivity(new Intent(getApplicationContext(), OrbitoWelcome.class));
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
