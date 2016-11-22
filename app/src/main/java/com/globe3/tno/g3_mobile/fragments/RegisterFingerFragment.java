package com.globe3.tno.g3_mobile.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
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
import com.globe3.tno.g3_mobile.activities.RegisterFingerActivity;
import com.globe3.tno.g3_mobile.app_objects.LogItem;
import com.globe3.tno.g3_mobile.app_objects.Staff;
import com.globe3.tno.g3_mobile.app_objects.factory.AuditFactory;
import com.globe3.tno.g3_mobile.app_objects.factory.StaffFactory;
import com.globe3.tno.g3_mobile.constants.TagTableUsage;
import com.globe3.tno.g3_mobile.util.FileUtility;
import com.neurotec.biometrics.NBiometricStatus;
import com.neurotec.biometrics.NFinger;
import com.neurotec.biometrics.NSubject;
import com.neurotec.biometrics.NTemplateSize;
import com.neurotec.biometrics.client.NBiometricClient;
import com.neurotec.devices.NDeviceManager;
import com.neurotec.devices.NDeviceType;
import com.neurotec.lang.NCore;
import com.neurotec.util.NImageUtils;
import com.neurotec.util.concurrent.CompletionHandler;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;

import static com.globe3.tno.g3_mobile.constants.App.GLOBE3_DATA_DIR;

public class RegisterFingerFragment extends DialogFragment {
    AuditFactory auditFactory;
    StaffFactory staffFactory;

    private String staff_unique;

    int step_num = 1;
    int finger_selected = 1;
    String pathFingerRef;
    String pathFingerCan;

    Context parentContext;

    NBiometricClient mBiometricClient;
    boolean scanner_found;

    LinearLayout ll_extract_node;
    ImageView iv_extract_node_check;
    TextView tv_extract_node_num;
    View line_step_1;

    LinearLayout ll_verify_node;
    ImageView iv_verify_node_check;
    TextView tv_verify_node_num;
    View line_step_2;

    LinearLayout ll_registration_node;

    TextView tv_prompt;
    ImageView iv_loader;
    ImageView iv_finger;
    TextView tv_action_button;

    final static int NODE_INACTIVE = 0;
    final static int NODE_ACTIVE = 1;
    final static int NODE_SUCCESS = 2;
    final static int NODE_FAILED = 3;

    final static int[] NODE_BACKGROUND = {R.drawable.stepper_node_inactive, R.drawable.stepper_node_active, R.drawable.stepper_node_success, R.drawable.stepper_node_failed};
    final static int[] NODE_NUM_DISPLAY = {View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE};
    final static int[] NODE_CHECK_DISPLAY = {View.GONE, View.GONE, View.VISIBLE, View.GONE};
    final static int[] LINE_DISPLAY = {View.GONE, View.GONE, View.VISIBLE, View.VISIBLE};

    final static int PROMPT_CONNECTING = 0;
    final static int PROMPT_PLACE_FINGER_SCAN = 1;
    final static int PROMPT_LIFT_FINGER_SCAN = 2;
    final static int PROMPT_SUCCESS = 3;
    final static int PROMPT_SCANNER_NOT_FOUND = 4;
    final static int PROMPT_EXTRACT_FAILED = 5;
    final static int PROMPT_ERROR_OCCURRED = 6;
    final static int PROMPT_NOT_MATCH = 7;
    final static int PROMPT_EXTRACTING = 8;
    final static int PROMPT_VERIFYING = 9;

    final static int[] PROMPT_TEXT = {R.string.msg_connecting_to_scanner, R.string.msg_place_finger_scanner, R.string.msg_lift_finger_scan_again, R.string.msg_registration_success, R.string.msg_scanner_not_found, R.string.msg_extraction_failed, R.string.msg_an_error_has_occured, R.string.msg_fingers_not_match, R.string.msg_extracting_fingerprint, R.string.msg_verifying_fingerprint};
    final static int[] PROMPT_TEXT_COLOR = {R.color.colorMenuLight, R.color.colorAccentLight, R.color.colorAccentLight, R.color.colorSuccess, R.color.colorFailed, R.color.colorFailed, R.color.colorFailed, R.color.colorFailed, R.color.colorAccentLight, R.color.colorAccentLight};
    final static int[] LOADER_DISPLAY = {View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.VISIBLE, View.VISIBLE};
    final static int[] LOADER_COLOR = {R.color.colorMenuLight, R.color.colorMenuLight, R.color.colorMenuLight, R.color.colorMenuLight, R.color.colorMenuLight, R.color.colorMenuLight, R.color.colorMenuLight, R.color.colorMenuLight, R.color.colorAccentLight, R.color.colorAccentLight};
    final static int[] FINGER_DISPLAY = {View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE, View.GONE};
    final static int[] FINGER_COLOR = {R.color.colorMenuLight, R.color.colorAccentLight, R.color.colorAccentLight, R.color.colorSuccess, R.color.colorFailed, R.color.colorFailed, R.color.colorFailed, R.color.colorFailed, R.color.colorAccentLight, R.color.colorAccentLight};
    final static int[] ACTION_TEXT = {R.string.label_refresh_scanner, R.string.label_refresh_scanner, R.string.label_refresh_scanner, R.string.label_finish, R.string.label_refresh_scanner, R.string.label_refresh_scanner, R.string.label_restart, R.string.label_restart, R.string.label_refresh_scanner, R.string.label_refresh_scanner};
    final static int[] ACTION_TEXT_COLOR = {R.color.colorMenuLight, R.color.colorAccentLight, R.color.colorAccentLight, R.color.colorSuccess, R.color.colorAccentLight, R.color.colorAccentLight, R.color.colorAccentLight, R.color.colorAccentLight, R.color.colorMenuLight, R.color.colorMenuLight};
    final static boolean[] ACTION_CLICKABLE = {false, true, true, true, true, true, true, true, false, false};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        pathFingerRef = GLOBE3_DATA_DIR + staff_unique + ".jpeg";
        pathFingerCan = GLOBE3_DATA_DIR + staff_unique + "_c.jpeg";

        View registerFragment = inflater.inflate(R.layout.fragment_regsiter_finger, viewGroup, false);
        parentContext = registerFragment.getContext();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        ll_extract_node = (LinearLayout) registerFragment.findViewById(R.id.ll_extract_node);
        iv_extract_node_check = (ImageView) registerFragment.findViewById(R.id.iv_extract_node_check);
        tv_extract_node_num = (TextView) registerFragment.findViewById(R.id.tv_extract_node_num);
        line_step_1 = registerFragment.findViewById(R.id.line_step_1);

        ll_verify_node = (LinearLayout) registerFragment.findViewById(R.id.ll_verify_node);
        iv_verify_node_check = (ImageView) registerFragment.findViewById(R.id.iv_verify_node_check);
        tv_verify_node_num = (TextView) registerFragment.findViewById(R.id.tv_verify_node_num);
        line_step_2 = registerFragment.findViewById(R.id.line_step_2);

        ll_registration_node = (LinearLayout) registerFragment.findViewById(R.id.ll_registration_node);

        tv_prompt = (TextView) registerFragment.findViewById(R.id.tv_prompt);
        iv_loader = (ImageView) registerFragment.findViewById(R.id.iv_loader);
        iv_finger = (ImageView) registerFragment.findViewById(R.id.iv_finger);
        tv_action_button = (TextView) registerFragment.findViewById(R.id.tv_action_button);

        iv_loader.startAnimation(AnimationUtils.loadAnimation(parentContext, R.anim.rotate));

        NCore.setContext(getActivity());

        startExtract();

        return registerFragment;
    }

    private CompletionHandler<NBiometricStatus, NSubject> completionHandler = new CompletionHandler<NBiometricStatus, NSubject>() {
        @Override
        public void completed(NBiometricStatus result, NSubject subject) {
            if (result == NBiometricStatus.OK) {
                if(step_num == 1){
                    setPrompt(PROMPT_EXTRACTING);
                }else{
                    setPrompt(PROMPT_VERIFYING);
                }

                try {
                    String sPrefix = step_num == 1 ? "" : "_c";

                    File outputFile = new File(GLOBE3_DATA_DIR, staff_unique + sPrefix + ".jpeg");
                    subject.getFingers().get(0).getImage().save(outputFile.getAbsolutePath());

                    if(step_num == 1){
                        extractStatus(NODE_SUCCESS);
                        capture();
                        step_num++;
                    }else{
                        try {
                            verify();
                        } catch (IOException e) {
                            e.printStackTrace();
                            setPrompt(PROMPT_ERROR_OCCURRED);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    setPrompt(PROMPT_ERROR_OCCURRED);
                }
            } else {
                setPrompt(PROMPT_EXTRACT_FAILED);
            }
        }

        @Override
        public void failed(Throwable exc, NSubject subject) {exc.printStackTrace();
        }
    };

    private void capture() {
        mBiometricClient = new NBiometricClient();
        NSubject subject = new NSubject();
        NFinger finger = new NFinger();

        mBiometricClient.setUseDeviceManager(true);
        NDeviceManager deviceManager = mBiometricClient.getDeviceManager();
        deviceManager.setDeviceTypes(EnumSet.of(NDeviceType.FINGER_SCANNER));
        mBiometricClient.initialize();

        NDeviceManager.DeviceCollection devices = deviceManager.getDevices();
        scanner_found = devices.size() > 0;
        if(!scanner_found) {
            return;
        }

        subject.getFingers().add(finger);

        mBiometricClient.setFingersTemplateSize(NTemplateSize.LARGE);
        mBiometricClient.createTemplate(subject, subject, completionHandler);
    }

    private void verify() throws IOException {
        NSubject fingerReference = createSubject(Uri.parse(pathFingerRef));
        NSubject fingerCandidate = createSubject(Uri.parse(pathFingerCan));

        mBiometricClient.verify(fingerReference, fingerCandidate, null, new CompletionHandler<NBiometricStatus, Void>() {
            @Override
            public void completed(NBiometricStatus result, Void attachment) {
                try {
                    if (result == NBiometricStatus.OK) {
                        Staff staff = staffFactory.getStaff(staff_unique);
                        staff.setRegistered(true);

                        switch (finger_selected){
                            case 1:
                                staff.setFingerprint_image1(FileUtility.getFileBlob(GLOBE3_DATA_DIR + staff_unique + ".jpeg"));
                                break;
                            case 2:
                                staff.setFingerprint_image2(FileUtility.getFileBlob(GLOBE3_DATA_DIR + staff_unique + ".jpeg"));
                                break;
                            case 3:
                                staff.setFingerprint_image3(FileUtility.getFileBlob(GLOBE3_DATA_DIR + staff_unique + ".jpeg"));
                                break;
                            case 4:
                                staff.setFingerprint_image3(FileUtility.getFileBlob(GLOBE3_DATA_DIR + staff_unique + ".jpeg"));
                                break;
                            case 5:
                                staff.setFingerprint_image3(FileUtility.getFileBlob(GLOBE3_DATA_DIR + staff_unique + ".jpeg"));
                                break;
                        }

                        staffFactory.updateStaff(staff);

                        LogItem logItem = auditFactory.Log(TagTableUsage.STAFF_SYNC_UP);
                        //new StaffSingleUploadTask(_activity, staff, staffFactory, logItem, null).execute();

                        insertRegister();
                        auditFactory.Log(TagTableUsage.FINGERPRINT_REGISTER);

                        verifyStatus(NODE_SUCCESS);
                    } else {
                        verifyStatus(NODE_FAILED);
                        setPrompt(PROMPT_NOT_MATCH);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    setPrompt(PROMPT_ERROR_OCCURRED);
                } finally {
                    fingerprintDelete(pathFingerRef);
                    fingerprintDelete(pathFingerCan);
                }
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                exc.printStackTrace();
                setPrompt(PROMPT_ERROR_OCCURRED);
            }
        });
    }

    private NSubject createSubject(Uri uri) throws IOException {
        NSubject subject = new NSubject();
        NFinger finger = new NFinger();
        finger.setImage(NImageUtils.fromUri(getActivity(), uri));
        subject.getFingers().add(finger);
        subject.setId(uri.getPath());
        return subject;
    }

    private void fingerprintDelete(String path){
        File file = new File(path);
        file.delete();
    }

    private void insertRegister(){
        Staff staff = staffFactory.getStaff(staff_unique);
        staffFactory.registerFingerprint(staff);
    }

    private class StepRegisterTask extends AsyncTask<Void, Void, Boolean> {
        Runnable pre;
        Runnable exec;
        Runnable post;

        public StepRegisterTask(Runnable pre, Runnable exec, Runnable post){
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

    private void setPrompt(final int status){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_prompt.setText(getText(PROMPT_TEXT[status]));
                tv_prompt.setTextColor(ContextCompat.getColor(parentContext, PROMPT_TEXT_COLOR[status]));
                iv_loader.setVisibility(LOADER_DISPLAY[status]);
                iv_loader.setColorFilter(ContextCompat.getColor(parentContext, LOADER_COLOR[status]));
                iv_finger.setVisibility(FINGER_DISPLAY[status]);
                iv_finger.setColorFilter(ContextCompat.getColor(parentContext, FINGER_COLOR[status]));
                if(status == PROMPT_CONNECTING || status == PROMPT_EXTRACTING || status == PROMPT_VERIFYING){
                    iv_loader.startAnimation(AnimationUtils.loadAnimation(parentContext, R.anim.rotate));
                }else{
                    iv_loader.clearAnimation();
                }
                tv_action_button.setText(getText(ACTION_TEXT[status]));
                tv_action_button.setTextColor(ContextCompat.getColor(parentContext, ACTION_TEXT_COLOR[status]));
                tv_action_button.setClickable(ACTION_CLICKABLE[status]);

                if(status == PROMPT_CONNECTING || status == PROMPT_PLACE_FINGER_SCAN || status == PROMPT_LIFT_FINGER_SCAN || status == PROMPT_SCANNER_NOT_FOUND || status == PROMPT_EXTRACT_FAILED){
                    tv_action_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new StepRegisterTask(new Runnable() {
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
                                    setPrompt(scanner_found ? (step_num==1?PROMPT_PLACE_FINGER_SCAN:PROMPT_LIFT_FINGER_SCAN) : PROMPT_SCANNER_NOT_FOUND);
                                }
                            }).execute();
                        }
                    });
                }else if(status == PROMPT_SUCCESS){
                    tv_action_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((RegisterFingerActivity) getActivity()).finishRegistration();
                        }
                    });
                }else if(status == PROMPT_ERROR_OCCURRED || status == PROMPT_NOT_MATCH){
                    tv_action_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            step_num = 1;
                            fingerprintDelete(pathFingerRef);
                            fingerprintDelete(pathFingerCan);
                            startExtract();
                        }
                    });
                }
            }
        });
    }

    private void extractStatus(final int status){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ll_extract_node.setBackground(ContextCompat.getDrawable(parentContext, NODE_BACKGROUND[status]));
                tv_extract_node_num.setVisibility(NODE_NUM_DISPLAY[status]);
                iv_extract_node_check.setVisibility(NODE_CHECK_DISPLAY[status]);
                line_step_1.setVisibility(LINE_DISPLAY[status]);
                if(status==NODE_SUCCESS){
                    ObjectAnimator stepperLineAnim = ObjectAnimator.ofFloat(line_step_1, "scaleX", 0.1f, 1f);
                    stepperLineAnim.setDuration(500);
                    stepperLineAnim.start();
                    stepperLineAnim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            verifyStatus(NODE_ACTIVE);
                            registerStatus(NODE_INACTIVE);

                            setPrompt(PROMPT_LIFT_FINGER_SCAN);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                }
            }
        });
    }

    private void verifyStatus(final int status){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ll_verify_node.setBackground(ContextCompat.getDrawable(parentContext, NODE_BACKGROUND[status]));
                tv_verify_node_num.setVisibility(NODE_NUM_DISPLAY[status]);
                iv_verify_node_check.setVisibility(NODE_CHECK_DISPLAY[status]);
                line_step_2.setVisibility(LINE_DISPLAY[status]);
                if(status==NODE_SUCCESS){
                    ObjectAnimator stepperLineAnim = ObjectAnimator.ofFloat(line_step_2, "scaleX", 0.1f, 1f);
                    stepperLineAnim.setDuration(500);
                    stepperLineAnim.start();
                    stepperLineAnim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            registerStatus(NODE_SUCCESS);

                            setPrompt(PROMPT_SUCCESS);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                }
            }
        });
    }

    private void registerStatus(final int status){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ll_registration_node.setBackground(ContextCompat.getDrawable(parentContext, NODE_BACKGROUND[status]));
            }
        });
    }

    public void startExtract(){
        new StepRegisterTask(new Runnable() {
            @Override
            public void run() {
                extractStatus(NODE_INACTIVE);
                verifyStatus(NODE_INACTIVE);
                registerStatus(NODE_INACTIVE);

                setPrompt(PROMPT_CONNECTING);
            }
        }, new Runnable() {
            @Override
            public void run() {
                try {
                    capture();
                    new Thread().sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Runnable() {
            @Override
            public void run() {
                extractStatus(NODE_ACTIVE);
                verifyStatus(NODE_INACTIVE);
                registerStatus(NODE_INACTIVE);

                setPrompt(scanner_found ? PROMPT_PLACE_FINGER_SCAN : PROMPT_SCANNER_NOT_FOUND);
            }
        }).execute();
    }

    public void finishRegistration(){
        dismiss();
    }

    public void setStaffUnique(String staff_unique){
        this.staff_unique = staff_unique;
    }

    public void setAuditFactory(AuditFactory auditFactory){
        this.auditFactory = auditFactory;
    }

    public void setStaffFactory(StaffFactory staffFactory){
        this.staffFactory = staffFactory;
    }
}
