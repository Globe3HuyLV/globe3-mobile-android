package com.globe3.tno.g3_mobile.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.GridView;

import com.globe3.tno.g3_mobile.adapters.ProjectPhotoItemViewGridAdapter;
import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.view_objects.GridItemProjectPhotoItem;

import java.util.ArrayList;

public class ProjectPhotoItemViewActivity extends BaseActivity {
    ProjectPhotoItemViewActivity project_photo_item_view_activity;
    ArrayList<GridItemProjectPhotoItem> project_photo_item_list;

    ActionBar action_bar;
    Drawable up_arrow;

    GridView gv_project_photo_items;
    FloatingActionButton fab_project_photo_item_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_project_photo_item_view);
        super.onCreate(savedInstanceState);

        project_photo_item_view_activity = this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    public void onActivityLoading(){
        gv_project_photo_items = (GridView) findViewById(R.id.gv_project_photo_items);
        fab_project_photo_item_add = (FloatingActionButton) findViewById(R.id.fab_project_photo_item_add);

        action_bar = getSupportActionBar();

        project_photo_item_list = new ArrayList<>();
        for(int i=2001; i<2016; i++){
            GridItemProjectPhotoItem gridItemProjectPhotoItem = new GridItemProjectPhotoItem();

            int rawResource = 0;
            switch (i%4){
                case 1:
                    rawResource = R.drawable.sample_2;
                    break;
                case 2:
                    rawResource = R.drawable.sample_3;
                    break;
                case 3:
                    rawResource = R.drawable.sample_4;
                    break;
                default:
                    rawResource = R.drawable.sample_1;
                    break;
            }

            Bitmap thumbnailRaw = BitmapFactory.decodeResource(project_photo_item_view_activity.getResources(), rawResource);

            int newSize = thumbnailRaw.getWidth() < thumbnailRaw.getHeight() ? thumbnailRaw.getWidth() : thumbnailRaw.getHeight();

            gridItemProjectPhotoItem.setThumbnail(Bitmap.createBitmap(thumbnailRaw, 0, 0, newSize, newSize));

            project_photo_item_list.add(gridItemProjectPhotoItem);
        }

        project_photo_item_view_activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gv_project_photo_items.setAdapter(new ProjectPhotoItemViewGridAdapter(project_photo_item_view_activity, project_photo_item_list));
            }
        });
    }

    public void onActivityReady(){
        if(action_bar != null){
            action_bar.setDisplayHomeAsUpEnabled(true);
            if(getResources().getResourceName(R.drawable.abc_ic_ab_back_mtrl_am_alpha) != null){
                up_arrow = ContextCompat.getDrawable(project_photo_item_view_activity, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
                up_arrow.setColorFilter(ContextCompat.getColor(project_photo_item_view_activity, R.color.colorMenuDark), PorterDuff.Mode.SRC_ATOP);
                action_bar.setTitle("REF1001");
                action_bar.setHomeAsUpIndicator(up_arrow);
            }
        }
    }
}
