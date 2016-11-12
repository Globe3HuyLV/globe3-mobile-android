package com.globe3.tno.g3_mobile.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.globe3.tno.g3_mobile.fragments.PhotosProjectFragment;
import com.globe3.tno.g3_mobile.fragments.PhotosStaffFragment;

public class PhotosTabAdapter extends FragmentStatePagerAdapter {

    public PhotosTabAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new PhotosProjectFragment();
            case 1:
                return new PhotosStaffFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}