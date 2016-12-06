package com.globe3.tno.g3_mobile.fragments;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.globe3.tno.g3_mobile.adapters.ProjectListAdapter;
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

public class LogTimeProjectFragment extends DialogFragment {
    Staff staff;

    LogTimeFragment log_time_fragment;
    LogTimeAutoFragment log_time_auto_fragment;
    LocationCheckFragment location_check_fragment;
    LocationCheckAutoFragment location_check_auto_fragment;
    LogTimeSummaryFragment log_time_summary_fragment;

    RecyclerView recycler_project_list;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    TextView tv_search_project;
    RelativeLayout rl_search_loader;
    ImageView iv_search_loader;
    RelativeLayout rl_no_record;

    ImageView iv_staff_photo;
    TextView tv_staff_id;
    TextView tv_staff_name;
    TextView tv_action_button;
    TextView tv_cancel;

    ArrayList<RowProject> project_list;

    String log_type;

    TimeLog time_log;

    SearchProject searchProject;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View logTimeProjectFragment = inflater.inflate(R.layout.fragment_log_time_project, viewGroup, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        tv_search_project = (TextView) logTimeProjectFragment.findViewById(R.id.tv_search_project);
        rl_search_loader = (RelativeLayout) logTimeProjectFragment.findViewById(R.id.rl_search_loader);
        iv_search_loader = (ImageView) logTimeProjectFragment.findViewById(R.id.iv_search_loader);
        rl_no_record = (RelativeLayout) logTimeProjectFragment.findViewById(R.id.rl_no_record);

        iv_search_loader.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.animate_rotate_clockwise));

        iv_staff_photo = (ImageView) logTimeProjectFragment.findViewById(R.id.iv_staff_photo);
        tv_staff_id = (TextView) logTimeProjectFragment.findViewById(R.id.tv_staff_id);
        tv_staff_name = (TextView) logTimeProjectFragment.findViewById(R.id.tv_staff_name);
        tv_action_button = (TextView) logTimeProjectFragment.findViewById(R.id.tv_action_button);
        tv_cancel = (TextView) logTimeProjectFragment.findViewById(R.id.tv_cancel);

        time_log = new TimeLog();
        time_log.setDate(Calendar.getInstance().getTime());
        time_log.setType(log_type);
        time_log.setStaff(staff);

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
            project_list.add(createRowProject(project));
        }

        recycler_project_list.setHasFixedSize(true);

        recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        recycler_project_list.setLayoutManager(recyclerViewLayoutManager);

        recyclerViewAdapter = new LogTimeProjectListAdapter(project_list, getActivity());
        recycler_project_list.setAdapter(recyclerViewAdapter);

        tv_search_project.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable searchTerm) {
                searchProject(searchTerm.toString());
            }
        });

        tv_action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSummary();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(log_time_fragment !=null){
                    log_time_fragment.startExtract();
                }
                if(log_time_auto_fragment !=null){
                    log_time_auto_fragment.startExtract();
                }
                if(location_check_auto_fragment !=null){
                    location_check_auto_fragment.startExtract();
                }
                if(location_check_fragment != null){
                    location_check_fragment.startExtract();
                }

                dismiss();
            }
        });
        return logTimeProjectFragment;
    }

    private RowProject createRowProject(final Project project){
        RowProject rowProject = new RowProject();
        rowProject.setProjectCode(project.getCode());
        rowProject.setProjectName(project.getDesc());
        rowProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_log.setProject(project);
                showSummary();
            }
        });

        return rowProject;
    }

    private void showSummary(){
        StaffFactory staffFactory = new StaffFactory(getActivity());

        DailyTime dailyTime = staffFactory.logTime(staff, time_log.getProject(), time_log.getType());

        LogItem logItem = new AuditFactory(getActivity()).Log(time_log.getType(), staff.getUniquenumPri());

        new TimeLogSingleUploadTask(staffFactory, dailyTime, logItem).execute();

        FragmentManager fragmentManager = getActivity().getFragmentManager();
        log_time_summary_fragment = new LogTimeSummaryFragment();
        log_time_summary_fragment.setCancelable(false);
        log_time_summary_fragment.setTimeLog(time_log);
        log_time_summary_fragment.setLogTimeAutoFragment(log_time_auto_fragment);
        log_time_summary_fragment.setLogTimeFragment(log_time_fragment);
        log_time_summary_fragment.setLocationCheckAutoFragment(location_check_auto_fragment);
        log_time_summary_fragment.setLocationCheckFragment(location_check_fragment);

        dismiss();
        log_time_summary_fragment.show(fragmentManager, getString(R.string.label_log_time_summary));
    }

    public void setLog_type(String log_type) {
        this.log_type = log_type;
    }
    public void setStaff(Staff staff) {
        this.staff = staff;
    }
    public void setLogTimeFragment(LogTimeFragment logTimeFragment) {
        this.log_time_fragment = logTimeFragment;
    }
    public void setLogTimeAutoFragment(LogTimeAutoFragment logTimeAutoFragment) {
        this.log_time_auto_fragment = logTimeAutoFragment;
    }
    public void setLocationCheckAutoFragment(LocationCheckAutoFragment locationCheckAutoFragment) {
        this.location_check_auto_fragment = locationCheckAutoFragment;
    }
    public void setLocationCheckFragment(LocationCheckFragment locationCheckFragment){
        location_check_fragment = locationCheckFragment;
    }

    public void searchProject(String searchTerm) {
        if(searchProject != null){
            searchProject.cancel(true);
        }
        searchProject = new SearchProject(searchTerm);
        searchProject.execute();
    }

    public class SearchProject extends AsyncTask<Void, Void, Void>
    {
        String searchTerm;

        public SearchProject(String searchTerm){
            this.searchTerm = searchTerm;
            project_list.clear();
        }

        @Override
        protected  void onPreExecute()
        {
            recycler_project_list.setVisibility(View.GONE);
            rl_search_loader.setVisibility(View.VISIBLE);
            rl_no_record.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... param) {
            for(Project project : (searchTerm.equals("")?new ProjectFactory(getActivity()).getActiveProjects():new ProjectFactory(getActivity()).searchProject(searchTerm))){
                project_list.add(createRowProject(project));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            recycler_project_list.setHasFixedSize(true);

            recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
            recycler_project_list.setLayoutManager(recyclerViewLayoutManager);

            recyclerViewAdapter = new ProjectListAdapter(project_list, getActivity());
            recycler_project_list.setAdapter(recyclerViewAdapter);

            recycler_project_list.setVisibility(project_list.size()==0?View.GONE:View.VISIBLE);
            rl_search_loader.setVisibility(View.GONE);
            rl_no_record.setVisibility(project_list.size()==0?View.VISIBLE:View.GONE);
        }
    }
}
