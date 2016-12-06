package com.globe3.tno.g3_mobile.async;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.globe3.tno.g3_mobile.BuildConfig;
import com.globe3.tno.g3_mobile.app_objects.LogItem;
import com.globe3.tno.g3_mobile.util.DateUtility;
import com.globe3.tno.g3_mobile.util.HttpUtility;
import com.globe3.tno.g3_mobile.R;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.globe3.tno.g3_mobile.constants.App.APP_NAME;
import static com.globe3.tno.g3_mobile.constants.App.GLOBE3_WEBSERVICE_ADDR;
import static com.globe3.tno.g3_mobile.constants.App.GLOBE3_WEBSERVICE_PATH;

public class AppUpdate extends AsyncTask<Void, Void, Boolean>
{
    private Activity activity;
    private Runnable ignore_update;

    private String latest_version;
    private String apk_directory;
    private String apk_filename;
    private String web_func;

    public AppUpdate(Activity activity, Runnable ignoreUpdate){
        web_func = "app_update";
        this.activity = activity;
        this.ignore_update = ignoreUpdate;
    }
    protected Boolean doInBackground(Void... param) {
        try {
            JSONObject json = HttpUtility.requestJSON(web_func, "");

            if(json!=null){
                latest_version = json.getString("latest_version");
                apk_directory = json.getString("apk_path");
                apk_filename = json.getString("apk_filename");
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if(success){
            try{
                final String buildDate = DateUtility.getDateString(new Date(BuildConfig.TIMESTAMP), "yyyy-MM-dd HH:mm:ss");

                Date appCurVersion = DateUtility.getStringDate(buildDate, "yyyy-MM-dd HH:mm:ss");
                Date appLatestVersion = DateUtility.getStringDate(latest_version, "yyyy-MM-dd HH:mm:ss");

                Log.i(APP_NAME, "buildDate"+buildDate);
                Log.i(APP_NAME, "latest_version"+latest_version);

                if(appCurVersion.getTime() < appLatestVersion.getTime()){
                    final Runnable doUpdate = new Runnable() {
                        @Override
                        public void run() {
                            String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
                            String appFileName = apk_filename;
                            destination += appFileName;
                            final Uri uri = Uri.parse("file://" + destination);

                            File file = new File(destination);
                            if (file.exists())
                            {
                                file.delete();
                            }
                            String url = GLOBE3_WEBSERVICE_ADDR.replace("/"+GLOBE3_WEBSERVICE_PATH, "") + apk_directory + appFileName;

                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                            request.setDescription(activity.getString(R.string.app_name));
                            request.setTitle(activity.getString(R.string.app_name));

                            ProgressDialog downloadProgressDialog = ProgressDialog.show(activity, "", activity.getString(R.string.msg_downloading_updates), true);
                            downloadProgressDialog.setCancelable(false);

                            request.setDestinationUri(uri);

                            final DownloadManager manager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
                            final long downloadId = manager.enqueue(request);

                            BroadcastReceiver onComplete = new BroadcastReceiver() {
                                public void onReceive(Context context, Intent intent) {
                                    Intent install = new Intent(Intent.ACTION_VIEW);
                                    install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    install.setDataAndType(uri,manager.getMimeTypeForDownloadedFile(downloadId));
                                    activity.startActivity(install);

                                    activity.unregisterReceiver(this);
                                    activity.finish();
                                }
                            };

                            activity.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                        }
                    };

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(activity)
                                .setCancelable(false)
                                .setTitle(activity.getString(R.string.label_update))
                                .setMessage(activity.getString(R.string.msg_update_available))
                                .setPositiveButton(activity.getString(R.string.label_update), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        doUpdate.run();
                                    }
                                })
                                .setNegativeButton(activity.getString(R.string.label_ignore), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ignore_update.run();
                                    }
                                })
                                .show();
                        }
                    });
                }else{
                    ignore_update.run();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            ignore_update.run();
        }
    }
}
