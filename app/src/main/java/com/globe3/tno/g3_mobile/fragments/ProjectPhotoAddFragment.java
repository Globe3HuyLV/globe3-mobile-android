package com.globe3.tno.g3_mobile.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.globe3.tno.g3_mobile.R;

public class ProjectPhotoAddFragment extends DialogFragment {
    Context parentContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View projectPhotoAddFragment = inflater.inflate(R.layout.fragment_project_photo_add, viewGroup, false);
        parentContext = projectPhotoAddFragment.getContext();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return projectPhotoAddFragment;
    }

    public void finishProjectPhotoAdd(){
        dismiss();
    }
}
