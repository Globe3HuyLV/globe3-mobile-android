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
import com.globe3.tno.g3_mobile.app_objects.Project;

import java.util.ArrayList;

public class ProjectPhotoAddFragment extends DialogFragment {
    Context parent_context;

    Project project;

    TextView tv_project_code;
    TextView tv_project_desc;
    TextView tv_selected_photos_count;
    EditText et_ref_num;
    EditText et_remarks;
    TextView tv_upload;
    TextView tv_cancel;

    private ArrayList<String> image_urls;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View projectPhotoAddFragment = inflater.inflate(R.layout.fragment_project_photo_add, viewGroup, false);
        parent_context = projectPhotoAddFragment.getContext();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        tv_project_code = (TextView) projectPhotoAddFragment.findViewById(R.id.tv_project_code);
        tv_project_desc = (TextView) projectPhotoAddFragment.findViewById(R.id.tv_project_desc);
        tv_selected_photos_count = (TextView) projectPhotoAddFragment.findViewById(R.id.tv_selected_photos_count);
        et_ref_num = (EditText) projectPhotoAddFragment.findViewById(R.id.et_ref_num);
        et_remarks = (EditText) projectPhotoAddFragment.findViewById(R.id.et_remarks);
        tv_upload = (TextView) projectPhotoAddFragment.findViewById(R.id.tv_upload);
        tv_cancel = (TextView) projectPhotoAddFragment.findViewById(R.id.tv_cancel);

        tv_project_code.setText(project.getCode());
        tv_project_desc.setText(project.getDesc());

        tv_selected_photos_count.setText(String.format(getString(R.string.msg_num_photos_selected), String.valueOf(image_urls.size())));

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return projectPhotoAddFragment;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setImageUrls(ArrayList<String> image_urls) {
        this.image_urls = image_urls;
    }
}
