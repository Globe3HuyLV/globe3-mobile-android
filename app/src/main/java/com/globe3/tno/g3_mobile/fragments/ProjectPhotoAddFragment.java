package com.globe3.tno.g3_mobile.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.globe3.tno.g3_mobile.R;

public class ProjectPhotoAddFragment extends DialogFragment {
    Context parentContext;

    TextView tv_project_code;
    TextView tv_project_desc;
    TextView tv_selected_photos_count;
    EditText et_ref_num;
    EditText et_remarks;
    TextView tv_select_photos;
    TextView tv_upload;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View projectPhotoAddFragment = inflater.inflate(R.layout.fragment_project_photo_add, viewGroup, false);
        parentContext = projectPhotoAddFragment.getContext();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        tv_project_code = (TextView) projectPhotoAddFragment.findViewById(R.id.tv_project_code);
        tv_project_desc = (TextView) projectPhotoAddFragment.findViewById(R.id.tv_project_desc);
        tv_selected_photos_count = (TextView) projectPhotoAddFragment.findViewById(R.id.tv_selected_photos_count);
        et_ref_num = (EditText) projectPhotoAddFragment.findViewById(R.id.et_ref_num);
        et_remarks = (EditText) projectPhotoAddFragment.findViewById(R.id.et_remarks);
        tv_select_photos = (TextView) projectPhotoAddFragment.findViewById(R.id.tv_select_photos);
        tv_upload = (TextView) projectPhotoAddFragment.findViewById(R.id.tv_upload);

        return projectPhotoAddFragment;
    }

    public void finishProjectPhotoAdd(){
        dismiss();
    }

    public void showPhotosCount(int photosCount){
        if(photosCount > 0){
            tv_selected_photos_count.setText(String.format(getString(R.string.msg_num_photos_selected), String.valueOf(photosCount)));
            tv_selected_photos_count.setVisibility(View.VISIBLE);
            tv_select_photos.setVisibility(View.GONE);
            tv_upload.setVisibility(View.VISIBLE);
        }else{
            tv_selected_photos_count.setVisibility(View.GONE);
            tv_select_photos.setVisibility(View.VISIBLE);
            tv_upload.setVisibility(View.GONE);
        }
    }
}
