package com.globe3.tno.g3_mobile.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.globe3.tno.g3_mobile.view_objects.RowProject;
import com.globe3.tno.g3_mobile.R;

import java.util.ArrayList;

public class PhotoProjectListAdapter extends RecyclerView.Adapter<PhotoProjectListAdapter.ViewHolder> {
    Context parentContext;

    private ArrayList<RowProject> projectList;

    public PhotoProjectListAdapter(ArrayList<RowProject> projectList, Context parentContext) {
        this.parentContext = parentContext;
        this.projectList = projectList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout rl_row_project;
        public TextView tv_project_code;
        public TextView tv_project_name;
        public TextView tv_project_photo_count;

        public ViewHolder(View view) {
            super(view);
            rl_row_project = (RelativeLayout) view.findViewById(R.id.rl_row_project);
            tv_project_code = (TextView) view.findViewById(R.id.tv_project_code);
            tv_project_name = (TextView) view.findViewById(R.id.tv_project_name);
            tv_project_photo_count = (TextView) view.findViewById(R.id.tv_project_photo_count);
        }
    }

    public void add(int position, RowProject project) {
        projectList.add(position, project);
        notifyItemInserted(position);
    }

    public void remove(RowProject project) {
        int position = projectList.indexOf(project);
        projectList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public PhotoProjectListAdapter.ViewHolder onCreateViewHolder(ViewGroup parentView, int viewType) {
        View rowView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.row_photo_project, parentView, false);
        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        RowProject rowProject = projectList.get(position);
        viewHolder.rl_row_project.setOnClickListener(rowProject.getOnClickListener());
        viewHolder.tv_project_code.setText(rowProject.getProjectCode());
        viewHolder.tv_project_name.setText(rowProject.getProjectName());
        viewHolder.tv_project_photo_count.setText(String.valueOf(rowProject.getProjectPhotosCount()));
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }
}
