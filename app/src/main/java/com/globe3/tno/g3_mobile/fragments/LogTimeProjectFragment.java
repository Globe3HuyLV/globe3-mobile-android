package com.globe3.tno.g3_mobile.fragments;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.globe3.tno.g3_mobile.app_objects.DailyTime;
import com.globe3.tno.g3_mobile.app_objects.LogItem;
import com.globe3.tno.g3_mobile.app_objects.Project;
import com.globe3.tno.g3_mobile.app_objects.factory.AuditFactory;
import com.globe3.tno.g3_mobile.app_objects.factory.ProjectFactory;
import com.globe3.tno.g3_mobile.app_objects.factory.StaffFactory;
import com.globe3.tno.g3_mobile.async.TimeLogSingleUploadTask;
import com.globe3.tno.g3_mobile.view_objects.RowProject;
import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.adapters.LogTimeProjectListAdapter;
import com.globe3.tno.g3_mobile.app_objects.TimeLog;
import com.globe3.tno.g3_mobile.app_objects.Staff;

import java.util.ArrayList;
import java.util.Calendar;

import static com.globe3.tno.g3_mobile.constants.App.APP_NAME;

public class LogTimeProjectFragment extends DialogFragment {
    Staff staff;

    LogTimeFragment logTimeFragment;
    LogTimeAutoFragment logTimeAutoFragment;

    RecyclerView recycler_project_list;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    ImageView iv_staff_photo;
    TextView tv_staff_id;
    TextView tv_staff_name;
    TextView tv_action_button;
    TextView tv_cancel;

    LogTimeSummaryFragment logTimeSummaryFragment;

    ArrayList<RowProject> project_list;

    TimeLog timeLog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View logTimeProjectFragment = inflater.inflate(R.layout.fragment_log_time_project, viewGroup, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        iv_staff_photo = (ImageView) logTimeProjectFragment.findViewById(R.id.iv_staff_photo);
        tv_staff_id = (TextView) logTimeProjectFragment.findViewById(R.id.tv_staff_id);
        tv_staff_name = (TextView) logTimeProjectFragment.findViewById(R.id.tv_staff_name);
        tv_action_button = (TextView) logTimeProjectFragment.findViewById(R.id.tv_action_button);
        tv_cancel = (TextView) logTimeProjectFragment.findViewById(R.id.tv_cancel);

        timeLog = new TimeLog();
        timeLog.setDate(Calendar.getInstance().getTime());
        timeLog.setType(logTimeFragment!=null?logTimeFragment.getLog_type() : logTimeAutoFragment.getLog_type());
        timeLog.setStaff(staff);

        if(staff!=null){
            if(staff.getPhoto1()!=null){
                Bitmap staffPhoto = BitmapFactory.decodeByteArray(staff.getPhoto1(), 0, staff.getPhoto1().length);

                int newSize = staffPhoto.getWidth() < staffPhoto.getHeight() ? staffPhoto.getWidth() : staffPhoto.getHeight();

                iv_staff_photo.setImageBitmap(Bitmap.createBitmap(staffPhoto, 0, 0, newSize, newSize));
            }else{
                iv_staff_photo.setImageResource(R.drawable.ic_person_black_48dp);
            }
            tv_staff_id.setText(staff.getStaff_num());
            tv_staff_name.setText(staff.getStaff_desc());
        }

        recycler_project_list = (RecyclerView) logTimeProjectFragment.findViewById(R.id.recycler_project_list);

        project_list = new ArrayList<>();
        for(final Project project : new ProjectFactory(getActivity()).getActiveProjects()){
            RowProject rowProject = new RowProject();
            rowProject.setProjectCode(project.getCode());
            rowProject.setProjectName(project.getDesc());
            rowProject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timeLog.setProject(project);
                    showSummary();
                }
            });
            project_list.add(rowProject);
        }

        recycler_project_list.setHasFixedSize(true);

        recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        recycler_project_list.setLayoutManager(recyclerViewLayoutManager);

        recyclerViewAdapter = new LogTimeProjectListAdapter(project_list, getActivity());
        recycler_project_list.setAdapter(recyclerViewAdapter);

        tv_action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSummary();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(logTimeFragment!=null){
                    logTimeFragment.startExtract();
                }
                if(logTimeAutoFragment!=null){
                    logTimeAutoFragment.startExtract();
                }
                dismiss();
            }
        });
        return logTimeProjectFragment;
    }

    private void showSummary(){
        StaffFactory staffFactory = new StaffFactory(getActivity());

        DailyTime dailyTime = staffFactory.logTime(staff, timeLog.getProject(), timeLog.getType());

        LogItem logItem = new AuditFactory(getActivity()).Log(timeLog.getType());

        new TimeLogSingleUploadTask(staffFactory, dailyTime, logItem).execute();

        FragmentManager fragmentManager = getActivity().getFragmentManager();
        logTimeSummaryFragment = new LogTimeSummaryFragment();
        logTimeSummaryFragment.setCancelable(false);
        logTimeSummaryFragment.setTimeLog(timeLog);
        logTimeSummaryFragment.setLogTimeAutoFragment(logTimeAutoFragment);
        logTimeSummaryFragment.setLogTimeFragment(logTimeFragment);

        dismiss();
        logTimeSummaryFragment.show(fragmentManager, getString(R.string.label_log_time_summary));
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public void setLogTimeFragment(LogTimeFragment logTimeFragment) {
        this.logTimeFragment = logTimeFragment;
    }
    public void setLogTimeAutoFragment(LogTimeAutoFragment logTimeAutoFragment) {
        this.logTimeAutoFragment = logTimeAutoFragment;
    }
}
