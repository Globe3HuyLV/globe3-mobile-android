package com.globe3.tno.g3_mobile.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.widget.GridView;

import com.globe3.tno.g3_mobile.adapters.ProjectPhotoItemViewGridAdapter;
import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.view_objects.GridItemProjectPhotoItem;

import java.util.ArrayList;

public class ProjectPhotoItemViewActivity extends BaseActivity {
    ProjectPhotoItemViewActivity projectPhotoItemViewActivity;
    ArrayList<GridItemProjectPhotoItem> projectPhotoItemList;

    Drawable upArrow;
    GridView gv_project_photo_items;
    FloatingActionButton fab_project_photo_item_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_project_photo_item_view);
        super.onCreate(savedInstanceState);
    }

    public void onActivityLoading(){
        Thread thread = new Thread();
        try {
            projectPhotoItemViewActivity = this;

            gv_project_photo_items = (GridView) findViewById(R.id.gv_project_photo_items);
            fab_project_photo_item_add = (FloatingActionButton) findViewById(R.id.fab_project_photo_item_add);

            if(getSupportActionBar() != null){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                if(getResources().getResourceName(R.drawable.abc_ic_ab_back_mtrl_am_alpha) != null){
                    upArrow = ContextCompat.getDrawable(projectPhotoItemViewActivity, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
                    upArrow.setColorFilter(ContextCompat.getColor(projectPhotoItemViewActivity, R.color.colorMenuDark), PorterDuff.Mode.SRC_ATOP);
                    getSupportActionBar().setTitle("REF1001");
                    getSupportActionBar().setHomeAsUpIndicator(upArrow);
                }
            }

            projectPhotoItemList = new ArrayList<>();
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

                Bitmap thumbnailRaw = BitmapFactory.decodeResource(projectPhotoItemViewActivity.getResources(), rawResource);

                int newSize = thumbnailRaw.getWidth() < thumbnailRaw.getHeight() ? thumbnailRaw.getWidth() : thumbnailRaw.getHeight();

                gridItemProjectPhotoItem.setThumbnail(Bitmap.createBitmap(thumbnailRaw, 0, 0, newSize, newSize));

                projectPhotoItemList.add(gridItemProjectPhotoItem);
            }

            projectPhotoItemViewActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gv_project_photo_items.setAdapter(new ProjectPhotoItemViewGridAdapter(projectPhotoItemViewActivity, projectPhotoItemList));
                }
            });

            thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onActivityReady(){
    }
}
