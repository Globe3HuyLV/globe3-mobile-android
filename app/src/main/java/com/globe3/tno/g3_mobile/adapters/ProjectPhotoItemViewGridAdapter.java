package com.globe3.tno.g3_mobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.view_objects.GridItemProjectPhotoItem;

import java.util.ArrayList;

public class ProjectPhotoItemViewGridAdapter extends BaseAdapter {
    private Activity parentActivity;
    private final ArrayList<GridItemProjectPhotoItem> projectPhotoItemList;

    public ProjectPhotoItemViewGridAdapter(Activity parentActivity, ArrayList<GridItemProjectPhotoItem> projectPhotoItemList) {
        this.parentActivity = parentActivity;
        this.projectPhotoItemList = projectPhotoItemList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) parentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = (convertView == null ? inflater.inflate(R.layout.grid_item_project_photo_item, null) : convertView);

        ImageView grid_item_image = (ImageView) convertView.findViewById(R.id.grid_item_image);

        grid_item_image.setImageBitmap(projectPhotoItemList.get(position).getThumbnail());
        return convertView;
    }

    @Override
    public int getCount() {
        return projectPhotoItemList.size();
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
