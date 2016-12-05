package com.globe3.tno.g3_mobile.fragments;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.activities.PhotosActivity;
import com.globe3.tno.g3_mobile.app_objects.Staff;
import com.globe3.tno.g3_mobile.app_objects.factory.AuditFactory;
import com.globe3.tno.g3_mobile.app_objects.factory.StaffFactory;
import com.globe3.tno.g3_mobile.async.StaffSingleUploadTask;
import com.globe3.tno.g3_mobile.constants.TagTableUsage;

public class StaffPhotoPreview extends DialogFragment {

    AuditFactory audit_factory;
    StaffFactory staff_factory;

    Staff staff;

    LinearLayout ll_main_container;
    ImageView iv_staff_photo;
    RelativeLayout rl_no_photo;
    TextView tv_staff_num;
    TextView tv_staff_desc;
    TextView tv_action_button;
    TextView tv_cancel;

    private View.OnClickListener take_photo = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startTakePhoto();
        }
    };

    private View.OnClickListener save_photo = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            staff_factory.updateStaff(staff);
            new StaffSingleUploadTask(staff_factory, staff, audit_factory.Log(TagTableUsage.STAFF_SYNC_UP)).execute();
            ((PhotosActivity) getActivity()).finishStaffTakePhoto();
            dismiss();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View staffPhotoPreviewFragment = inflater.inflate(R.layout.fragment_staff_photo_preview, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        ll_main_container = (LinearLayout) staffPhotoPreviewFragment.findViewById(R.id.ll_main_container);
        rl_no_photo = (RelativeLayout) staffPhotoPreviewFragment.findViewById(R.id.rl_no_photo);
        iv_staff_photo = (ImageView) staffPhotoPreviewFragment.findViewById(R.id.iv_staff_photo);
        tv_staff_num = (TextView) staffPhotoPreviewFragment.findViewById(R.id.tv_staff_num);
        tv_staff_desc = (TextView) staffPhotoPreviewFragment.findViewById(R.id.tv_staff_desc);
        tv_action_button = (TextView) staffPhotoPreviewFragment.findViewById(R.id.tv_action_button);
        tv_cancel = (TextView) staffPhotoPreviewFragment.findViewById(R.id.tv_cancel);

        if(staff.getPhoto1()!=null){
            Bitmap staffPhoto = BitmapFactory.decodeByteArray(staff.getPhoto1(), 0, staff.getPhoto1().length);

            int newSize = staffPhoto.getWidth() < staffPhoto.getHeight() ? staffPhoto.getWidth() : staffPhoto.getHeight();

            iv_staff_photo.setImageBitmap(Bitmap.createBitmap(staffPhoto, 0, 0, newSize, newSize));
            iv_staff_photo.setVisibility(View.VISIBLE);
            rl_no_photo.setVisibility(View.GONE);
        }else{
            iv_staff_photo.setImageResource(R.drawable.ic_person_black_48dp);
            iv_staff_photo.setVisibility(View.GONE);
            rl_no_photo.setVisibility(View.VISIBLE);
        }

        tv_staff_num.setText(staff.getStaff_num());
        tv_staff_desc.setText(staff.getStaff_desc());

        tv_action_button.setOnClickListener(take_photo);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return  staffPhotoPreviewFragment;
    }

    private void startTakePhoto(){
        ll_main_container.setVisibility(View.GONE);
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        StaffTakePhotoFragment staffTakePhotoFragment = new StaffTakePhotoFragment();
        staffTakePhotoFragment.setCancelable(false);
        staffTakePhotoFragment.setStaff(staff);
        staffTakePhotoFragment.setStaffFactory(staff_factory);
        staffTakePhotoFragment.setAuditFactory(audit_factory);
        staffTakePhotoFragment.setStaffPhotoPreview(this);
        staffTakePhotoFragment.show(fragmentManager, getString(R.string.label_take_photo));
    }

    public void setAuditFactory(AuditFactory auditFactory){
        this.audit_factory = auditFactory;
    }
    public void setStaffFactory(StaffFactory staffFactory){
        this.staff_factory = staffFactory;
    }
    public void setStaff(Staff staff){
        this.staff = staff;
    }

    public void show(Staff staff, boolean isUpdated) {
        this.staff = staff;

        if(staff.getPhoto1()!=null){
            Bitmap staffPhoto = BitmapFactory.decodeByteArray(staff.getPhoto1(), 0, staff.getPhoto1().length);

            int newSize = staffPhoto.getWidth() < staffPhoto.getHeight() ? staffPhoto.getWidth() : staffPhoto.getHeight();

            iv_staff_photo.setImageBitmap(Bitmap.createBitmap(staffPhoto, 0, 0, newSize, newSize));
            iv_staff_photo.setVisibility(View.VISIBLE);
            rl_no_photo.setVisibility(View.GONE);
        }else{
            iv_staff_photo.setImageResource(R.drawable.ic_person_black_48dp);
            iv_staff_photo.setVisibility(View.GONE);
            rl_no_photo.setVisibility(View.VISIBLE);
        }

        if(isUpdated){
            tv_action_button.setText(getString(R.string.label_save_photo));
            tv_action_button.setOnClickListener(save_photo);
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ll_main_container.setVisibility(View.VISIBLE);
            }
        });
    }
}
