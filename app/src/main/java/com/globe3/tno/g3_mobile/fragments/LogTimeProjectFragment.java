package com.globe3.tno.g3_mobile.fragments;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.globe3.tno.g3_mobile.app_objects.Project;
import com.globe3.tno.g3_mobile.view_objects.RowProject;
import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.adapters.LogTimeProjectListAdapter;
import com.globe3.tno.g3_mobile.app_objects.TimeLog;
import com.globe3.tno.g3_mobile.app_objects.Staff;

import java.util.ArrayList;
import java.util.Calendar;

public class LogTimeProjectFragment extends DialogFragment {
    Context parentContext;

    Staff staff;

    RecyclerView recycler_project_list;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    TextView tv_staff_id;
    TextView tv_staff_name;
    TextView tv_cancel;

    LogTimeSummaryFragment logTimeSummaryFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View logTimeAutoFragment = inflater.inflate(R.layout.fragment_log_time_project, viewGroup, false);
        parentContext = logTimeAutoFragment.getContext();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        if(getArguments() != null && getArguments().containsKey("staff")){
            staff = (Staff) getArguments().getSerializable("staff");

            tv_staff_id = (TextView) logTimeAutoFragment.findViewById(R.id.tv_staff_id);
            tv_staff_id.setText(staff.getStaff_num());

            tv_staff_name = (TextView) logTimeAutoFragment.findViewById(R.id.tv_staff_name);
            tv_staff_name.setText(staff.getStaff_desc());
        }

        recycler_project_list = (RecyclerView) logTimeAutoFragment.findViewById(R.id.recycler_project_list);
        ArrayList<RowProject> project_list = new ArrayList<>();
        for(int i=1001;i<=1027;i++){
            final int n = i;
            RowProject rowProject = new RowProject();
            rowProject.setProjectCode("PRJ"+String.valueOf(i));
            rowProject.setProjectName("Project Desc");
            rowProject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Project project = new Project();
                    project.setCode("PRJ"+String.valueOf(n));
                    project.setDesc("Project Desc");

                    Bundle logTimeBundle = new Bundle();
                    TimeLog timeLog = new TimeLog();
                    timeLog.setDate(Calendar.getInstance().getTime());
                    timeLog.setType("Time In");
                    timeLog.setStaff(staff);
                    timeLog.setProject(project);

                    logTimeBundle.putSerializable("time_log", timeLog);

                    FragmentManager fragmentManager = getActivity().getFragmentManager();
                    logTimeSummaryFragment = new LogTimeSummaryFragment();
                    logTimeSummaryFragment.setCancelable(false);
                    logTimeSummaryFragment.setArguments(logTimeBundle);

                    dismiss();
                    logTimeSummaryFragment.show(fragmentManager, getString(R.string.label_log_time_summary));
                }
            });
            project_list.add(rowProject);
        }

        recycler_project_list.setHasFixedSize(true);

        recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        recycler_project_list.setLayoutManager(recyclerViewLayoutManager);

        recyclerViewAdapter = new LogTimeProjectListAdapter(project_list, getActivity());
        recycler_project_list.setAdapter(recyclerViewAdapter);

        tv_cancel = (TextView) logTimeAutoFragment.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return logTimeAutoFragment;
    }
}
