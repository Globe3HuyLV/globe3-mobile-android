package com.globe3.tno.g3_mobile.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.DialogFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.globe3.tno.g3_mobile.R;

public class RegisterFingerFragment extends DialogFragment {
    int sim_num = 1;

    Context parentContext;

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
    TextView tv_refresh_scanner;

    final static int NODE_INACTIVE = 0;
    final static int NODE_ACTIVE = 1;
    final static int NODE_SUCCESS = 2;

    final static int[] NODE_BACKGROUND = {R.drawable.stepper_node_inactive, R.drawable.stepper_node_active, R.drawable.stepper_node_success};
    final static int[] NODE_NUM_DISPLAY = {View.VISIBLE, View.VISIBLE, View.GONE};
    final static int[] NODE_CHECK_DISPLAY = {View.GONE, View.GONE, View.VISIBLE};
    final static int[] LINE_DISPLAY = {View.GONE, View.GONE, View.VISIBLE};

    final static int PROMPT_CONNECTING = 0;
    final static int PROMPT_PLACE_FINGER_SCAN = 1;
    final static int PROMPT_LIFT_FINGER_SCAN = 2;
    final static int PROMPT_SUCCESS = 3;

    final static int[] PROMPT_TEXT = {R.string.msg_connecting_to_scanner, R.string.msg_place_finger_scanner, R.string.msg_lift_finger_scan_again, R.string.msg_registration_success};
    final static int[] PROMPT_TEXT_COLOR = {R.color.colorMenuLight, R.color.colorAccentLight, R.color.colorAccentLight, R.color.colorSuccess};
    final static int[] LOADER_DISPLAY = {View.VISIBLE, View.GONE, View.GONE, View.GONE};
    final static int[] FINGER_DISPLAY = {View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE};
    final static int[] FINGER_COLOR = {R.color.colorMenuLight, R.color.colorAccentLight, R.color.colorAccentLight, R.color.colorSuccess};
    final static int[] REFRESH_TEXT = {R.string.label_refresh_scanner, R.string.label_refresh_scanner, R.string.label_refresh_scanner, R.string.label_finish};
    final static int[] REFRESH_TEXT_COLOR = {R.color.colorMenuLight, R.color.colorAccentLight, R.color.colorAccentLight, R.color.colorSuccess};
    final static boolean[] REFRESH_CLICKABLE = {false, true, true, true};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
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
        tv_refresh_scanner = (TextView) registerFragment.findViewById(R.id.tv_refresh_scanner);

        Animation loader_rotate = AnimationUtils.loadAnimation(parentContext, R.anim.rotate);
        iv_loader.startAnimation(loader_rotate);

        simulate();

        return registerFragment;
    }

    private void setPrompt(int status){
        tv_prompt.setText(getText(PROMPT_TEXT[status]));
        tv_prompt.setTextColor(ContextCompat.getColor(parentContext, PROMPT_TEXT_COLOR[status]));
        iv_loader.setVisibility(LOADER_DISPLAY[status]);
        iv_finger.setVisibility(FINGER_DISPLAY[status]);
        iv_finger.setColorFilter(ContextCompat.getColor(parentContext, FINGER_COLOR[status]));
        if(status!=PROMPT_CONNECTING){
            iv_loader.clearAnimation();
        }
        tv_refresh_scanner.setText(getText(REFRESH_TEXT[status]));
        tv_refresh_scanner.setTextColor(ContextCompat.getColor(parentContext, REFRESH_TEXT_COLOR[status]));
        tv_refresh_scanner.setClickable(REFRESH_CLICKABLE[status]);
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

    private void extractStatus(final int status){
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

    private void verifyStatus(final int status){
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

    private void registerStatus(final int status){
        ll_registration_node.setBackground(ContextCompat.getDrawable(parentContext, NODE_BACKGROUND[status]));
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
                    new Thread().sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, new Runnable() {
            @Override
            public void run() {
                extractStatus(NODE_ACTIVE);
                verifyStatus(NODE_INACTIVE);
                registerStatus(NODE_INACTIVE);

                setPrompt(PROMPT_PLACE_FINGER_SCAN);
            }
        }).execute();
    }

    public void simulate(){
        if(sim_num==1){
            startExtract();
        }

        if(sim_num==2){
            extractStatus(NODE_SUCCESS);
        }

        if(sim_num==3){
            verifyStatus(NODE_SUCCESS);
        }
        sim_num++;
    }

    public void finishRegistration(){
        dismiss();
    }
}
