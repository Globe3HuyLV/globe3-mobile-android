package com.globe3.tno.g3_mobile.util;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.support.v4.app.ActivityCompat;
import android.view.Window;

import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.constants.App;

public class PermissionUtility {
    public static void requestWriteStorage(final Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    android.app.AlertDialog.Builder alertBuilder = new android.app.AlertDialog.Builder(activity)
                            .setCancelable(false)
                            .setMessage(activity.getString(R.string.msg_failed_to_grant_storage_permission))
                            .setPositiveButton(activity.getString(R.string.label_exit), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    activity.finish();
                                }
                            })
                            .setNegativeButton(activity.getString(R.string.label_retry), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, App.REQUEST_WRITE_EXTERNAL_STORAGE);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert);

                    android.app.AlertDialog storagePermission = alertBuilder.create();
                    storagePermission.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    storagePermission.show();
                }
            });

        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, App.REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    public static void requestLocationServices(final Activity activity, boolean doRequest) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION ) && !doRequest) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    android.app.AlertDialog.Builder alertBuilder = new android.app.AlertDialog.Builder(activity)
                            .setCancelable(false)
                            .setMessage(activity.getString(R.string.msg_failed_to_grant_gps_permission))
                            .setPositiveButton(activity.getString(R.string.label_cancel), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton(activity.getString(R.string.label_retry), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, App.REQUEST_GPS);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert);

                    android.app.AlertDialog gpsPermission = alertBuilder.create();
                    gpsPermission.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    gpsPermission.show();
                }
            });
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, App.REQUEST_GPS);
        }
    }

    public static void requestCamera(final Activity activity, boolean doRequest) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA ) && !doRequest) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    android.app.AlertDialog.Builder alertBuilder = new android.app.AlertDialog.Builder(activity)
                            .setCancelable(false)
                            .setMessage(activity.getString(R.string.msg_failed_to_grant_camera_permission))
                            .setPositiveButton(activity.getString(R.string.label_cancel), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton(activity.getString(R.string.label_retry), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, App.REQUEST_CAMERA);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert);

                    android.app.AlertDialog cameraPermission = alertBuilder.create();
                    cameraPermission.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    cameraPermission.show();
                }
            });
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, App.REQUEST_CAMERA);
        }
    }
}
