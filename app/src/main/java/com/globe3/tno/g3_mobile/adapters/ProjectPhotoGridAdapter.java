package com.globe3.tno.g3_mobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.view_objects.GridItemProjectPhoto;

import java.util.ArrayList;

public class ProjectPhotoGridAdapter extends BaseAdapter {
    private Activity parent_activity;
    private final ArrayList<GridItemProjectPhoto> project_photo_list;

    public ProjectPhotoGridAdapter(Activity parentActivity, ArrayList<GridItemProjectPhoto> projectPhotoList) {
        this.parent_activity = parentActivity;
        this.project_photo_list = projectPhotoList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) parent_activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = (convertView == null ? inflater.inflate(R.layout.grid_item_project_photo, null) : convertView);

        ImageView grid_item_image = (ImageView) convertView.findViewById(R.id.grid_item_image);
        TextView tv_reference = (TextView) convertView.findViewById(R.id.tv_reference);
        TextView tv_remarks = (TextView) convertView.findViewById(R.id.tv_remarks);
        TextView tv_photos_count = (TextView) convertView.findViewById(R.id.tv_photos_count);

        grid_item_image.setImageBitmap(project_photo_list.get(position).getThumbnail());
        tv_reference.setText(project_photo_list.get(position).getReference());
        tv_remarks.setText(project_photo_list.get(position).getRemarks());
        tv_photos_count.setText(String.valueOf(project_photo_list.get(position).getPhotosCount()));
        return convertView;
    }

    @Override
    public int getCount() {
        return project_photo_list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
