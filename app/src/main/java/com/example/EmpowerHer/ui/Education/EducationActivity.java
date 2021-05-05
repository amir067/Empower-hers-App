package com.example.EmpowerHer.ui.Education;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.EmpowerHer.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class EducationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);
        ButterKnife.bind(this);



    }




    @OnClick(R.id.iv_back)
    public void onBackClick(){
        finish();
    }
}
