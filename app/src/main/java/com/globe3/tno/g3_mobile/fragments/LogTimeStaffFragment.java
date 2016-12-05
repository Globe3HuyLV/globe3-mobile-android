package com.globe3.tno.g3_mobile.fragments;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.globe3.tno.g3_mobile.adapters.LogTimeStaffListAdapter;
import com.globe3.tno.g3_mobile.adapters.TimesheetStaffListAdapter;
import com.globe3.tno.g3_mobile.app_objects.Project;
import com.globe3.tno.g3_mobile.app_objects.factory.StaffFactory;
import com.globe3.tno.g3_mobile.view_objects.RowStaff;
import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.app_objects.Staff;
import com.neurotec.biometrics.client.NBiometricClient;

import java.util.ArrayList;

public class LogTimeStaffFragment extends DialogFragment {
    Context parentContext;

    Project project;

    NBiometricClient biometric_client;

    LinearLayout ll_main_container;

    RecyclerView recycler_staff_list;
    RecyclerView.Adapter recycler_view_adapter;
    RecyclerView.LayoutManager recycler_view_layout_manager;

    TextView tv_search_staff;
    RelativeLayout rl_search_loader;
    ImageView iv_search_loader;
    RelativeLayout rl_no_record;

    TextView tv_project_code;
    TextView tv_project_desc;
    TextView tv_auto_screening;
    TextView tv_cancel;

    LogTimeAutoFragment log_time_auto_fragment;
    LogTimeFragment log_time_fragment;

    ArrayList<RowStaff> staff_list;

    SearchStaff search_staff;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View logTimeStaffFragment = inflater.inflate(R.layout.fragment_log_time_staff, viewGroup, false);
        parentContext = logTimeStaffFragment.getContext();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        ll_main_container = (LinearLayout) logTimeStaffFragment.findViewById(R.id.ll_main_container);

        tv_search_staff = (TextView) logTimeStaffFragment.findViewById(R.id.tv_search_staff);
        rl_search_loader = (RelativeLayout) logTimeStaffFragment.findViewById(R.id.rl_search_loader);
        iv_search_loader = (ImageView) logTimeStaffFragment.findViewById(R.id.iv_search_loader);
        rl_no_record = (RelativeLayout) logTimeStaffFragment.findViewById(R.id.rl_no_record);

        tv_project_code = (TextView) logTimeStaffFragment.findViewById(R.id.tv_project_code);
        tv_project_desc = (TextView) logTimeStaffFragment.findViewById(R.id.tv_project_desc);
        recycler_staff_list = (RecyclerView) logTimeStaffFragment.findViewById(R.id.recycler_staff_list);

        if(project != null){
            tv_project_code.setText(project.getCode());
            tv_project_desc.setText(project.getDesc());
        }


        staff_list = new ArrayList<>();
        for(Staff staff : new StaffFactory(getActivity()).getActiveStaffs()){
            staff_list.add(createRowStaff(staff));
        }

        recycler_staff_list.setHasFixedSize(true);

        recycler_view_layout_manager = new LinearLayoutManager(getActivity());
        recycler_staff_list.setLayoutManager(recycler_view_layout_manager);

        recycler_view_adapter = new LogTimeStaffListAdapter(staff_list, getActivity());
        recycler_staff_list.setAdapter(recycler_view_adapter);

        tv_search_staff.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable searchTerm) {
                searchStaff(searchTerm.toString());
            }
        });

        tv_auto_screening = (TextView) logTimeStaffFragment.findViewById(R.id.tv_auto_screening);
        tv_auto_screening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_main_container.setVisibility(View.GONE);
                biometric_client = new NBiometricClient();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                log_time_auto_fragment = new LogTimeAutoFragment();
                log_time_auto_fragment.setCancelable(false);
                log_time_auto_fragment.setProject(project);
                log_time_auto_fragment.setLogTimeStaffFragment(LogTimeStaffFragment.this);
                log_time_auto_fragment.setmBiometricClient(biometric_client);
                log_time_auto_fragment.show(fragmentManager, getString(R.string.label_log_time_auto));
            }
        });

        tv_cancel = (TextView) logTimeStaffFragment.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return logTimeStaffFragment;
    }

    private RowStaff createRowStaff(final Staff staff){
        RowStaff rowStaff = new RowStaff();
        rowStaff.setStaffCode(staff.getStaff_num());
        rowStaff.setStaffName(staff.getStaff_desc());

        int staff_finger_count = 0;
        staff_finger_count += (staff.getFingerprint_image1()==null?0:1);
        staff_finger_count += (staff.getFingerprint_image2()==null?0:1);
        staff_finger_count += (staff.getFingerprint_image3()==null?0:1);
        staff_finger_count += (staff.getFingerprint_image4()==null?0:1);
        staff_finger_count += (staff.getFingerprint_image5()==null?0:1);

        rowStaff.setStaffFingerCount(staff_finger_count);

        if(staff.getPhoto1()!=null){
            Bitmap staffPhoto = BitmapFactory.decodeByteArray(staff.getPhoto1(), 0, staff.getPhoto1().length);

            int newSize = staffPhoto.getWidth() < staffPhoto.getHeight() ? staffPhoto.getWidth() : staffPhoto.getHeight();

            rowStaff.setStaffPhoto(Bitmap.createBitmap(staffPhoto, 0, 0, newSize, newSize));
        }else{
            rowStaff.setStaffPhoto(null);
        }

        rowStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_main_container.setVisibility(View.GONE);
                biometric_client = new NBiometricClient();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                log_time_fragment = new LogTimeFragment();
                log_time_fragment.setCancelable(false);
                log_time_fragment.setStaff(staff);
                log_time_fragment.setProject(project);
                log_time_fragment.setLogTimeStaffFragment(LogTimeStaffFragment.this);
                log_time_fragment.setmBiometricClient(biometric_client);
                log_time_fragment.show(fragmentManager, getString(R.string.label_log_time));
            }
        });

        return rowStaff;
    }

    public void resume(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ll_main_container.setVisibility(View.VISIBLE);
            }
        });
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void searchStaff(String searchTerm) {
        if(search_staff != null){
            search_staff.cancel(true);
        }

        search_staff = new SearchStaff(searchTerm);

        search_staff.execute();
    }

    public class SearchStaff extends AsyncTask<Void, Void, Void>
    {
        String searchTerm;

        public SearchStaff(String searchTerm){
            this.searchTerm = searchTerm;
            staff_list.clear();
        }

        @Override
        protected  void onPreExecute()
        {
            recycler_staff_list.setVisibility(View.GONE);
            tv_auto_screening.setClickable(false);
            rl_search_loader.setVisibility(View.VISIBLE);
            rl_no_record.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... param) {
            for(Staff staff : (searchTerm.equals("")?new StaffFactory(getActivity()).getActiveStaffs():new StaffFactory(getActivity()).searchStaffs(searchTerm))){
                staff_list.add(createRowStaff(staff));
            }
            staff_list.get(staff_list.size()-1).setDisplayBottomSpacer(true);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            recycler_staff_list.setHasFixedSize(true);

            recycler_view_layout_manager = new LinearLayoutManager(getActivity());
            recycler_staff_list.setLayoutManager(recycler_view_layout_manager);

            recycler_view_adapter = new TimesheetStaffListAdapter(staff_list, getActivity());
            recycler_staff_list.setAdapter(recycler_view_adapter);

            recycler_staff_list.setVisibility(staff_list.size()==0?View.GONE:View.VISIBLE);
            tv_auto_screening.setClickable(true);
            rl_search_loader.setVisibility(View.GONE);
            rl_no_record.setVisibility(staff_list.size()==0?View.VISIBLE:View.GONE);
        }
    }
}
