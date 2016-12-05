package com.globe3.tno.g3_mobile.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.view_objects.RowStaff;

import java.util.ArrayList;

public class TimesheetStaffListAdapter extends RecyclerView.Adapter<TimesheetStaffListAdapter.ViewHolder> {
    Context parent_context;

    private ArrayList<RowStaff> staff_list;

    public TimesheetStaffListAdapter(ArrayList<RowStaff> staffList, Context parentContext) {
        this.parent_context = parentContext;
        this.staff_list = staffList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout rl_row_staff;
        public ImageView iv_staff_photo;
        public TextView tv_staff_id;
        public TextView tv_staff_name;
        public View v_bottom_spacer;

        public ViewHolder(View view) {
            super(view);
            rl_row_staff = (RelativeLayout) view.findViewById(R.id.rl_row_staff);
            iv_staff_photo = (ImageView) view.findViewById(R.id.iv_staff_photo);
            tv_staff_id = (TextView) view.findViewById(R.id.tv_staff_id);
            tv_staff_name = (TextView) view.findViewById(R.id.tv_staff_name);
            v_bottom_spacer = view.findViewById(R.id.v_bottom_spacer);
        }
    }

    public void add(int position, RowStaff staff) {
        staff_list.add(position, staff);
        notifyItemInserted(position);
    }

    public void remove(RowStaff staff) {
        int position = staff_list.indexOf(staff);
        staff_list.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public TimesheetStaffListAdapter.ViewHolder onCreateViewHolder(ViewGroup parentView, int viewType) {
        View rowView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.row_timesheet_staff, parentView, false);
        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        RowStaff rowStaff = staff_list.get(position);
        viewHolder.rl_row_staff.setOnClickListener(rowStaff.getOnClickListener());
        if(rowStaff.getStaffPhoto()!=null){
            viewHolder.iv_staff_photo.setImageBitmap(rowStaff.getStaffPhoto());
        }else{
            viewHolder.iv_staff_photo.setImageResource(R.drawable.ic_person_black_48dp);
        }
        viewHolder.tv_staff_id.setText(rowStaff.getStaffCode());
        viewHolder.tv_staff_name.setText(rowStaff.getStaffName());
        viewHolder.v_bottom_spacer.setVisibility(rowStaff.isDisplayBottomSpacer() ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return staff_list.size();
    }
}
