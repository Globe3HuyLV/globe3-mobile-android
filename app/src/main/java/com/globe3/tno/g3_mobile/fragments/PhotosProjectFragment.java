package com.globe3.tno.g3_mobile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globe3.tno.g3_mobile.activities.ProjectPhotoActivity;
import com.globe3.tno.g3_mobile.view_objects.RowProject;
import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.adapters.PhotoProjectListAdapter;

import java.util.ArrayList;

public class PhotosProjectFragment extends Fragment {
    RecyclerView recycler_project_list;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View projectFragment = inflater.inflate(R.layout.fragment_project_list, viewGroup, false);

        recycler_project_list = (RecyclerView) projectFragment.findViewById(R.id.recycler_project_list);

        ArrayList<RowProject> project_list = new ArrayList<>();
        for(int i=1001;i<=1027;i++){
            RowProject rowProject = new RowProject();
            rowProject.setProjectCode("PRJ"+String.valueOf(i));
            rowProject.setProjectName("Project Desc");
            rowProject.setProjectPhotosCount(1027-i);
            rowProject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), ProjectPhotoActivity.class));
                }
            });
            project_list.add(rowProject);
        }

        recycler_project_list.setHasFixedSize(true);

        recyclerViewLayoutManager = new LinearLayoutManager(viewGroup.getContext());
        recycler_project_list.setLayoutManager(recyclerViewLayoutManager);

        recyclerViewAdapter = new PhotoProjectListAdapter(project_list, viewGroup.getContext());
        recycler_project_list.setAdapter(recyclerViewAdapter);

        return projectFragment;
    }
}
