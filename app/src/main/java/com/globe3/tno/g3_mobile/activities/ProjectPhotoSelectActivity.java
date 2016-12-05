package com.globe3.tno.g3_mobile.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.globe3.tno.g3_mobile.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import java.util.ArrayList;

import static com.globe3.tno.g3_mobile.constants.App.ACTIVITY_RESULT_SELECT_PHOTOS;

public class ProjectPhotoSelectActivity extends BaseActivity {
    ProjectPhotoSelectActivity project_photo_select_activity;

    ActionBar action_bar;
    Drawable up_arrow;
    Menu menu;

    protected ImageLoader imageLoader = ImageLoader.getInstance();

    private ArrayList<String> imageUrls;
    private DisplayImageOptions options;
    private ImageAdapter imageAdapter;
    private ArrayList<String> selectedPhotos;

    GridView gv_select_photos;
    
    private int maxSelect = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_photo_select);

        project_photo_select_activity = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return true;
    }

    @Override
    protected void onStop() {
        imageLoader.stop();
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_add_photos:
                ArrayList<String> selectedItems = imageAdapter.getCheckedItems();

                if(selectedItems.size() > maxSelect){
                    promptLimitReached();
                }else{
                    Intent resultIntent = new Intent();
                    resultIntent.putStringArrayListExtra("selected_photos", selectedItems);
                    project_photo_select_activity.setResult(ACTIVITY_RESULT_SELECT_PHOTOS, resultIntent);
                    finish();
                }
                break;
        }
        return true;
    }

    public void onActivityLoading(){
        action_bar = getSupportActionBar();

        if(selectedPhotos == null){
            selectedPhotos = new ArrayList<>();
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("selected_photos")) {
                selectedPhotos = extras.getStringArrayList("selected_photos");
            }
        }

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCacheSize(1500000) // 1.5 Mb
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .enableLogging()
                .build();
        ImageLoader.getInstance().init(config);

        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;

        Cursor imagecursor = project_photo_select_activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy + " DESC");

        this.imageUrls = new ArrayList<>();

        for (int i = 0; i < imagecursor.getCount(); i++) {
            imagecursor.moveToPosition(i);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
            imageUrls.add(imagecursor.getString(dataColumnIndex));

            System.out.println("=====> Array path => "+imageUrls.get(i));
        }

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.image_stub)
                .showImageForEmptyUri(R.drawable.image_stub)
                .cacheInMemory()
                .cacheOnDisc()
                .build();

        imageAdapter = new ImageAdapter(this, imageUrls);

        gv_select_photos = (GridView) findViewById(R.id.gv_select_photos);
    }

    public void onActivityReady(){
        if(action_bar != null){
            action_bar.setDisplayHomeAsUpEnabled(true);
            if(getResources().getResourceName(R.drawable.abc_ic_ab_back_mtrl_am_alpha) != null){
                up_arrow = ContextCompat.getDrawable(project_photo_select_activity, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
                up_arrow.setColorFilter(ContextCompat.getColor(project_photo_select_activity, R.color.colorMenuDark), PorterDuff.Mode.SRC_ATOP);
                action_bar.setHomeAsUpIndicator(up_arrow);
            }
        }

        gv_select_photos.setAdapter(imageAdapter);
    }

    private void promptLimitReached(){
        android.app.AlertDialog.Builder alertBuilder = new AlertDialog.Builder(project_photo_select_activity)
                .setMessage(String.format(project_photo_select_activity.getString(R.string.msg_selection_limit_photos_reached), String.valueOf(maxSelect)))
                .setPositiveButton(project_photo_select_activity.getString(R.string.label_return), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        android.app.AlertDialog selectAlert = alertBuilder.create();
        selectAlert.requestWindowFeature(Window.FEATURE_NO_TITLE);

        selectAlert.show();
    }

    public class ImageAdapter extends BaseAdapter {

        ArrayList<String> mList;
        LayoutInflater mInflater;
        Context mContext;
        SparseBooleanArray mSparseBooleanArray;

        public ImageAdapter(Context context, ArrayList<String> imageList) {
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
            mSparseBooleanArray = new SparseBooleanArray();
            mList = new ArrayList<>();
            this.mList = imageList;

        }

        public ArrayList<String> getCheckedItems() {
            ArrayList<String> mTempArry = new ArrayList<String>();

            for(int i=0;i<mList.size();i++) {
                if(mSparseBooleanArray.get(i)) {
                    mTempArry.add(mList.get(i));
                }
            }

            return mTempArry;
        }

        @Override
        public int getCount() {
            return imageUrls.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null) {
                convertView = mInflater.inflate(R.layout.item_projectphotoselect, null);
            }

            final RelativeLayout rl_wrapper = (RelativeLayout) convertView.findViewById(R.id.rl_wrapper);
            final CheckBox chk_photo = (CheckBox) convertView.findViewById(R.id.chk_photo);
            final ImageView iv_check_box = (ImageView) convertView.findViewById(R.id.iv_check_box);
            final ImageView iv_photo = (ImageView) convertView.findViewById(R.id.iv_photo);
            iv_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(chk_photo.isChecked()){
                        chk_photo.setChecked(false);
                    }else if(!chk_photo.isChecked()){
                        if(mSparseBooleanArray.size() < 10){
                            chk_photo.setChecked(true);
                        }else{
                           promptLimitReached();
                        }
                    }
                }
            });

            imageLoader.displayImage("file://" + imageUrls.get(position), iv_photo, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(Bitmap loadedImage) {
                    Animation anim = AnimationUtils.loadAnimation(project_photo_select_activity, R.anim.animate_fade_in);
                    iv_photo.setAnimation(anim);
                    anim.start();
                }
            });

            chk_photo.setTag(position);
            chk_photo.setChecked(mSparseBooleanArray.get(position) || selectedPhotos.contains(imageUrls.get(position)));
            chk_photo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
                        iv_check_box.setImageDrawable(ActivityCompat.getDrawable(project_photo_select_activity, R.drawable.ic_check_box_black_24dp));
                        iv_check_box.setColorFilter(ActivityCompat.getColor(project_photo_select_activity, R.color.colorAccentLight));
                        iv_photo.setColorFilter(ActivityCompat.getColor(project_photo_select_activity, R.color.colorBlueTransparent));
                        rl_wrapper.setBackgroundColor(ActivityCompat.getColor(project_photo_select_activity, R.color.colorBlueTransparent));
                    }else{
                        mSparseBooleanArray.delete((Integer) buttonView.getTag());
                        iv_check_box.setImageDrawable(ActivityCompat.getDrawable(project_photo_select_activity, R.drawable.ic_check_box_outline_blank_black_24dp));
                        iv_check_box.setColorFilter(ActivityCompat.getColor(project_photo_select_activity, R.color.colorAccentLight));
                        iv_photo.setColorFilter(null);
                        rl_wrapper.setBackground(ActivityCompat.getDrawable(project_photo_select_activity, R.drawable.border_blue));
                    }
                    menu.clear();
                    if(mSparseBooleanArray.size() > 0){
                        getMenuInflater().inflate(R.menu.actionbar_add_photos, menu);
                        MenuItem photos_count = menu.findItem(R.id.display_photos_count);
                        photos_count.setTitle(String.format(project_photo_select_activity.getString(R.string.msg_num_photos_selected), String.valueOf(mSparseBooleanArray.size())));
                    }
                }
            });

            chk_photo.setEnabled(false);

            if(chk_photo.isChecked()){
                mSparseBooleanArray.put((Integer) chk_photo.getTag(), chk_photo.isChecked());
                iv_check_box.setImageDrawable(ActivityCompat.getDrawable(project_photo_select_activity, R.drawable.ic_check_box_black_24dp));
                iv_check_box.setColorFilter(ActivityCompat.getColor(project_photo_select_activity, R.color.colorAccentLight));
                iv_photo.setColorFilter(ActivityCompat.getColor(project_photo_select_activity, R.color.colorBlueTransparent));
                rl_wrapper.setBackgroundColor(ActivityCompat.getColor(project_photo_select_activity, R.color.colorBlueTransparent));
            }else{
                mSparseBooleanArray.delete((Integer) chk_photo.getTag());
                iv_check_box.setImageDrawable(ActivityCompat.getDrawable(project_photo_select_activity, R.drawable.ic_check_box_outline_blank_black_24dp));
                iv_check_box.setColorFilter(ActivityCompat.getColor(project_photo_select_activity, R.color.colorAccentLight));
                iv_photo.setColorFilter(null);
                rl_wrapper.setBackground(ActivityCompat.getDrawable(project_photo_select_activity, R.drawable.border_blue));
            }

            return convertView;
        }
    }
}
