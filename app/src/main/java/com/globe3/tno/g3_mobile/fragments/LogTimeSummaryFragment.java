package com.globe3.tno.g3_mobile.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.app_objects.TimeLog;
import com.globe3.tno.g3_mobile.constants.TagTableUsage;
import com.globe3.tno.g3_mobile.util.DateUtility;

public class LogTimeSummaryFragment extends DialogFragment {
    Context parent_context;

    TimeLog time_log;

    LogTimeFragment log_time_fragment;
    LogTimeAutoFragment log_time_auto_fragment;
    LocationCheckFragment location_check_fragment;
    LocationCheckAutoFragment location_check_auto_fragment;

    ImageView iv_staff_photo;
    TextView tv_staff_id;
    TextView tv_staff_name;
    TextView tv_log_type;
    TextView tv_log_time;
    TextView tv_log_date;
    TextView tv_log_project;
    TextView tv_next_scan;
    TextView tv_cancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        final View logTimeSummaryFragment = inflater.inflate(R.layout.fragment_log_time_summary, viewGroup, false);
        parent_context = logTimeSummaryFragment.getContext();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        iv_staff_photo = (ImageView) logTimeSummaryFragment.findViewById(R.id.iv_staff_photo);
        tv_staff_id = (TextView) logTimeSummaryFragment.findViewById(R.id.tv_staff_id);
        tv_log_type = (TextView) logTimeSummaryFragment.findViewById(R.id.tv_log_type);
        tv_log_time = (TextView) logTimeSummaryFragment.findViewById(R.id.tv_log_time);
        tv_log_date = (TextView) logTimeSummaryFragment.findViewById(R.id.tv_log_date);
        tv_log_project = (TextView) logTimeSummaryFragment.findViewById(R.id.tv_log_project);

        if(time_log != null){
            if(time_log.getStaff()!=null){
                if(time_log.getStaff().getPhoto1()!=null){
                    Bitmap staffPhoto = BitmapFactory.decodeByteArray(time_log.getStaff().getPhoto1(), 0, time_log.getStaff().getPhoto1().length);

                    int newSize = staffPhoto.getWidth() < staffPhoto.getHeight() ? staffPhoto.getWidth() : staffPhoto.getHeight();

                    iv_staff_photo.setImageBitmap(Bitmap.createBitmap(staffPhoto, 0, 0, newSize, newSize));
                }else{
                    iv_staff_photo.setImageResource(R.drawable.ic_person_black_48dp);
                }
                tv_staff_id.setText(time_log.getStaff().getStaff_num());
                tv_staff_name = (TextView) logTimeSummaryFragment.findViewById(R.id.tv_staff_name);
                tv_staff_name.setText(time_log.getStaff().getStaff_desc());
            }

            switch (time_log.getType()){
                case TagTableUsage.TIMELOG_IN:
                    tv_log_type.setText(getString(R.string.label_time_in));
                    tv_log_type.setTextColor(ResourcesCompat.getColor(getActivity().getResources(), R.color.colorGreen, null));
                    tv_log_time.setTextColor(ResourcesCompat.getColor(getActivity().getResources(), R.color.colorGreen, null));
                    break;
                case TagTableUsage.TIMELOG_OUT:
                    tv_log_type.setText(getString(R.string.label_time_out));
                    tv_log_type.setTextColor(ResourcesCompat.getColor(getActivity().getResources(), R.color.colorOrange, null));
                    tv_log_time.setTextColor(ResourcesCompat.getColor(getActivity().getResources(), R.color.colorOrange, null));
                    break;
                default:
                    break;
            }

            tv_log_time.setText(DateUtility.getDateString(time_log.getDate(), "HH:mm"));
            tv_log_date.setText(DateUtility.getDateString(time_log.getDate(), "dd MMM yyyy"));

            if(time_log.getProject()!=null){
                tv_log_project.setText(time_log.getProject().getCode());
            }else{
                tv_log_project.setText("-");
            }
        }

        tv_next_scan = (TextView) logTimeSummaryFragment.findViewById(R.id.tv_next_scan);
        tv_next_scan.setOnClickListener(new View.OnClickListener() {
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
                if(location_check_fragment !=null){
                    location_check_fragment.startExtract();
                }
                dismiss();
            }
        });

        tv_cancel = (TextView) logTimeSummaryFragment.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(log_time_fragment !=null){
                    log_time_fragment.dismiss();
                }
                if(log_time_auto_fragment !=null){
                    log_time_auto_fragment.dismiss();
                }
                if(location_check_auto_fragment !=null){
                    location_check_auto_fragment.dismiss();
                }
                if(location_check_fragment !=null){
                    location_check_fragment.dismiss();
                }
                dismiss();
            }
        });
        return logTimeSummaryFragment;
    }

    public void setTimeLog(TimeLog timeLog) {
        this.time_log = timeLog;
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
}
