package com.lokeshkedia.e_wall;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class DemoFragmentStateAdapter extends FragmentStatePagerAdapter {
    public DemoFragmentStateAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                return FirstFragment.newInstance("FirstFragment, Instance 1");
            case 1:
                return SecondFragment.newInstance("SecondFragment, Instance 1");
            case 2:
                return ThirdFragment.newInstance("ThirdFragment, Instance 1");
            /*case 3:
                return PFFirst.newInstance("PFFirst, Instance 4");*/
            default:
                return SecondFragment.newInstance("SecondFragment, Default");
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
