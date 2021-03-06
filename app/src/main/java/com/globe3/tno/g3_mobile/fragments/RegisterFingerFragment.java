package com.globe3.tno.g3_mobile.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
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
import com.globe3.tno.g3_mobile.async.StaffSingleUploadTask;
import com.globe3.tno.g3_mobile.constants.TagTableUsage;
import com.globe3.tno.g3_mobile.util.BiometricUtility;
import com.globe3.tno.g3_mobile.util.FileUtility;
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
import com.neurotec.images.NImage;
import com.neurotec.lang.NCore;
import com.neurotec.util.concurrent.CompletionHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.EnumSet;

import static com.globe3.tno.g3_mobile.constants.App.APP_NAME;
import static com.globe3.tno.g3_mobile.constants.App.FINGER_COUNTER;
import static com.globe3.tno.g3_mobile.constants.App.GLOBE3_DATA_DIR;
import static com.globe3.tno.g3_mobile.globals.Globals.BIOMETRIC_DATA;

public class RegisterFingerFragment extends DialogFragment {
    SelectFingerFragment select_finger_fragment;

    AuditFactory audit_factory;
    StaffFactory staff_factory;

    private Staff staff;

    int step_num = 1;
    int finger_selected = 1;

    Context parent_context;

    NBiometricClient biometric_client;
    boolean scanner_found;

    ImageView iv_staff_photo;
    TextView tv_staff_num;
    TextView tv_staff_desc;
    ImageView iv_staff_finger_count;

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
    TextView tv_cancel;

    NSubject fingerReference;
    NSubject fingerCandidate;

    private View.OnClickListener refresh = new  View.OnClickListener(){
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
    };

    private View.OnClickListener restart = new  View.OnClickListener(){
        @Override
        public void onClick(View v) {
            step_num = 1;
            startExtract();
        }
    };

    private View.OnClickListener finish = new  View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(biometric_client !=null){
                biometric_client.cancel();
            }
            biometric_client = null;
            ((RegisterFingerActivity) getActivity()).finishRegistration();
            if(select_finger_fragment !=null){
                select_finger_fragment.dismiss();
            }
            dismiss();
        }
    };

    private View.OnClickListener cancel = new  View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(biometric_client !=null){
                biometric_client.cancel();
            }
            biometric_client = null;
            if(select_finger_fragment !=null){
                select_finger_fragment.show(staff);
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
    final static int PROMPT_FINGER_EXIST = 10;

    final static int[] PROMPT_TEXT = {R.string.msg_connecting_to_scanner, R.string.msg_place_finger_scanner, R.string.msg_lift_finger_scan_again, R.string.msg_registration_success, R.string.msg_scanner_not_found, R.string.msg_extraction_failed, R.string.msg_an_error_has_occured, R.string.msg_fingers_not_match, R.string.msg_extracting_fingerprint, R.string.msg_verifying_fingerprint, R.string.msg_finger_registered_by_another_staff};
    final static int[] PROMPT_TEXT_COLOR = {R.color.colorMenuLight, R.color.colorAccentLight, R.color.colorAccentLight, R.color.colorSuccess, R.color.colorFailed, R.color.colorFailed, R.color.colorFailed, R.color.colorFailed, R.color.colorAccentLight, R.color.colorAccentLight, R.color.colorFailed};
    final static int[] LOADER_DISPLAY = {View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.VISIBLE, View.VISIBLE, View.GONE};
    final static int[] LOADER_COLOR = {R.color.colorMenuLight, R.color.colorMenuLight, R.color.colorMenuLight, R.color.colorMenuLight, R.color.colorMenuLight, R.color.colorMenuLight, R.color.colorMenuLight, R.color.colorMenuLight, R.color.colorAccentLight, R.color.colorAccentLight, R.color.colorMenuLight};
    final static int[] FINGER_DISPLAY = {View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE, View.GONE, View.VISIBLE};
    final static int[] FINGER_COLOR = {R.color.colorMenuLight, R.color.colorAccentLight, R.color.colorAccentLight, R.color.colorSuccess, R.color.colorFailed, R.color.colorFailed, R.color.colorFailed, R.color.colorFailed, R.color.colorAccentLight, R.color.colorAccentLight, R.color.colorFailed};
    final static int[] ACTION_TEXT = {R.string.label_refresh_scanner, R.string.label_refresh_scanner, R.string.label_refresh_scanner, R.string.label_finish, R.string.label_refresh_scanner, R.string.label_refresh_scanner, R.string.label_restart, R.string.label_restart, R.string.label_refresh_scanner, R.string.label_refresh_scanner, R.string.label_restart};
    final static int[] ACTION_TEXT_COLOR = {R.color.colorMenuLight, R.color.colorAccentLight, R.color.colorAccentLight, R.color.colorSuccess, R.color.colorAccentLight, R.color.colorAccentLight, R.color.colorAccentLight, R.color.colorAccentLight, R.color.colorMenuLight, R.color.colorMenuLight, R.color.colorAccentLight};
    final static int[] CANCEL_TEXT_COLOR = {R.color.colorMenuLight, R.color.colorBlueGrey, R.color.colorBlueGrey, R.color.colorBlueGrey, R.color.colorBlueGrey, R.color.colorBlueGrey, R.color.colorBlueGrey, R.color.colorBlueGrey, R.color.colorMenuLight, R.color.colorMenuLight, R.color.colorBlueGrey};
    final static boolean[] ACTION_CLICKABLE = {false, true, true, true, true, true, true, true, false, false, true};
    final static boolean[] CANCEL_CLICKABLE = {false, true, true, true, true, true, true, true, false, false, true};
    final Runnable[] LOADER_ANIMATION = {loaderAnimate, loaderStop, loaderStop, loaderStop, loaderStop, loaderStop, loaderStop, loaderStop, loaderAnimate, loaderAnimate, loaderStop};
    final View.OnClickListener[] ONCLICK_ACTION = {refresh, refresh, refresh, finish, refresh, refresh, restart, restart, refresh, refresh, restart};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {

        View registerFragment = inflater.inflate(R.layout.fragment_regsiter_finger, viewGroup, false);
        parent_context = registerFragment.getContext();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        iv_staff_photo = (ImageView) registerFragment.findViewById(R.id.iv_staff_photo);
        tv_staff_num = (TextView) registerFragment.findViewById(R.id.tv_staff_num);
        tv_staff_desc = (TextView) registerFragment.findViewById(R.id.tv_staff_desc);
        iv_staff_finger_count = (ImageView) registerFragment.findViewById(R.id.iv_staff_finger_count);

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
        tv_cancel = (TextView) registerFragment.findViewById(R.id.tv_cancel);

        iv_staff_finger_count.setImageDrawable(ContextCompat.getDrawable(getActivity(), FINGER_COUNTER[finger_selected]));

        tv_cancel.setOnClickListener(cancel);

        iv_loader.startAnimation(AnimationUtils.loadAnimation(parent_context, R.anim.rotate));

        NCore.setContext(getActivity());

        startExtract();

        return registerFragment;
    }

    private CompletionHandler<NBiometricStatus, NSubject> completionHandler = new CompletionHandler<NBiometricStatus, NSubject>() {
        @Override
        public void completed(NBiometricStatus result, NSubject subject) {
            if (result == NBiometricStatus.OK) {
                if(step_num == 1){
                    fingerReference = subject;
                    setPrompt(PROMPT_EXTRACTING);
                }else{
                    fingerCandidate = subject;
                    setPrompt(PROMPT_VERIFYING);
                }

                try {

                    if(step_num == 1){
                        extractStatus(NODE_SUCCESS);
                        capture();
                        step_num++;
                    }else{
                        verify();
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

    private CompletionHandler<NBiometricTask, NBiometricOperation> identifyHandler = new CompletionHandler<NBiometricTask, NBiometricOperation>() {

        @Override
        public void completed(final NBiometricTask result, NBiometricOperation attachment) {
            switch (attachment) {
                case IDENTIFY:
                    if (result.getStatus() == NBiometricStatus.MATCH_NOT_FOUND) {;
                        saveFinger();
                    } else if (result.getStatus() == NBiometricStatus.OK) {
                        boolean single_staff = true;
                        String staff_unique = result.getSubjects().get(0).getMatchingResults().get(0).getId();

                        staff_unique = staff_unique.substring(0, staff_unique.indexOf('_'));

                        for(NMatchingResult matchingResult : result.getSubjects().get(0).getMatchingResults()){
                            if(!staff_unique.equals(matchingResult.getId().substring(0, matchingResult.getId().indexOf('_')))){
                                single_staff = false;
                                break;
                            }
                        }

                        if(single_staff && staff_unique.equals(staff.getUniquenumPri())){
                            saveFinger();
                        }else{
                            setPrompt(PROMPT_FINGER_EXIST);
                        }
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

    private void saveFinger(){
        try {
            staff.setRegistered(true);
            String subject_id = staff.getUniquenumPri() + "_" + String.valueOf(finger_selected);

            Bitmap finger_bitmap = fingerReference.getFingers().get(0).getImage().toBitmap();
            ByteArrayOutputStream finger_bos = new ByteArrayOutputStream();
            finger_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, finger_bos);

            byte[] finger_byte = finger_bos.toByteArray();

            finger_byte[13] = 00000001;
            finger_byte[14] = 00000001;
            finger_byte[15] = (byte) 244;
            finger_byte[16] =  00000001;
            finger_byte[17] = (byte) 244;

            switch (finger_selected){
                case 1:
                    staff.setFingerprint_image1(finger_byte);
                    finger_byte = staff.getFingerprint_image1();
                    break;
                case 2:
                    staff.setFingerprint_image2(finger_byte);
                    finger_byte = staff.getFingerprint_image2();
                    break;
                case 3:
                    staff.setFingerprint_image3(finger_byte);
                    finger_byte = staff.getFingerprint_image3();
                    break;
                case 4:
                    staff.setFingerprint_image4(finger_byte);
                    finger_byte = staff.getFingerprint_image4();
                    break;
                case 5:
                    staff.setFingerprint_image5(finger_byte);
                    finger_byte = staff.getFingerprint_image5();
                    break;
            }

            staff_factory.updateStaff(staff);

            audit_factory.Log(TagTableUsage.FINGERPRINT_REGISTER, staff.getUniquenumPri());

            new StaffSingleUploadTask(staff_factory, staff, audit_factory.Log(TagTableUsage.STAFF_SYNC_UP, staff.getUniquenumPri())).execute();

            staff_factory.registerFingerprint(staff);

            BiometricUtility.updateFinger(finger_byte, subject_id);

            verifyStatus(NODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            setPrompt(PROMPT_EXTRACT_FAILED);
        }

    }

    private void capture() {
        biometric_client = new NBiometricClient();
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
        biometric_client.createTemplate(subject, subject, completionHandler);
    }

    private void verify() throws IOException {

        biometric_client.verify(fingerReference, fingerCandidate, null, new CompletionHandler<NBiometricStatus, Void>() {
            @Override
            public void completed(NBiometricStatus result, Void attachment) {
                try {
                    if (result == NBiometricStatus.OK) {
                        NBiometricTask identifyTask = BIOMETRIC_DATA.createTask(EnumSet.of(NBiometricOperation.IDENTIFY), fingerReference);
                        BIOMETRIC_DATA.performTask(identifyTask, NBiometricOperation.IDENTIFY, identifyHandler);
                    } else {
                        verifyStatus(NODE_FAILED);
                        setPrompt(PROMPT_NOT_MATCH);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    setPrompt(PROMPT_ERROR_OCCURRED);
                }
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                exc.printStackTrace();
                setPrompt(PROMPT_ERROR_OCCURRED);
            }
        });
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
                    tv_action_button.setText(getText(ACTION_TEXT[status]));
                    tv_action_button.setTextColor(ContextCompat.getColor(parent_context, ACTION_TEXT_COLOR[status]));
                    tv_action_button.setClickable(ACTION_CLICKABLE[status]);
                    tv_action_button.setOnClickListener(ONCLICK_ACTION[status]);
                    tv_cancel.setTextColor(ContextCompat.getColor(parent_context, CANCEL_TEXT_COLOR[status]));
                    tv_cancel.setClickable(CANCEL_CLICKABLE[status]);
                }
            });
        }
    }

    private void extractStatus(final int status){
        if(getActivity()!=null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ll_extract_node.setBackground(ContextCompat.getDrawable(parent_context, NODE_BACKGROUND[status]));
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
    }

    private void verifyStatus(final int status){
        if(getActivity()!=null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ll_verify_node.setBackground(ContextCompat.getDrawable(parent_context, NODE_BACKGROUND[status]));
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
    }

    private void registerStatus(final int status){
        if(getActivity()!=null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ll_registration_node.setBackground(ContextCompat.getDrawable(parent_context, NODE_BACKGROUND[status]));
                }
            });
        }
    }

    public void startExtract(){
        new StepRegisterTask(new Runnable() {
            @Override
            public void run() {
                if(staff.getPhoto1()!=null){
                    Bitmap staffPhoto = BitmapFactory.decodeByteArray(staff.getPhoto1(), 0, staff.getPhoto1().length);

                    int newSize = staffPhoto.getWidth() < staffPhoto.getHeight() ? staffPhoto.getWidth() : staffPhoto.getHeight();

                    iv_staff_photo.setImageBitmap(Bitmap.createBitmap(staffPhoto, 0, 0, newSize, newSize));
                }else{
                    iv_staff_photo.setImageResource(R.drawable.ic_person_black_48dp);
                }

                tv_staff_num.setText(staff.getStaff_num());
                tv_staff_desc.setText(staff.getStaff_desc());

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
                    new Thread().sleep(5000);
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

    public void setStaff(Staff staff){
        this.staff = staff;
    }

    public void setFingerSelected(int finger_selected){
        this.finger_selected = finger_selected;
    }

    public void setAuditFactory(AuditFactory auditFactory){
        this.audit_factory = auditFactory;
    }

    public void setStaffFactory(StaffFactory staffFactory){
        this.staff_factory = staffFactory;
    }

    public void setSelectFingerFragment(SelectFingerFragment selectFingerFragment) {
        this.select_finger_fragment = selectFingerFragment;
    }
}
