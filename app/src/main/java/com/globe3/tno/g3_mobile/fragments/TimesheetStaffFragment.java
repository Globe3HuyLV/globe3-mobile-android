package com.globe3.tno.g3_mobile.fragments;

import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.globe3.tno.g3_mobile.adapters.TimesheetStaffListAdapter;
import com.globe3.tno.g3_mobile.app_objects.factory.StaffFactory;
import com.globe3.tno.g3_mobile.view_objects.RowStaff;
import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.app_objects.Staff;
import com.neurotec.biometrics.client.NBiometricClient;

import java.util.ArrayList;

import static com.globe3.tno.g3_mobile.globals.Globals.ACTIVE_FEATURE_TIMESHEET_SALES_ORDER;

public class TimesheetStaffFragment extends Fragment {
    StaffFactory staff_factory;

    NBiometricClient biometric_client;

    LogTimeAutoFragment log_time_auto_fragment;
    LogTimeFragment log_time_fragment;

    FloatingActionButton fab_auto_screening;
    View v_spacer;
    RecyclerView recycler_staff_list;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    RelativeLayout rl_search_loader;
    ImageView iv_search_loader;
    RelativeLayout rl_no_record;

    ArrayList<RowStaff> staff_list;

    SearchStaff search_staff;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        staff_factory = new StaffFactory(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View staffFragment = inflater.inflate(R.layout.fragment_timesheet_staff_list, viewGroup, false);

        fab_auto_screening = (FloatingActionButton) staffFragment.findViewById(R.id.fab_auto_screening);
        v_spacer = staffFragment.findViewById(R.id.v_spacer);
        recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        recycler_staff_list = (RecyclerView) staffFragment.findViewById(R.id.recycler_staff_list);
        rl_search_loader = (RelativeLayout) staffFragment.findViewById(R.id.rl_search_loader);
        iv_search_loader = (ImageView) staffFragment.findViewById(R.id.iv_search_loader);
        rl_no_record = (RelativeLayout) staffFragment.findViewById(R.id.rl_no_record);

        v_spacer.setVisibility(ACTIVE_FEATURE_TIMESHEET_SALES_ORDER?View.GONE:View.VISIBLE);

        iv_search_loader.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.animate_rotate_clockwise));

        fab_auto_screening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometric_client = new NBiometricClient();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                log_time_auto_fragment = new LogTimeAutoFragment();
                log_time_auto_fragment.setCancelable(false);
                log_time_auto_fragment.setmBiometricClient(biometric_client);
                log_time_auto_fragment.show(fragmentManager, getString(R.string.label_log_time_auto));
            }
        });

        staff_list = new ArrayList<>();
        searchStaff("");

        return staffFragment;
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

        rowStaff.setDisplayBottomSpacer(false);

        rowStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometric_client = new NBiometricClient();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                log_time_fragment = new LogTimeFragment();
                log_time_fragment.setCancelable(false);
                log_time_fragment.setStaff(staff);
                log_time_fragment.setBiometricClient(biometric_client);
                log_time_fragment.show(fragmentManager, getString(R.string.label_log_time));
            }
        });

        return rowStaff;
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
            fab_auto_screening.setVisibility(View.GONE);
            rl_search_loader.setVisibility(View.VISIBLE);
            rl_no_record.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... param) {
            for(Staff staff : (searchTerm.equals("")? staff_factory.getActiveStaffs(): staff_factory.searchStaffs(searchTerm))){
                staff_list.add(createRowStaff(staff));
            }
            if(staff_list.size()>0){
                staff_list.get(staff_list.size()-1).setDisplayBottomSpacer(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            recycler_staff_list.setHasFixedSize(true);

            recycler_staff_list.setLayoutManager(recyclerViewLayoutManager);

            recyclerViewAdapter = new TimesheetStaffListAdapter(staff_list, getActivity());
            recycler_staff_list.setAdapter(recyclerViewAdapter);

            recycler_staff_list.setVisibility(staff_list.size()==0?View.GONE:View.VISIBLE);
            fab_auto_screening.setVisibility(staff_list.size()==0?View.GONE:View.VISIBLE);
            rl_search_loader.setVisibility(View.GONE);
            rl_no_record.setVisibility(staff_list.size()==0?View.VISIBLE:View.GONE);
        }
    }
}
