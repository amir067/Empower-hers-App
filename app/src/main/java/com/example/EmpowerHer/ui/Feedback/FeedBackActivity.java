package com.example.EmpowerHer.ui.Feedback;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.EmpowerHer.R;
import com.example.EmpowerHer.utils.Tools;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedBackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        Tools.setSystemBarTransparent(this);
        Tools.setSystemBarLight(this);



    }

    @OnClick(R.id.iv_back)
    public void onBackClick(){
        finish();
    }
}