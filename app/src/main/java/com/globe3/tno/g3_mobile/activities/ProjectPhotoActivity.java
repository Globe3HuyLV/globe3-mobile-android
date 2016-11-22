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
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;

import com.globe3.tno.g3_mobile.adapters.ProjectPhotoGridAdapter;
import com.globe3.tno.g3_mobile.fragments.ProjectPhotoAddFragment;
import com.globe3.tno.g3_mobile.view_objects.GridItemProjectPhoto;
import com.globe3.tno.g3_mobile.R;

import java.util.ArrayList;

import static com.globe3.tno.g3_mobile.constants.App.ACTIVITY_RESULT_SELECT_PHOTOS;

public class ProjectPhotoActivity extends BaseActivity {
    ProjectPhotoActivity projectPhotoActivity;
    ArrayList<GridItemProjectPhoto> projectPhotoList;

    ProjectPhotoAddFragment projectPhotoAddFragment;

    ActionBar actionBar;
    Drawable upArrow;

    GridView gv_project_photos;
    FloatingActionButton fab_project_photo_add;

    ArrayList<String> selectedPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_project_photo);
        super.onCreate(savedInstanceState);

        projectPhotoActivity = this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data!= null && requestCode == ACTIVITY_RESULT_SELECT_PHOTOS){
            selectedPhotos = data.getStringArrayListExtra("selected_photos");
            projectPhotoAddFragment.showPhotosCount(selectedPhotos.size());
        }
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
        gv_project_photos = (GridView) findViewById(R.id.gv_project_photos);
        fab_project_photo_add = (FloatingActionButton) findViewById(R.id.fab_project_photo_add);

        actionBar = getSupportActionBar();

        projectPhotoList = new ArrayList<>();
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

            Bitmap thumbnailRaw = BitmapFactory.decodeResource(projectPhotoActivity.getResources(), rawResource);

            int newSize = thumbnailRaw.getWidth() < thumbnailRaw.getHeight() ? thumbnailRaw.getWidth() : thumbnailRaw.getHeight();

            gridItemProjectPhoto.setThumbnail(Bitmap.createBitmap(thumbnailRaw, 0, 0, newSize, newSize));

            projectPhotoList.add(gridItemProjectPhoto);
        }

        if(selectedPhotos == null){
            selectedPhotos = new ArrayList<>();
        }
    }

    public void onActivityReady(){
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            if(getResources().getResourceName(R.drawable.abc_ic_ab_back_mtrl_am_alpha) != null){
                upArrow = ContextCompat.getDrawable(projectPhotoActivity, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
                upArrow.setColorFilter(ContextCompat.getColor(projectPhotoActivity, R.color.colorMenuDark), PorterDuff.Mode.SRC_ATOP);
                actionBar.setTitle("PRJ1001");
                actionBar.setHomeAsUpIndicator(upArrow);
            }
        }

        gv_project_photos.setAdapter(new ProjectPhotoGridAdapter(projectPhotoActivity, projectPhotoList));
        gv_project_photos.setOnScrollListener(new gridViewOnScrollListener());
        fab_project_photo_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                projectPhotoAddFragment = new ProjectPhotoAddFragment();
                projectPhotoAddFragment.setCancelable(false);
                projectPhotoAddFragment.show(fragmentManager, getString(R.string.label_add_project_photo));
            }
        });
    }

    public void goToProjectPhotoItem(View view){
        startActivity(new Intent(projectPhotoActivity, ProjectPhotoItemViewActivity.class));
    }

    public void finishProjectPhotoAdd(View view){
        projectPhotoAddFragment.finishProjectPhotoAdd();
        if(!fab_project_photo_add.isShown()){
            fab_project_photo_add.show();
        }
    }

    public void uploadPhotos(View view) {

    }

    public void goToSelectPhotos(View view){
        Intent selePhotosIntent = new Intent(projectPhotoActivity, ProjectPhotoSelectActivity.class);
        selePhotosIntent.putStringArrayListExtra("selected_photos", selectedPhotos);
        startActivityForResult(selePhotosIntent, ACTIVITY_RESULT_SELECT_PHOTOS);
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
