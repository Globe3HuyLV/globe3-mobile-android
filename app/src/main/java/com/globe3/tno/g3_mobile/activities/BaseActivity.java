package com.globe3.tno.g3_mobile.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.globe3.tno.g3_mobile.R;

public abstract class BaseActivity extends AppCompatActivity {
    public BaseActivity baseActivity;

    public RelativeLayout layout_base_loader;
    public ImageView image_loader;
    public RelativeLayout layout_main;

    public BaseActivity(){
        super();
        baseActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout_base_loader = (RelativeLayout) findViewById(R.id.layout_base_loader);
        image_loader = (ImageView) findViewById(R.id.image_loader);
        layout_main = (RelativeLayout) findViewById(R.id.layout_main);

        LoaderTask loaderTask = new LoaderTask();
        loaderTask.execute();
    }

    public abstract void onActivityLoading();
    public abstract void onActivityReady();

    public void onActivityPostLoading(){
        layout_base_loader.setVisibility(View.INVISIBLE);
        layout_main.setAnimation(AnimationUtils.loadAnimation(baseActivity, R.anim.animate_fade_in));
        layout_main.getAnimation().setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layout_main.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        layout_main.startAnimation(layout_main.getAnimation());
        baseActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onActivityReady();
            }
        });
    }

    public void onActivityError(){
        AlertDialog.Builder alertDialogBuilder =
                new AlertDialog.Builder(this)
                        .setMessage(baseActivity.getString(R.string.msg_an_error_has_occured))
                        .setNegativeButton(baseActivity.getString(R.string.label_exit), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                finish();
                            }
                        });
        alertDialogBuilder.show();
    }

    private class LoaderTask extends AsyncTask<Void, Void, Boolean> {
        protected void onPreExecute(){
            layout_base_loader.setVisibility(View.VISIBLE);
            image_loader.setAnimation(AnimationUtils.loadAnimation(baseActivity, R.anim.animate_rotate_clockwise));
            layout_main.setVisibility(View.INVISIBLE);
        }

        protected Boolean doInBackground(Void... params) {
            try{
                onActivityLoading();
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
        protected void onPostExecute(Boolean loadSuccess) {
            if(loadSuccess){
                onActivityPostLoading();
            }else{
                onActivityError();
            }
        }
    }
}
