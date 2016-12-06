package com.globe3.tno.g3_mobile.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.view_objects.RecentActivity;
import com.globe3.tno.g3_mobile.view_objects.RowStaff;

import java.util.ArrayList;

public class RecentActivityListAdapter extends RecyclerView.Adapter<RecentActivityListAdapter.ViewHolder> {
    Context parent_context;

    private ArrayList<RecentActivity> recent_activity_list;

    public RecentActivityListAdapter(ArrayList<RecentActivity> recent_activity_list, Context parentContext) {
        this.parent_context = parentContext;
        this.recent_activity_list = recent_activity_list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_icon;
        public TextView tv_action_type;
        public TextView tv_action_time;
        public TextView tv_staff_num;
        public TextView tv_staff_desc;

        public ViewHolder(View view) {
            super(view);
            iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            tv_action_type = (TextView) view.findViewById(R.id.tv_action_type);
            tv_action_time = (TextView) view.findViewById(R.id.tv_action_time);
            tv_staff_num = (TextView) view.findViewById(R.id.tv_staff_num);
            tv_staff_desc = (TextView) view.findViewById(R.id.tv_staff_desc);
        }
    }

    public void add(int position, RecentActivity recentActivity) {
        recent_activity_list.add(position, recentActivity);
        notifyItemInserted(position);
    }

    public void remove(RecentActivity recentActivity) {
        int position = recent_activity_list.indexOf(recentActivity);
        recent_activity_list.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public RecentActivityListAdapter.ViewHolder onCreateViewHolder(ViewGroup parentView, int viewType) {
        View rowView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.row_recent_activity, parentView, false);
        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        RecentActivity recentActivity = recent_activity_list.get(position);

        viewHolder.iv_icon.setImageResource(recentActivity.getIcon());
        viewHolder.iv_icon.setColorFilter(ContextCompat.getColor(parent_context, recentActivity.getIconColor()));
        viewHolder.tv_action_type.setText(recentActivity.getType());
        viewHolder.tv_action_time.setText(recentActivity.getTime());
        viewHolder.tv_staff_num.setText(recentActivity.getStaffNum());
        viewHolder.tv_staff_desc.setText(recentActivity.getStaffName());
    }

    @Override
    public int getItemCount() {
        return recent_activity_list.size();
    }
}
