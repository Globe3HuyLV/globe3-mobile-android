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

public class LogTimeProjectListAdapter extends RecyclerView.Adapter<LogTimeProjectListAdapter.ViewHolder> {
    Context parent_context;

    private ArrayList<RowProject> project_list;

    public LogTimeProjectListAdapter(ArrayList<RowProject> projectList, Context parentContext) {
        this.parent_context = parentContext;
        this.project_list = projectList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout rl_row_project;
        public TextView tv_project_code;
        public TextView tv_project_name;

        public ViewHolder(View view) {
            super(view);
            rl_row_project = (RelativeLayout) view.findViewById(R.id.rl_row_project);
            tv_project_code = (TextView) view.findViewById(R.id.tv_project_code);
            tv_project_name = (TextView) view.findViewById(R.id.tv_project_name);
        }
    }

    public void add(int position, RowProject project) {
        project_list.add(position, project);
        notifyItemInserted(position);
    }

    public void remove(RowProject project) {
        int position = project_list.indexOf(project);
        project_list.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public LogTimeProjectListAdapter.ViewHolder onCreateViewHolder(ViewGroup parentView, int viewType) {
        View rowView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.row_log_time_project, parentView, false);
        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        RowProject rowProject = project_list.get(position);
        viewHolder.rl_row_project.setOnClickListener(rowProject.getOnClickListener());
        viewHolder.tv_project_code.setText(rowProject.getProjectCode());
        viewHolder.tv_project_name.setText(rowProject.getProjectName());

    }

    @Override
    public int getItemCount() {
        return project_list.size();
    }
}
