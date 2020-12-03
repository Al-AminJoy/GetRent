package com.getrentbd.getrent.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.getrentbd.getrent.R;
import com.getrentbd.getrent.helper.Common;

public class SplashScreen extends AppCompatActivity {
    private ProgressBar progressBar;
    private String number;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //Finding Id
        progressBar = findViewById(R.id.progressid);
        getSharedPrefValue();
        //Using Thread
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                dowork();
            }
        });
        thread.start();

    }
    private void getSharedPrefValue() {
        SharedPreferences sharedPreferences = getSharedPreferences(Common.AppPra, Context.MODE_PRIVATE);
        number = sharedPreferences.getString(Common.SpNumber, "");
        password = sharedPreferences.getString(Common.SpPass, "");
    }

    public void dowork() {

        for (int progress = 20; progress <= 100; progress = progress + 20) {
            try {
                Thread.sleep(200);
                progressBar.setProgress(progress);
                if (progress == 100) {
                    if (!number.isEmpty()&& !password.isEmpty()){
                        startActivity(new Intent(SplashScreen.this, HomePage.class));
                        finish();
                    }else {
                        startActivity(new Intent(SplashScreen.this, LogIn.class));
                        finish();
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
