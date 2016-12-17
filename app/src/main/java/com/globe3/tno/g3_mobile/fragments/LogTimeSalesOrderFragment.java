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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.adapters.LogTimeSalesOrderListAdapter;
import com.globe3.tno.g3_mobile.adapters.SalesOrderListAdapter;
import com.globe3.tno.g3_mobile.app_objects.LogItem;
import com.globe3.tno.g3_mobile.app_objects.SalesOrder;
import com.globe3.tno.g3_mobile.app_objects.Staff;
import com.globe3.tno.g3_mobile.app_objects.StaffTeam;
import com.globe3.tno.g3_mobile.app_objects.TimeLog;
import com.globe3.tno.g3_mobile.app_objects.TimeRecord;
import com.globe3.tno.g3_mobile.app_objects.factory.AuditFactory;
import com.globe3.tno.g3_mobile.app_objects.factory.SalesOrderFactory;
import com.globe3.tno.g3_mobile.app_objects.factory.StaffFactory;
import com.globe3.tno.g3_mobile.async.TimeLogSingleUploadTask;
import com.globe3.tno.g3_mobile.view_objects.RowSalesOrder;

import java.util.ArrayList;
import java.util.Calendar;

public class LogTimeSalesOrderFragment extends DialogFragment {
    Staff staff;

    LogTimeFragment log_time_fragment;
    LogTimeAutoFragment log_time_auto_fragment;
    LocationCheckFragment location_check_fragment;
    LocationCheckAutoFragment location_check_auto_fragment;
    LogTimeSummaryFragment log_time_summary_fragment;

    RecyclerView recycler_sales_order_list;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    Spinner spn_sales_order_filter;

    TextView tv_search_sales_order;
    RelativeLayout rl_search_loader;
    ImageView iv_search_loader;
    RelativeLayout rl_no_record;

    ImageView iv_staff_photo;
    TextView tv_staff_id;
    TextView tv_staff_name;
    TextView tv_action_button;
    TextView tv_cancel;

    ArrayList<StaffTeam> staff_teams;
    ArrayList<RowSalesOrder> sales_order_list;

    String log_type;

    TimeLog time_log;

    SearchSalesOrder searchSalesOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View logTimeSalesOrderFragment = inflater.inflate(R.layout.fragment_log_time_sales_order, viewGroup, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        spn_sales_order_filter = (Spinner) logTimeSalesOrderFragment.findViewById(R.id.spn_sales_order_filter);

        tv_search_sales_order = (TextView) logTimeSalesOrderFragment.findViewById(R.id.tv_search_sales_order);
        rl_search_loader = (RelativeLayout) logTimeSalesOrderFragment.findViewById(R.id.rl_search_loader);
        iv_search_loader = (ImageView) logTimeSalesOrderFragment.findViewById(R.id.iv_search_loader);
        rl_no_record = (RelativeLayout) logTimeSalesOrderFragment.findViewById(R.id.rl_no_record);

        iv_search_loader.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.animate_rotate_clockwise));

        iv_staff_photo = (ImageView) logTimeSalesOrderFragment.findViewById(R.id.iv_staff_photo);
        tv_staff_id = (TextView) logTimeSalesOrderFragment.findViewById(R.id.tv_staff_id);
        tv_staff_name = (TextView) logTimeSalesOrderFragment.findViewById(R.id.tv_staff_name);
        tv_action_button = (TextView) logTimeSalesOrderFragment.findViewById(R.id.tv_action_button);
        tv_cancel = (TextView) logTimeSalesOrderFragment.findViewById(R.id.tv_cancel);

        staff_teams = new StaffFactory(getActivity()).getStaffTeams(staff.getUniquenumPri());
        ArrayList<String> spinnerArray =  new ArrayList<>();
        for(StaffTeam staffTeam : staff_teams){
            spinnerArray.add(staffTeam.getCode());
        }
        spinnerArray.add(getString(R.string.label_all_sales_order));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_sales_order_filter.setAdapter(adapter);

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

        recycler_sales_order_list = (RecyclerView) logTimeSalesOrderFragment.findViewById(R.id.recycler_sales_order_list);

        sales_order_list = new ArrayList<>();
        for(final SalesOrder sales_order : new SalesOrderFactory(getActivity()).getActiveSalesOrder()){
            sales_order_list.add(createRowSalesOrder(sales_order));
        }

        recycler_sales_order_list.setHasFixedSize(true);

        recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        recycler_sales_order_list.setLayoutManager(recyclerViewLayoutManager);

        recyclerViewAdapter = new LogTimeSalesOrderListAdapter(sales_order_list, getActivity());
        recycler_sales_order_list.setAdapter(recyclerViewAdapter);

        tv_search_sales_order.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable searchTerm) {
                searchSalesOrder(searchTerm.toString());
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
        return logTimeSalesOrderFragment;
    }

    private RowSalesOrder createRowSalesOrder(final SalesOrder sales_order){
        RowSalesOrder rowSalesOrder = new RowSalesOrder();
        rowSalesOrder.setSalesOrderCode(sales_order.getCode());
        rowSalesOrder.setSalesOrderDesc(sales_order.getDesc());
        rowSalesOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_log.setSalesOrder(sales_order);
                showSummary();
            }
        });

        return rowSalesOrder;
    }

    private void showSummary(){
        StaffFactory staffFactory = new StaffFactory(getActivity());

        TimeRecord timeRecord = staffFactory.logTime(staff, null, time_log.getSalesOrder(), time_log.getType());

        LogItem logItem = new AuditFactory(getActivity()).Log(time_log.getType(), staff.getUniquenumPri());

        new TimeLogSingleUploadTask(staffFactory, timeRecord, logItem).execute();

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

    public void searchSalesOrder(String searchTerm) {
        if(searchSalesOrder != null){
            searchSalesOrder.cancel(true);
        }
        searchSalesOrder = new SearchSalesOrder(searchTerm);
        searchSalesOrder.execute();
    }

    public class SearchSalesOrder extends AsyncTask<Void, Void, Void>
    {
        String searchTerm;

        public SearchSalesOrder(String searchTerm){
            this.searchTerm = searchTerm;
            sales_order_list.clear();
        }

        @Override
        protected  void onPreExecute()
        {
            recycler_sales_order_list.setVisibility(View.GONE);
            rl_search_loader.setVisibility(View.VISIBLE);
            rl_no_record.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... param) {
            for(SalesOrder sales_order : (searchTerm.equals("")?new SalesOrderFactory(getActivity()).getActiveSalesOrder():new SalesOrderFactory(getActivity()).searchSalesOrder(searchTerm))){
                sales_order_list.add(createRowSalesOrder(sales_order));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            recycler_sales_order_list.setHasFixedSize(true);

            recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
            recycler_sales_order_list.setLayoutManager(recyclerViewLayoutManager);

            recyclerViewAdapter = new SalesOrderListAdapter(sales_order_list, getActivity());
            recycler_sales_order_list.setAdapter(recyclerViewAdapter);

            recycler_sales_order_list.setVisibility(sales_order_list.size()==0?View.GONE:View.VISIBLE);
            rl_search_loader.setVisibility(View.GONE);
            rl_no_record.setVisibility(sales_order_list.size()==0?View.VISIBLE:View.GONE);
        }
    }
}
