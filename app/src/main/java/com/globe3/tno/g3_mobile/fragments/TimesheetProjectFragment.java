package com.globe3.tno.g3_mobile.fragments;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globe3.tno.g3_mobile.app_objects.Project;
import com.globe3.tno.g3_mobile.view_objects.RowProject;
import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.adapters.ProjectListAdapter;

import java.util.ArrayList;

public class TimesheetProjectFragment extends Fragment {
    RecyclerView recycler_project_list;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    LogTimeStaffFragment logTimeStaffFragment;

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
            final int n = i;
            RowProject rowProject = new RowProject();
            rowProject.setProjectCode("PRJ"+String.valueOf(i));
            rowProject.setProjectName("Project Desc");
            rowProject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getActivity().getFragmentManager();
                    Bundle projectBundle = new Bundle();

                    Project project = new Project();
                    project.setCode("PRJ"+String.valueOf(n));
                    project.setDesc("Project Desc");

                    projectBundle.putSerializable("entproject", project);

                    logTimeStaffFragment = new LogTimeStaffFragment();
                    logTimeStaffFragment.setCancelable(false);
                    logTimeStaffFragment.setArguments(projectBundle);
                    logTimeStaffFragment.show(fragmentManager, getString(R.string.label_log_time_staff));
                }
            });
            project_list.add(rowProject);
        }

        recycler_project_list.setHasFixedSize(true);

        recyclerViewLayoutManager = new LinearLayoutManager(viewGroup.getContext());
        recycler_project_list.setLayoutManager(recyclerViewLayoutManager);

        recyclerViewAdapter = new ProjectListAdapter(project_list, viewGroup.getContext());
        recycler_project_list.setAdapter(recyclerViewAdapter);

        return projectFragment;
    }
}
