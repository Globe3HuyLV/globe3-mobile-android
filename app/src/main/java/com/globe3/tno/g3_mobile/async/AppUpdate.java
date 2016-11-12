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

import com.globe3.tno.g3_mobile.util.DateUtility;
import com.globe3.tno.g3_mobile.util.HttpUtility;
import com.globe3.tno.g3_mobile.R;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.globe3.tno.g3_mobile.constants.App.GLOBE3_WEBSERVICE_ADDR;
import static com.globe3.tno.g3_mobile.constants.App.GLOBE3_WEBSERVICE_PATH;

public class AppUpdate extends AsyncTask<Void, Void, Boolean>
{
    private Activity activity;
    private Runnable ignoreUpdate;

    private String latestVersion;
    private String apkDirectory;
    private String apkFilename;
    private String web_func;

    public AppUpdate(Activity activity, Runnable ignoreUpdate){
        web_func = "app_update";
        this.activity = activity;
        this.ignoreUpdate = ignoreUpdate;
    }

    @Override
    protected Boolean doInBackground(Void... param) {
        try {
            JSONObject json = HttpUtility.requestJSON(web_func, "");

            if(json!=null){
                latestVersion = json.getString("latest_version");
                apkDirectory = json.getString("apk_path");
                apkFilename = json.getString("apk_filename");
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
                ApplicationInfo appInfo = activity.getPackageManager().getApplicationInfo(activity.getPackageName(), 0);
                String appFile = appInfo.sourceDir;
                long time = new File(appFile).lastModified();

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                final String buildDate = formatter.format(time);

                Date appCurVersion = DateUtility.getStringDate(buildDate, "yyyy-MM-dd");
                Date appLatestVersion = DateUtility.getStringDate(latestVersion, "yyyy-MM-dd");

                if(appCurVersion.getTime() < appLatestVersion.getTime()){
                    final Runnable doUpdate = new Runnable() {
                        @Override
                        public void run() {
                            String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
                            String appFileName = apkFilename;
                            destination += appFileName;
                            final Uri uri = Uri.parse("file://" + destination);

                            File file = new File(destination);
                            if (file.exists())
                            {
                                file.delete();
                            }
                            String url = GLOBE3_WEBSERVICE_ADDR.replace("/"+GLOBE3_WEBSERVICE_PATH, "") + apkDirectory + appFileName;

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
                                        ignoreUpdate.run();
                                    }
                                })
                                .show();
                        }
                    });
                }else{
                    ignoreUpdate.run();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            ignoreUpdate.run();
        }
    }
}
