package com.globe3.tno.g3_mobile.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.globe3.tno.g3_mobile.fragments.PhotosProjectFragment;
import com.globe3.tno.g3_mobile.fragments.PhotosStaffFragment;

public class PhotosTabAdapter extends FragmentStatePagerAdapter {
    SparseArray<Fragment> registered_fragments = new SparseArray<Fragment>();

    public PhotosTabAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
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

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registered_fragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registered_fragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registered_fragments.get(position);
    }
}