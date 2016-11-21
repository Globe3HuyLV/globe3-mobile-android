package com.globe3.tno.g3_mobile.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
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

public class ProjectPhotoSelectActivity extends BaseActivity {
    ProjectPhotoSelectActivity projectPhotoSelectActivity;

    protected ImageLoader imageLoader = ImageLoader.getInstance();

    private ArrayList<String> imageUrls;
    private DisplayImageOptions options;
    private ImageAdapter imageAdapter;

    GridView gridView;

    private int maxSelect = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_photo_select);

        projectPhotoSelectActivity = this;
    }

    @Override
    protected void onStop() {
        imageLoader.stop();
        super.onStop();
    }

    public void onActivityLoading(){
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

        Cursor imagecursor = projectPhotoSelectActivity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy + " DESC");

        this.imageUrls = new ArrayList<>();

        for (int i = 0; i < imagecursor.getCount(); i++) {
            imagecursor.moveToPosition(i);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
            imageUrls.add(imagecursor.getString(dataColumnIndex));

            System.out.println("=====> Array path => "+imageUrls.get(i));
        }

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_image_black_48dp)
                .showImageForEmptyUri(R.drawable.ic_image_black_48dp)
                .cacheInMemory()
                .cacheOnDisc()
                .build();

        imageAdapter = new ImageAdapter(this, imageUrls);

        gridView = (GridView) findViewById(R.id.gv_select_photos);
    }

    public void onActivityReady(){
        gridView.setAdapter(imageAdapter);
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
            mList = new ArrayList<String>();
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

            final RelativeLayout rlWrapper = (RelativeLayout) convertView.findViewById(R.id.rl_wrapper);
            final CheckBox mCheckBox = (CheckBox) convertView.findViewById(R.id.chk_photo);
            final ImageView mImageCheck = (ImageView) convertView.findViewById(R.id.iv_check_photo);
            final ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_photo);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mCheckBox.isChecked()){
                        mCheckBox.setChecked(false);
                    }else if(!mCheckBox.isChecked()){
                        if(mSparseBooleanArray.size() < 10){
                            mCheckBox.setChecked(true);
                        }else{
                            android.app.AlertDialog.Builder alertBuilder = new AlertDialog.Builder(projectPhotoSelectActivity)
                                .setMessage(String.format(projectPhotoSelectActivity.getString(R.string.msg_selection_limit_photos_reached), String.valueOf(maxSelect)))
                                .setPositiveButton(projectPhotoSelectActivity.getString(R.string.label_okay), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                            android.app.AlertDialog selectAlert = alertBuilder.create();
                            selectAlert.requestWindowFeature(Window.FEATURE_NO_TITLE);

                            selectAlert.show();
                        }
                    }
                }
            });

            imageLoader.displayImage("file://" + imageUrls.get(position), imageView, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(Bitmap loadedImage) {
                    Animation anim = AnimationUtils.loadAnimation(projectPhotoSelectActivity, R.anim.animate_fade_in);
                    imageView.setAnimation(anim);
                    anim.start();
                }
            });

            mCheckBox.setTag(position);
            mCheckBox.setChecked(mSparseBooleanArray.get(position));
            mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
                        mImageCheck.setImageDrawable(ActivityCompat.getDrawable(projectPhotoSelectActivity, R.drawable.ic_check_box_black_24dp));
                        mImageCheck.setColorFilter(ActivityCompat.getColor(projectPhotoSelectActivity, R.color.colorAccentLight));
                        imageView.setColorFilter(ActivityCompat.getColor(projectPhotoSelectActivity, R.color.colorBlueTransparent));
                        rlWrapper.setBackgroundColor(ActivityCompat.getColor(projectPhotoSelectActivity, R.color.colorBlueTransparent));
                    }else{
                        mSparseBooleanArray.delete((Integer) buttonView.getTag());
                        mImageCheck.setImageDrawable(ActivityCompat.getDrawable(projectPhotoSelectActivity, R.drawable.ic_check_box_outline_blank_black_24dp));
                        mImageCheck.setColorFilter(ActivityCompat.getColor(projectPhotoSelectActivity, R.color.colorAccentLight));
                        imageView.setColorFilter(null);
                        rlWrapper.setBackground(ActivityCompat.getDrawable(projectPhotoSelectActivity, R.drawable.border_blue));
                    }
                }
            });
            mCheckBox.setEnabled(false);
            return convertView;
        }
    }
}
