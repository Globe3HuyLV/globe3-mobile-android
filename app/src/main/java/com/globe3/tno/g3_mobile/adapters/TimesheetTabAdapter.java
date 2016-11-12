package com.globe3.tno.g3_mobile.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.globe3.tno.g3_mobile.fragments.TimesheetProjectFragment;
import com.globe3.tno.g3_mobile.fragments.TimesheetStaffFragment;

public class TimesheetTabAdapter extends FragmentStatePagerAdapter {

    public TimesheetTabAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new TimesheetStaffFragment();
            case 1:
                return new TimesheetProjectFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}