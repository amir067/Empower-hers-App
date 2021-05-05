package com.example.EmpowerHer.ui.Complains;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.EmpowerHer.R;
import com.example.EmpowerHer.utils.Tools;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ComplainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);
        ButterKnife.bind(this);
        Tools.setSystemBarTransparent(this);
        Tools.setSystemBarLight(this);



    }

    @OnClick(R.id.iv_back)
    public void onBackClick(){
        finish();
    }
}