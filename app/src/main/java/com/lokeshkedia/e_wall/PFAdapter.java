package com.lokeshkedia.e_wall;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PFAdapter extends FragmentStatePagerAdapter {
    public PFAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            /*case 0:
                return PFFirst.newInstance("PFFirst, Instance 1");*/
            case 0:
                return PFSecond.newInstance("PFSecond, Instance 2");
            case 1:
                return PFThird.newInstance("PFThird, Instance 3");
            case 2:
                return PFFourth.newInstance("PFFourth, Default");
            default:
                return PFFirst.newInstance("PFFirst, Instance 1");
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
