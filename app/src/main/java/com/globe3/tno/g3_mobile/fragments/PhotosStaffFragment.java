package com.globe3.tno.g3_mobile.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.globe3.tno.g3_mobile.adapters.PhotosStaffListAdapter;
import com.globe3.tno.g3_mobile.adapters.RegisterFingerStaffListAdapter;
import com.globe3.tno.g3_mobile.app_objects.Staff;
import com.globe3.tno.g3_mobile.app_objects.factory.StaffFactory;
import com.globe3.tno.g3_mobile.view_objects.RowStaff;
import com.globe3.tno.g3_mobile.R;

import java.util.ArrayList;

public class PhotosStaffFragment extends Fragment {
    StaffFactory staffFactory;

    RecyclerView recycler_staff_list;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    RelativeLayout rl_search_loader;
    ImageView iv_search_loader;
    RelativeLayout rl_no_record;

    ArrayList<RowStaff> staff_list;

    SearchStaff searchStaff;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        staffFactory = new StaffFactory(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View staffFragment = inflater.inflate(R.layout.fragment_photos_staff_list, viewGroup, false);

        recycler_staff_list = (RecyclerView) staffFragment.findViewById(R.id.recycler_staff_list);
        rl_search_loader = (RelativeLayout) staffFragment.findViewById(R.id.rl_search_loader);
        iv_search_loader = (ImageView) staffFragment.findViewById(R.id.iv_search_loader);
        rl_no_record = (RelativeLayout) staffFragment.findViewById(R.id.rl_no_record);

        iv_search_loader.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.animate_rotate_clockwise));

        staff_list = new ArrayList<>();
        for(Staff staff : staffFactory.getActiveStaffs()){
            staff_list.add(createRowStaff(staff));
        }

        recycler_staff_list.setHasFixedSize(true);

        recyclerViewLayoutManager = new LinearLayoutManager(viewGroup.getContext());
        recycler_staff_list.setLayoutManager(recyclerViewLayoutManager);

        recyclerViewAdapter = new PhotosStaffListAdapter(staff_list, viewGroup.getContext());
        recycler_staff_list.setAdapter(recyclerViewAdapter);

        return staffFragment;
    }

    private RowStaff createRowStaff(final Staff staff){
        RowStaff rowStaff = new RowStaff();
        rowStaff.setStaffCode(staff.getStaff_num());
        rowStaff.setStaffName(staff.getStaff_desc());
        rowStaff.setStaffFingerCount((staff.getFingerprint_image1()==null?0:1)+(staff.getFingerprint_image2()==null?0:1));

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

            }
        });

        return rowStaff;
    }
    public void searchStaff(String searchTerm) {
        if(searchStaff != null){
            searchStaff.cancel(true);
        }
        searchStaff = new SearchStaff(searchTerm);
        searchStaff.execute();
    }

    public class SearchStaff extends AsyncTask<Void, Void, Void>
    {
        String searchTerm;

        public SearchStaff(String searchTerm){
            this.searchTerm = searchTerm;
            staff_list.clear();
        }

        @Override
        protected  void onPreExecute() {
            recycler_staff_list.setVisibility(View.GONE);
            rl_search_loader.setVisibility(View.VISIBLE);
            rl_no_record.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... param) {
            for(Staff staff : (searchTerm.equals("")?staffFactory.getActiveStaffs():staffFactory.searchStaffs(searchTerm))){
                staff_list.add(createRowStaff(staff));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            recycler_staff_list.setHasFixedSize(true);

            recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
            recycler_staff_list.setLayoutManager(recyclerViewLayoutManager);

            recyclerViewAdapter = new RegisterFingerStaffListAdapter(staff_list, getActivity());
            recycler_staff_list.setAdapter(recyclerViewAdapter);

            recycler_staff_list.setVisibility(staff_list.size()==0?View.GONE:View.VISIBLE);
            rl_search_loader.setVisibility(View.GONE);
            rl_no_record.setVisibility(staff_list.size()==0?View.VISIBLE:View.GONE);
        }
    }
}
