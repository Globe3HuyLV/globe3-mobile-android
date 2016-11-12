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
    private Activity parentActivity;
    private final ArrayList<GridItemProjectPhoto> projectPhotoList;

    public ProjectPhotoGridAdapter(Activity parentActivity, ArrayList<GridItemProjectPhoto> projectPhotoList) {
        this.parentActivity = parentActivity;
        this.projectPhotoList = projectPhotoList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) parentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = (convertView == null ? inflater.inflate(R.layout.grid_item_project_photo, null) : convertView);

        ImageView grid_item_image = (ImageView) convertView.findViewById(R.id.grid_item_image);
        TextView tv_reference = (TextView) convertView.findViewById(R.id.tv_reference);
        TextView tv_remarks = (TextView) convertView.findViewById(R.id.tv_remarks);
        TextView tv_photos_count = (TextView) convertView.findViewById(R.id.tv_photos_count);

        grid_item_image.setImageBitmap(projectPhotoList.get(position).getThumbnail());
        tv_reference.setText(projectPhotoList.get(position).getReference());
        tv_remarks.setText(projectPhotoList.get(position).getRemarks());
        tv_photos_count.setText(String.valueOf(projectPhotoList.get(position).getPhotosCount()));
        return convertView;
    }

    @Override
    public int getCount() {
        return projectPhotoList.size();
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
