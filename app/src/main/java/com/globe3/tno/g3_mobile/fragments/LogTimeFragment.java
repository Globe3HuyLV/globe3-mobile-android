package com.globe3.tno.g3_mobile.fragments;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.globe3.tno.g3_mobile.app_objects.Project;
import com.globe3.tno.g3_mobile.app_objects.Staff;
import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.app_objects.TimeLog;

import java.util.Calendar;

public class LogTimeFragment extends DialogFragment {
    Context parentContext;

    Staff staff;
    Project project;

    TextView tv_staff_id;
    TextView tv_staff_name;
    TextView tv_time_in;
    TextView tv_time_out;
    TextView tv_refresh_scanner;
    TextView tv_cancel;

    LogTimeSummaryFragment logTimeSummaryFragment;
    LogTimeProjectFragment logTimeProjectFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View logTimeFragment = inflater.inflate(R.layout.fragment_log_time, viewGroup, false);
        parentContext = logTimeFragment.getContext();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        if(getArguments() != null && getArguments().containsKey("staff")){
            staff = (Staff) getArguments().getSerializable("staff");

            tv_staff_id = (TextView) logTimeFragment.findViewById(R.id.tv_staff_id);
            tv_staff_id.setText(staff.getStaff_num());

            tv_staff_name = (TextView) logTimeFragment.findViewById(R.id.tv_staff_name);
            tv_staff_name.setText(staff.getStaff_desc());
        }

        if(getArguments() != null && getArguments().containsKey("entproject")){
            project = (Project) getArguments().getSerializable("entproject");
        }

        tv_time_in = (TextView) logTimeFragment.findViewById(R.id.tv_time_in);
        tv_time_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_time_in.setBackground(ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.background_button_group_first_full_green, null));
                tv_time_in.setTextColor(ResourcesCompat.getColor(getActivity().getResources(), R.color.colorWhite, null));
                tv_time_in.setTypeface(null, Typeface.BOLD);
                tv_time_out.setBackground(ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.background_button_group_last_stroke_green, null));
                tv_time_out.setTextColor(ResourcesCompat.getColor(getActivity().getResources(), R.color.colorOrangeLight, null));
                tv_time_out.setTypeface(null, Typeface.NORMAL);
            }
        });

        tv_time_out = (TextView) logTimeFragment.findViewById(R.id.tv_time_out);
        tv_time_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_time_out.setBackground(ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.background_button_group_last_full_amber, null));
                tv_time_out.setTextColor(ResourcesCompat.getColor(getActivity().getResources(), R.color.colorWhite, null));
                tv_time_out.setTypeface(null, Typeface.BOLD);
                tv_time_in.setBackground(ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.background_button_group_first_stroke_amber, null));
                tv_time_in.setTextColor(ResourcesCompat.getColor(getActivity().getResources(), R.color.colorGreenLight, null));
                tv_time_in.setTypeface(null, Typeface.NORMAL);
            }
        });

        tv_refresh_scanner = (TextView) logTimeFragment.findViewById(R.id.tv_action_button);
        tv_refresh_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(project==null){
                    Bundle staffBundle = new Bundle();
                    staffBundle.putSerializable("staff", staff);

                    FragmentManager fragmentManager = getActivity().getFragmentManager();
                    logTimeProjectFragment = new LogTimeProjectFragment();
                    logTimeProjectFragment.setCancelable(false);
                    logTimeProjectFragment.setArguments(staffBundle);
                    logTimeProjectFragment.show(fragmentManager, getString(R.string.label_log_time_project));
                }else{
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
                    logTimeSummaryFragment.show(fragmentManager, getString(R.string.label_log_time_summary));
                }
            }
        });

        tv_cancel = (TextView) logTimeFragment.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return logTimeFragment;
    }
}
