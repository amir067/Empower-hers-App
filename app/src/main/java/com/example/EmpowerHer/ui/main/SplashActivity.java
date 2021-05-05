package com.example.EmpowerHer.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.EmpowerHer.R;
import com.example.EmpowerHer.ui.auth.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);





        //hold screen for 3 to 5 seconds and move to main screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //go to next activity
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, 3000); // for 3 second
    }



}
