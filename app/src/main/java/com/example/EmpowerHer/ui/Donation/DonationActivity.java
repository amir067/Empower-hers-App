package com.example.EmpowerHer.ui.Donation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.EmpowerHer.R;
import com.example.EmpowerHer.utils.Tools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DonationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);
        ButterKnife.bind(this);
        Tools.setSystemBarTransparent(this);
        Tools.setSystemBarLight(this);



    }

    @OnClick(R.id.iv_back)
    public void onBackClick(){
        finish();
    }
}