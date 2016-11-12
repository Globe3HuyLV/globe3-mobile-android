package com.globe3.tno.g3_mobile.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.app_objects.TimeLog;
import com.globe3.tno.g3_mobile.util.DateUtility;

public class LogTimeSummaryFragment extends DialogFragment {
    Context parentContext;

    TimeLog timeLog;

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
        View logTimeSummaryFragment = inflater.inflate(R.layout.fragment_log_time_summary, viewGroup, false);
        parentContext = logTimeSummaryFragment.getContext();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        if(getArguments() != null && getArguments().containsKey("time_log")) {
            timeLog = (TimeLog) getArguments().getSerializable("time_log");
            if(timeLog != null){
                if(timeLog.getStaff()!=null){
                    tv_staff_id = (TextView) logTimeSummaryFragment.findViewById(R.id.tv_staff_id);
                    tv_staff_id.setText(timeLog.getStaff().getStaff_num());

                    tv_staff_name = (TextView) logTimeSummaryFragment.findViewById(R.id.tv_staff_name);
                    tv_staff_name.setText(timeLog.getStaff().getStaff_desc());
                }

                tv_log_type = (TextView) logTimeSummaryFragment.findViewById(R.id.tv_log_type);
                tv_log_type.setText(timeLog.getType());

                tv_log_time = (TextView) logTimeSummaryFragment.findViewById(R.id.tv_log_time);
                tv_log_time.setText(DateUtility.getDateString(timeLog.getDate(), "HH:mm"));

                tv_log_date = (TextView) logTimeSummaryFragment.findViewById(R.id.tv_log_date);
                tv_log_date.setText(DateUtility.getDateString(timeLog.getDate(), "dd MMM yyyy"));

                if(timeLog.getProject()!=null){
                    tv_log_project = (TextView) logTimeSummaryFragment.findViewById(R.id.tv_log_project);
                    tv_log_project.setText(timeLog.getProject().getCode());
                }
            }
        }


        tv_next_scan = (TextView) logTimeSummaryFragment.findViewById(R.id.tv_next_scan);
        tv_next_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //next scan
            }
        });

        tv_cancel = (TextView) logTimeSummaryFragment.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return logTimeSummaryFragment;
    }
}
