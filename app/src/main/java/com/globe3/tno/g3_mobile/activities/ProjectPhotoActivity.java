package com.globe3.tno.g3_mobile.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;

import com.globe3.tno.g3_mobile.adapters.ProjectPhotoGridAdapter;
import com.globe3.tno.g3_mobile.app_objects.Project;
import com.globe3.tno.g3_mobile.fragments.ProjectPhotoAddFragment;
import com.globe3.tno.g3_mobile.view_objects.GridItemProjectPhoto;
import com.globe3.tno.g3_mobile.R;

import java.util.ArrayList;

import static com.globe3.tno.g3_mobile.constants.App.ACTIVITY_RESULT_SELECT_PHOTOS;
import static com.globe3.tno.g3_mobile.constants.App.APP_NAME;

public class ProjectPhotoActivity extends BaseActivity {
    ProjectPhotoActivity project_photo_activity;
    ArrayList<GridItemProjectPhoto> project_photo_list;

    Project project;

    ActionBar action_bar;
    Drawable up_arrow;

    GridView gv_project_photos;
    FloatingActionButton fab_project_photo_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_project_photo);
        super.onCreate(savedInstanceState);

        project_photo_activity = this;
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
        Bundle projectBundle = getIntent().getExtras();
        if(projectBundle != null){
            project = (Project) projectBundle.getSerializable("project");
        }

        gv_project_photos = (GridView) findViewById(R.id.gv_project_photos);
        fab_project_photo_add = (FloatingActionButton) findViewById(R.id.fab_project_photo_add);

        action_bar = getSupportActionBar();

        project_photo_list = new ArrayList<>();
        for(int i=2001; i<2009; i++){
            GridItemProjectPhoto gridItemProjectPhoto = new GridItemProjectPhoto();
            gridItemProjectPhoto.setReference("REF"+String.valueOf(i));
            gridItemProjectPhoto.setRemarks("Remarks");
            gridItemProjectPhoto.setPhotosCount(i-1990);

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

            Bitmap thumbnailRaw = BitmapFactory.decodeResource(project_photo_activity.getResources(), rawResource);

            int newSize = thumbnailRaw.getWidth() < thumbnailRaw.getHeight() ? thumbnailRaw.getWidth() : thumbnailRaw.getHeight();

            gridItemProjectPhoto.setThumbnail(Bitmap.createBitmap(thumbnailRaw, 0, 0, newSize, newSize));

            project_photo_list.add(gridItemProjectPhoto);
        }
    }

    public void onActivityReady(){
        if(action_bar != null){
            action_bar.setDisplayHomeAsUpEnabled(true);
            if(getResources().getResourceName(R.drawable.abc_ic_ab_back_mtrl_am_alpha) != null){
                up_arrow = ContextCompat.getDrawable(project_photo_activity, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
                up_arrow.setColorFilter(ContextCompat.getColor(project_photo_activity, R.color.colorMenuDark), PorterDuff.Mode.SRC_ATOP);
                action_bar.setTitle(project.getCode());
                action_bar.setHomeAsUpIndicator(up_arrow);
            }
        }

        gv_project_photos.setAdapter(new ProjectPhotoGridAdapter(project_photo_activity, project_photo_list));
        gv_project_photos.setOnScrollListener(new gridViewOnScrollListener());
        fab_project_photo_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectPhotosIntent = new Intent(project_photo_activity, ProjectPhotoSelectActivity.class);
                Bundle projectBundle = new Bundle();
                projectBundle.putSerializable("project", project);
                selectPhotosIntent.putExtras(projectBundle);
                startActivityForResult(selectPhotosIntent, ACTIVITY_RESULT_SELECT_PHOTOS);
            }
        });
    }

    public void goToProjectPhotoItem(View view){
        startActivity(new Intent(project_photo_activity, ProjectPhotoItemViewActivity.class));
    }

    public final class gridViewOnScrollListener implements AbsListView.OnScrollListener {
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (visibleItemCount > 0 && fab_project_photo_add.isShown()){
                fab_project_photo_add.hide();
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState){
            if(scrollState == GridView.SCROLL_AXIS_NONE || !fab_project_photo_add.isShown()){
                fab_project_photo_add.show();
            }
        }
    }
}
