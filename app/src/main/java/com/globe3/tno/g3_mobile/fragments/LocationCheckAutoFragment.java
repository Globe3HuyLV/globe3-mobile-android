package com.globe3.tno.g3_mobile.fragments;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.app_objects.SalesOrder;
import com.globe3.tno.g3_mobile.app_objects.TimeRecord;
import com.globe3.tno.g3_mobile.app_objects.LogItem;
import com.globe3.tno.g3_mobile.app_objects.Project;
import com.globe3.tno.g3_mobile.app_objects.Staff;
import com.globe3.tno.g3_mobile.app_objects.TimeLog;
import com.globe3.tno.g3_mobile.app_objects.factory.AuditFactory;
import com.globe3.tno.g3_mobile.app_objects.factory.StaffFactory;
import com.globe3.tno.g3_mobile.async.TimeLogSingleUploadTask;
import com.globe3.tno.g3_mobile.constants.TagTableUsage;
import com.neurotec.biometrics.NBiometricOperation;
import com.neurotec.biometrics.NBiometricStatus;
import com.neurotec.biometrics.NBiometricTask;
import com.neurotec.biometrics.NFinger;
import com.neurotec.biometrics.NMatchingResult;
import com.neurotec.biometrics.NSubject;
import com.neurotec.biometrics.NTemplateSize;
import com.neurotec.biometrics.client.NBiometricClient;
import com.neurotec.devices.NDeviceManager;
import com.neurotec.devices.NDeviceType;
import com.neurotec.util.concurrent.CompletionHandler;

import java.util.Calendar;
import java.util.EnumSet;

import static com.globe3.tno.g3_mobile.globals.Globals.ACTIVE_FEATURE_TIMESHEET_PROJECT;
import static com.globe3.tno.g3_mobile.globals.Globals.ACTIVE_FEATURE_TIMESHEET_SALES_ORDER;
import static com.globe3.tno.g3_mobile.globals.Globals.BIOMETRIC_DATA;

public class LocationCheckAutoFragment extends DialogFragment {
    Context parent_context;

    Project project;
    SalesOrder sales_order;

    LogTimeStaffFragment log_time_staff_fragment;
    LogTimeProjectFragment log_time_project_fragment;
    LogTimeSalesOrderFragment log_time_sale_order_fragment;
    LogTimeSummaryFragment log_time_summary_fragment;

    NBiometricClient biometric_client;
    boolean scanner_found;

    String log_type = TagTableUsage.LOCATION_CHECK;

    LinearLayout ll_main_container;

    TextView tv_prompt;
    ImageView iv_loader;
    ImageView iv_finger;
    TextView tv_action_button;
    TextView tv_cancel;

    private View.OnClickListener refresh = new  View.OnClickListener(){
        @Override
        public void onClick(View v) {
            new ScanTask(new Runnable() {
                @Override
                public void run() {
                    biometric_client = new NBiometricClient();
                    setPrompt(PROMPT_CONNECTING);
                }
            }, new Runnable() {
                @Override
                public void run() {
                    try {
                        capture();
                        new Thread().sleep(5000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Runnable() {
                @Override
                public void run() {
                    setPrompt(scanner_found ? PROMPT_PLACE_FINGER_SCAN : PROMPT_SCANNER_NOT_FOUND);
                }
            }).execute();
        }
    };

    private View.OnClickListener scan_again = new  View.OnClickListener(){
        @Override
        public void onClick(View v) {
            setPrompt(PROMPT_PLACE_FINGER_SCAN);
            capture();
        }
    };

    private View.OnClickListener cancel = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(biometric_client !=null){
                biometric_client.cancel();
                biometric_client = null;
            }
            if(log_time_staff_fragment != null){
                log_time_staff_fragment.resume();
            }
            dismiss();
        }
    };

    private Runnable loaderAnimate = new Runnable() {
        @Override
        public void run() {
            iv_loader.startAnimation(AnimationUtils.loadAnimation(parent_context, R.anim.rotate));
        }
    };

    private Runnable loaderStop = new Runnable() {
        @Override
        public void run() {
            iv_loader.clearAnimation();
        }
    };

    final static int PROMPT_CONNECTING = 0;
    final static int PROMPT_PLACE_FINGER_SCAN = 1;
    final static int PROMPT_SEARCHING = 2;
    final static int PROMPT_FINGER_NOT_FOUND = 3;
    final static int PROMPT_SCANNER_NOT_FOUND = 4;
    final static int PROMPT_EXTRACT_FAILED = 5;
    final static int PROMPT_MULTIPLE_RESULT = 6;

    final static int[] PROMPT_TEXT = {R.string.msg_connecting_to_scanner, R.string.msg_place_finger_scanner, R.string.msg_searching, R.string.msg_no_match_found, R.string.msg_scanner_not_found, R.string.msg_extraction_failed, R.string.msg_finger_registered_to_multiple_staff};
    final static int[] PROMPT_TEXT_COLOR = {R.color.colorMenuLight, R.color.colorAccentLight, R.color.colorAccentLight, R.color.colorFailed, R.color.colorFailed, R.color.colorFailed, R.color.colorFailed};
    final static int[] LOADER_DISPLAY = {View.VISIBLE, View.GONE, View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE};
    final static int[] LOADER_COLOR = {R.color.colorMenuLight, R.color.colorAccentLight, R.color.colorAccentLight, R.color.colorFailed, R.color.colorFailed, R.color.colorFailed, R.color.colorFailed};
    final static int[] FINGER_DISPLAY = {View.GONE, View.VISIBLE, View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE};
    final static int[] FINGER_COLOR = {R.color.colorMenuLight, R.color.colorAccentLight, R.color.colorAccentLight, R.color.colorFailed, R.color.colorFailed, R.color.colorFailed, R.color.colorFailed};
    final Runnable[] LOADER_ANIMATION = {loaderAnimate, loaderStop, loaderAnimate, loaderStop, loaderStop, loaderStop, loaderStop};
    final static int[] ACTION_TEXT = {R.string.msg_refresh_scanner, R.string.msg_refresh_scanner, R.string.msg_refresh_scanner, R.string.msg_scan_again, R.string.msg_refresh_scanner, R.string.msg_scan_again, R.string.msg_scan_again};
    final static int[] ACTION_TEXT_COLOR = {R.color.colorMenuLight, R.color.colorAccent, R.color.colorMenuLight, R.color.colorAccent, R.color.colorAccent, R.color.colorAccent, R.color.colorAccent};
    final static int[] CANCEL_TEXT_COLOR = {R.color.colorMenuLight, R.color.colorBlueGrey, R.color.colorMenuLight, R.color.colorBlueGrey, R.color.colorBlueGrey, R.color.colorBlueGrey, R.color.colorBlueGrey};
    final static boolean[] ACTION_CLICKABLE = {false, true, false, true, true, true, true};
    final static boolean[] CANCEL_CLICKABLE = {false, true, false, true, true, true, true};
    final View.OnClickListener[] ONCLICK_ACTION = {null, refresh, null, scan_again, refresh, refresh, scan_again};
    final View.OnClickListener[] ONCLICK_CANCEL = {null, cancel, null, cancel, cancel, cancel, cancel};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View locationCheckAutoFragment = inflater.inflate(R.layout.fragment_location_check_auto, viewGroup, false);
        parent_context = locationCheckAutoFragment.getContext();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        ll_main_container = (LinearLayout) locationCheckAutoFragment.findViewById(R.id.ll_main_container);

        tv_prompt = (TextView) locationCheckAutoFragment.findViewById(R.id.tv_prompt);
        iv_loader = (ImageView) locationCheckAutoFragment.findViewById(R.id.iv_loader);
        iv_finger = (ImageView) locationCheckAutoFragment.findViewById(R.id.iv_finger);
        tv_action_button = (TextView) locationCheckAutoFragment.findViewById(R.id.tv_action_button);
        tv_cancel = (TextView) locationCheckAutoFragment.findViewById(R.id.tv_cancel);

        tv_cancel.setOnClickListener(cancel);

        startExtract();
        return locationCheckAutoFragment;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(log_time_staff_fragment != null){
            log_time_staff_fragment.resume();
        }
    }

    private CompletionHandler<NBiometricStatus, NSubject> captureHandler = new CompletionHandler<NBiometricStatus, NSubject>() {
        @Override
        public void completed(NBiometricStatus result, NSubject subject) {
            if (result == NBiometricStatus.OK) {
                setPrompt(PROMPT_SEARCHING);

                try {
                    new Thread().sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                NBiometricTask identifyTask = BIOMETRIC_DATA.createTask(EnumSet.of(NBiometricOperation.IDENTIFY), subject);
                BIOMETRIC_DATA.performTask(identifyTask, NBiometricOperation.IDENTIFY, identifyHandler);

            } else {
                setPrompt(PROMPT_EXTRACT_FAILED);
            }
        }

        @Override
        public void failed(Throwable exc, NSubject subject) {exc.printStackTrace();
        }
    };

    private CompletionHandler<NBiometricTask, NBiometricOperation> identifyHandler = new CompletionHandler<NBiometricTask, NBiometricOperation>() {

        @Override
        public void completed(final NBiometricTask result, NBiometricOperation attachment) {
            switch (attachment) {
                case IDENTIFY:
                    if (result.getStatus() == NBiometricStatus.OK) {
                        if(result.getSubjects().get(0).getMatchingResults().size() > 0){
                            boolean single_staff = true;
                            String staff_unique = result.getSubjects().get(0).getMatchingResults().get(0).getId();
                            staff_unique = staff_unique.substring(0, staff_unique.indexOf('_'));

                            for(NMatchingResult matchingResult : result.getSubjects().get(0).getMatchingResults()){
                                if(!staff_unique.equals(matchingResult.getId().substring(0, matchingResult.getId().indexOf('_')))){
                                    single_staff = false;
                                    break;
                                }
                            }
                            if(single_staff){
                                if(ACTIVE_FEATURE_TIMESHEET_PROJECT){
                                    if(project==null && !log_type.equals(TagTableUsage.TIMELOG_OUT)){
                                        selectProject(new StaffFactory(getActivity()).getStaff(staff_unique));
                                    }else{
                                        showSummary(new StaffFactory(getActivity()).getStaff(staff_unique));
                                    }
                                }else if(ACTIVE_FEATURE_TIMESHEET_SALES_ORDER){
                                    if(sales_order==null && !log_type.equals(TagTableUsage.TIMELOG_OUT)){
                                        selectSalesOrder(new StaffFactory(getActivity()).getStaff(staff_unique));
                                    }else{
                                        showSummary(new StaffFactory(getActivity()).getStaff(staff_unique));
                                    }
                                }else{
                                    showSummary(new StaffFactory(getActivity()).getStaff(staff_unique));
                                }

                                biometric_client.cancel();
                                biometric_client = null;
                            }else{
                                setPrompt(PROMPT_MULTIPLE_RESULT);
                            }
                        }else{
                            setPrompt(PROMPT_FINGER_NOT_FOUND);
                        }

                    } else if (result.getStatus() == NBiometricStatus.MATCH_NOT_FOUND) {
                        setPrompt(PROMPT_FINGER_NOT_FOUND);
                    }else {
                        setPrompt(PROMPT_EXTRACT_FAILED);
                    }

                    break;
                default:
                    break;
            }
        }

        @Override
        public void failed(Throwable th, NBiometricOperation attachment) {
            th.printStackTrace();
            setPrompt(PROMPT_EXTRACT_FAILED);
        }

    };

    private void capture() {
        if(biometric_client ==null){
            biometric_client = new NBiometricClient();
        }
        NSubject subject = new NSubject();
        NFinger finger = new NFinger();

        biometric_client.setUseDeviceManager(true);
        NDeviceManager deviceManager = biometric_client.getDeviceManager();
        deviceManager.setDeviceTypes(EnumSet.of(NDeviceType.FINGER_SCANNER));
        biometric_client.initialize();

        NDeviceManager.DeviceCollection devices = deviceManager.getDevices();
        scanner_found = devices.size() > 0;
        if(!scanner_found) {
            setPrompt(PROMPT_SCANNER_NOT_FOUND);
            return;
        }

        subject.getFingers().add(finger);

        biometric_client.setFingersTemplateSize(NTemplateSize.LARGE);
        biometric_client.createTemplate(subject, subject, captureHandler);
    }

    private void setPrompt(final int status){
        if(getActivity()!=null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_prompt.setText(getText(PROMPT_TEXT[status]));
                    tv_prompt.setTextColor(ContextCompat.getColor(parent_context, PROMPT_TEXT_COLOR[status]));
                    iv_loader.setVisibility(LOADER_DISPLAY[status]);
                    iv_loader.setColorFilter(ContextCompat.getColor(parent_context, LOADER_COLOR[status]));
                    iv_finger.setVisibility(FINGER_DISPLAY[status]);
                    iv_finger.setColorFilter(ContextCompat.getColor(parent_context, FINGER_COLOR[status]));
                    LOADER_ANIMATION[status].run();
                    tv_action_button.setText(ACTION_TEXT[status]);
                    tv_action_button.setTextColor(ContextCompat.getColor(parent_context, ACTION_TEXT_COLOR[status]));
                    tv_action_button.setClickable(ACTION_CLICKABLE[status]);
                    tv_action_button.setOnClickListener(ONCLICK_ACTION[status]);
                    tv_cancel.setTextColor(ContextCompat.getColor(parent_context, CANCEL_TEXT_COLOR[status]));
                    tv_cancel.setClickable(CANCEL_CLICKABLE[status]);
                    tv_cancel.setOnClickListener(ONCLICK_CANCEL[status]);
                }
            });
        }
    }

    public void startExtract(){
        ll_main_container.setVisibility(View.VISIBLE);
        new ScanTask(new Runnable() {
            @Override
            public void run() {
                setPrompt(PROMPT_CONNECTING);
            }
        }, new Runnable() {
            @Override
            public void run() {
                try {
                    capture();
                    new Thread().sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Runnable() {
            @Override
            public void run() {
                setPrompt(scanner_found ? PROMPT_PLACE_FINGER_SCAN : PROMPT_SCANNER_NOT_FOUND);
            }
        }).execute();
    }

    private void selectProject(Staff staff){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ll_main_container.setVisibility(View.GONE);
            }
        });

        FragmentManager fragmentManager = getActivity().getFragmentManager();
        log_time_project_fragment = new LogTimeProjectFragment();
        log_time_project_fragment.setCancelable(false);
        log_time_project_fragment.setStaff(staff);
        log_time_project_fragment.setLog_type(log_type);
        log_time_project_fragment.setLocationCheckAutoFragment(this);
        log_time_project_fragment.show(fragmentManager, getString(R.string.label_log_time_project));
    }

    private void selectSalesOrder(Staff staff){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ll_main_container.setVisibility(View.GONE);
            }
        });

        FragmentManager fragmentManager = getActivity().getFragmentManager();
        log_time_sale_order_fragment = new LogTimeSalesOrderFragment();
        log_time_sale_order_fragment.setCancelable(false);
        log_time_sale_order_fragment.setStaff(staff);
        log_time_sale_order_fragment.setLog_type(log_type);
        log_time_sale_order_fragment.setLocationCheckAutoFragment(this);
        log_time_sale_order_fragment.show(fragmentManager, getString(R.string.label_log_time_sales_order));
    }

    private void showSummary(Staff staff){
        StaffFactory staffFactory = new StaffFactory(getActivity());

        TimeLog timeLog = new TimeLog();
        timeLog.setDate(Calendar.getInstance().getTime());
        timeLog.setProject(project);
        timeLog.setType(log_type);
        timeLog.setStaff(staff);

        TimeRecord timeRecord = staffFactory.logTime(staff, project, sales_order, log_type);

        LogItem logItem = new AuditFactory(getActivity()).Log(log_type, staff.getUniquenumPri());

        new TimeLogSingleUploadTask(staffFactory, timeRecord, logItem).execute();

        FragmentManager fragmentManager = getActivity().getFragmentManager();
        log_time_summary_fragment = new LogTimeSummaryFragment();
        log_time_summary_fragment.setCancelable(false);
        log_time_summary_fragment.setTimeLog(timeLog);
        log_time_summary_fragment.setLocationCheckAutoFragment(this);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ll_main_container.setVisibility(View.GONE);
            }
        });

        log_time_summary_fragment.show(fragmentManager, getString(R.string.label_log_time_summary));
    }

    private class ScanTask extends AsyncTask<Void, Void, Boolean> {
        Runnable pre;
        Runnable exec;
        Runnable post;

        public ScanTask(Runnable pre, Runnable exec, Runnable post){
            this.pre = pre;
            this.exec = exec;
            this.post = post;
        }
        @Override
        protected void onPreExecute() {
            pre.run();
        }

        protected Boolean doInBackground(Void... params) {
            try{
                exec.run();

                Thread thread = new Thread();
                thread.sleep(100);
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
        protected void onPostExecute(Boolean taskSuccess) {
            if(taskSuccess){
                post.run();
            }else{
                post.run();
            }
        }
    }

    public void setProject(Project project) {
        this.project = project;
    }
    public void setSalesOrder(SalesOrder salesOrder) {
        this.sales_order = salesOrder;
    }
    public void setLogTimeStaffFragment(LogTimeStaffFragment log_time_staff_fragment) {
        this.log_time_staff_fragment = log_time_staff_fragment;
    }
    public void setBiometricClient(NBiometricClient biometric_client) {
        this.biometric_client = biometric_client;
    }
}
