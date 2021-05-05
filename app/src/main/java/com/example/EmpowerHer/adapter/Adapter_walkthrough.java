package com.example.EmpowerHer.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.EmpowerHer.fragment.Fragment_walkthroughone;
import com.example.EmpowerHer.fragment.Fragment_walkthroughthree;
import com.example.EmpowerHer.fragment.Fragment_walkthroughtwo;

public class Adapter_walkthrough extends FragmentStatePagerAdapter {

    public Adapter_walkthrough(FragmentManager fm) {
        super(fm);

    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Fragment_walkthroughthree tab3 = new Fragment_walkthroughthree();
                return tab3;


            case 1:
                Fragment_walkthroughtwo tab2 = new Fragment_walkthroughtwo();
                return tab2;


            case 2:
                Fragment_walkthroughone tab1 = new Fragment_walkthroughone();
                return tab1;


            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
