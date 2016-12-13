package com.globe3.tno.g3_mobile.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.app_objects.LogItem;
import com.globe3.tno.g3_mobile.app_objects.Project;
import com.globe3.tno.g3_mobile.app_objects.ProjectPhotoItem;
import com.globe3.tno.g3_mobile.app_objects.factory.AuditFactory;
import com.globe3.tno.g3_mobile.app_objects.factory.ProjectFactory;
import com.globe3.tno.g3_mobile.constants.TagTableUsage;
import com.globe3.tno.g3_mobile.util.FileUtility;
import com.globe3.tno.g3_mobile.util.HttpUtility;
import com.globe3.tno.g3_mobile.util.Uniquenum;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.globe3.tno.g3_mobile.constants.App.APP_NAME;
import static com.globe3.tno.g3_mobile.globals.Globals.CFSQLFILENAME;
import static com.globe3.tno.g3_mobile.globals.Globals.COMPANYFN;
import static com.globe3.tno.g3_mobile.globals.Globals.MASTERFN;
import static com.globe3.tno.g3_mobile.globals.Globals.USERLOGINID;

public class ProjectPhotoAddFragment extends DialogFragment {
    Context parent_context;

    ProjectFactory project_factory;

    Project project;

    TextView tv_project_code;
    TextView tv_project_desc;
    TextView tv_selected_photos_count;
    TextView tv_upload_progress;
    EditText et_ref_num;
    EditText et_remarks;
    TextView tv_upload;
    TextView tv_cancel;

    String upload_uniquenum;
    String reference;
    String remarks;

    private ArrayList<String> image_urls;

    private ArrayList<ProjectPhotoUploadTask> upload_task_list;

    View.OnClickListener dismiss = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View projectPhotoAddFragment = inflater.inflate(R.layout.fragment_project_photo_add, viewGroup, false);
        parent_context = projectPhotoAddFragment.getContext();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        project_factory = new ProjectFactory(parent_context);

        tv_project_code = (TextView) projectPhotoAddFragment.findViewById(R.id.tv_project_code);
        tv_project_desc = (TextView) projectPhotoAddFragment.findViewById(R.id.tv_project_desc);
        tv_selected_photos_count = (TextView) projectPhotoAddFragment.findViewById(R.id.tv_selected_photos_count);
        tv_upload_progress = (TextView) projectPhotoAddFragment.findViewById(R.id.tv_upload_progress);
        et_ref_num = (EditText) projectPhotoAddFragment.findViewById(R.id.et_ref_num);
        et_remarks = (EditText) projectPhotoAddFragment.findViewById(R.id.et_remarks);
        tv_upload = (TextView) projectPhotoAddFragment.findViewById(R.id.tv_upload);
        tv_cancel = (TextView) projectPhotoAddFragment.findViewById(R.id.tv_cancel);

        tv_project_code.setText(project.getCode());
        tv_project_desc.setText(project.getDesc());

        tv_selected_photos_count.setText(String.format(getString(R.string.msg_num_photos_selected), String.valueOf(image_urls.size())));

        upload_task_list = new ArrayList<>();
        for(int i=0; i<image_urls.size(); i++){
            upload_task_list.add(new ProjectPhotoUploadTask(i));
        }

        tv_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_uniquenum = Uniquenum.Generate();
                reference = et_ref_num.getText().toString();
                remarks = et_remarks.getText().toString();

                if(upload_task_list.size()>0){
                    tv_selected_photos_count.setVisibility(View.GONE);
                    tv_upload_progress.setVisibility(View.VISIBLE);
                    tv_upload_progress.setText(String.format(getString(R.string.msg_uploading_num_of_total), "1", String.valueOf(image_urls.size())));

                    et_ref_num.setEnabled(false);
                    et_remarks.setEnabled(false);

                    tv_upload.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorMenuLight));
                    tv_cancel.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorMenuLight));

                    tv_upload.setOnClickListener(null);
                    tv_cancel.setOnClickListener(null);

                    project_factory.openRepo();
                    upload_task_list.get(0).execute();
                }
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return projectPhotoAddFragment;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setImageUrls(ArrayList<String> image_urls) {
        this.image_urls = image_urls;
    }

    public class ProjectPhotoUploadTask extends AsyncTask<Void, Void, Boolean> {
        private int image_url_index;


        public ProjectPhotoUploadTask(int image_url_index){
            this.image_url_index = image_url_index;
        }

        @Override
        protected void onPreExecute() {
            tv_upload_progress.setText(String.format(getString(R.string.msg_uploading_num_of_total), String.valueOf(image_url_index), String.valueOf(image_urls.size())));
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                LogItem logItem = new AuditFactory(parent_context).Log(TagTableUsage.PROJECT_PHOTO_UPLOAD);

                int row_item_num = image_url_index + 1;

                HashMap<String,String> detail = new HashMap<>();
                detail.put("cfsqlfilename", CFSQLFILENAME);
                detail.put("masterfn", MASTERFN);
                detail.put("companyfn", COMPANYFN);
                detail.put("userloginid", USERLOGINID);
                detail.put("project_unique", project.getUniquenumPri());
                detail.put("project_code", project.getCode());
                detail.put("project_desc", project.getDesc());
                detail.put("reference", reference);
                detail.put("remarks", remarks);
                detail.put("photo_unique", upload_uniquenum);
                detail.put("row_item_num", String.valueOf(row_item_num));
                detail.put("photo", FileUtility.getBase64(image_urls.get(image_url_index)));

                String param = HttpUtility.hashMapToUrl(detail);

                JSONObject uploadResultJSON = HttpUtility.requestJSON("project_photo_upload", param);
                if(uploadResultJSON != null){
                    ProjectPhotoItem projectPhotoItem = new ProjectPhotoItem();
                    projectPhotoItem.setUniquenumPri(upload_uniquenum);
                    projectPhotoItem.setUniquenumSec(Uniquenum.Generate());
                    projectPhotoItem.setProject(project);
                    projectPhotoItem.setDate_post(logItem.getLogDate());
                    projectPhotoItem.setRowItemNum(row_item_num);
                    projectPhotoItem.setReferenceNum(reference);
                    projectPhotoItem.setRemarks(remarks);
                    projectPhotoItem.setPhoto(FileUtility.getFileBlob(image_urls.get(image_url_index)));

                    project_factory.saveProjectPhoto(projectPhotoItem, logItem);

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
        protected void onPostExecute(final Boolean uploadSuccess) {
            if(uploadSuccess){
                image_url_index++;
                if(image_url_index==image_urls.size()){
                    tv_upload_progress.setText(String.format(getString(R.string.msg_upload_success)));
                    tv_upload_progress.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorSuccess));

                    tv_upload.setText(getString(R.string.label_finish));
                    tv_upload.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccentLight));
                    tv_upload.setOnClickListener(dismiss);

                    tv_cancel.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlueGrey));
                    tv_cancel.setOnClickListener(dismiss);

                    project_factory.closeRepo();
                }else{
                    upload_task_list.get(image_url_index).execute();
                }
            }else{
                tv_upload_progress.setText(String.format(getString(R.string.msg_upload_failed)));
                tv_upload_progress.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorFailed));

                tv_upload.setText(getString(R.string.label_finish));
                tv_upload.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccentLight));
                tv_upload.setOnClickListener(dismiss);

                tv_cancel.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlueGrey));
                tv_cancel.setOnClickListener(dismiss);

                project_factory.closeRepo();
            }
        }
    }
}
