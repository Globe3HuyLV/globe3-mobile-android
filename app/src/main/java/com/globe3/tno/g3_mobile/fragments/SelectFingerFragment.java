package com.globe3.tno.g3_mobile.fragments;

import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.DialogFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.app_objects.Staff;
import com.globe3.tno.g3_mobile.app_objects.factory.AuditFactory;
import com.globe3.tno.g3_mobile.app_objects.factory.StaffFactory;

import static com.globe3.tno.g3_mobile.constants.App.APP_NAME;
import static com.globe3.tno.g3_mobile.constants.App.FINGER_COUNTER;

public class SelectFingerFragment extends DialogFragment {

    AuditFactory auditFactory;
    StaffFactory staffFactory;

    Staff staff;

    int finger_num = 1;

    LinearLayout main_container;

    ImageView iv_staff_photo;
    TextView tv_staff_num;
    TextView tv_staff_desc;

    RelativeLayout rl_finger_one;
    RelativeLayout rl_finger_two;
    RelativeLayout rl_finger_three;
    RelativeLayout rl_finger_four;
    RelativeLayout rl_finger_five;

    ImageView iv_finger_one;
    ImageView iv_finger_two;
    ImageView iv_finger_three;
    ImageView iv_finger_four;
    ImageView iv_finger_five;

    ImageView iv_finger_num_one;
    ImageView iv_finger_num_two;
    ImageView iv_finger_num_three;
    ImageView iv_finger_num_four;
    ImageView iv_finger_num_five;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View selectFingerFragment = inflater.inflate(R.layout.fragment_select_finger, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        main_container = (LinearLayout) selectFingerFragment.findViewById(R.id.main_container);

        iv_staff_photo = (ImageView) selectFingerFragment.findViewById(R.id.iv_staff_photo);
        tv_staff_num = (TextView) selectFingerFragment.findViewById(R.id.tv_staff_num);
        tv_staff_desc = (TextView) selectFingerFragment.findViewById(R.id.tv_staff_desc);

        rl_finger_one = (RelativeLayout) selectFingerFragment.findViewById(R.id.rl_finger_one);
        rl_finger_two = (RelativeLayout) selectFingerFragment.findViewById(R.id.rl_finger_two);
        rl_finger_three = (RelativeLayout) selectFingerFragment.findViewById(R.id.rl_finger_three);
        rl_finger_four = (RelativeLayout) selectFingerFragment.findViewById(R.id.rl_finger_four);
        rl_finger_five = (RelativeLayout) selectFingerFragment.findViewById(R.id.rl_finger_five);

        iv_finger_one = (ImageView) selectFingerFragment.findViewById(R.id.iv_finger_one);
        iv_finger_two = (ImageView) selectFingerFragment.findViewById(R.id.iv_finger_two);
        iv_finger_three = (ImageView) selectFingerFragment.findViewById(R.id.iv_finger_three);
        iv_finger_four = (ImageView) selectFingerFragment.findViewById(R.id.iv_finger_four);
        iv_finger_five = (ImageView) selectFingerFragment.findViewById(R.id.iv_finger_five);

        iv_finger_num_one = (ImageView) selectFingerFragment.findViewById(R.id.iv_finger_num_one);
        iv_finger_num_two = (ImageView) selectFingerFragment.findViewById(R.id.iv_finger_num_two);
        iv_finger_num_three = (ImageView) selectFingerFragment.findViewById(R.id.iv_finger_num_three);
        iv_finger_num_four = (ImageView) selectFingerFragment.findViewById(R.id.iv_finger_num_four);
        iv_finger_num_five = (ImageView) selectFingerFragment.findViewById(R.id.iv_finger_num_five);

        rl_finger_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFinger(1);
            }
        });
        rl_finger_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFinger(2);
            }
        });
        rl_finger_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFinger(3);
            }
        });
        rl_finger_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFinger(4);
            }
        });
        rl_finger_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFinger(5);
            }
        });

        iv_finger_one.setColorFilter(ResourcesCompat.getColor(getResources(), (staff.getFingerprint_image1()!=null?R.color.colorSuccess:R.color.colorMenuLight), null));
        iv_finger_two.setColorFilter(ResourcesCompat.getColor(getResources(), (staff.getFingerprint_image2()!=null?R.color.colorSuccess:R.color.colorMenuLight), null));
        iv_finger_three.setColorFilter(ResourcesCompat.getColor(getResources(), (staff.getFingerprint_image3()!=null?R.color.colorSuccess:R.color.colorMenuLight), null));
        iv_finger_four.setColorFilter(ResourcesCompat.getColor(getResources(), (staff.getFingerprint_image4()!=null?R.color.colorSuccess:R.color.colorMenuLight), null));
        iv_finger_five.setColorFilter(ResourcesCompat.getColor(getResources(), (staff.getFingerprint_image5()!=null?R.color.colorSuccess:R.color.colorMenuLight), null));

        iv_finger_num_one.setImageDrawable(ContextCompat.getDrawable(getActivity(), staff.getFingerprint_image1()!=null?R.drawable.ic_finger_one_select:R.drawable.ic_finger_one_gray_select));
        iv_finger_num_two.setImageDrawable(ContextCompat.getDrawable(getActivity(), staff.getFingerprint_image2()!=null?R.drawable.ic_finger_two:R.drawable.ic_finger_two_gray));
        iv_finger_num_three.setImageDrawable(ContextCompat.getDrawable(getActivity(), staff.getFingerprint_image3()!=null?R.drawable.ic_finger_three:R.drawable.ic_finger_three_gray));
        iv_finger_num_four.setImageDrawable(ContextCompat.getDrawable(getActivity(), staff.getFingerprint_image4()!=null?R.drawable.ic_finger_four:R.drawable.ic_finger_four_gray));
        iv_finger_num_five.setImageDrawable(ContextCompat.getDrawable(getActivity(), staff.getFingerprint_image5()!=null?R.drawable.ic_finger_five:R.drawable.ic_finger_five_gray));

        if(staff.getPhoto1()!=null){
            Bitmap staffPhoto = BitmapFactory.decodeByteArray(staff.getPhoto1(), 0, staff.getPhoto1().length);

            int newSize = staffPhoto.getWidth() < staffPhoto.getHeight() ? staffPhoto.getWidth() : staffPhoto.getHeight();

            iv_staff_photo.setImageBitmap(Bitmap.createBitmap(staffPhoto, 0, 0, newSize, newSize));
        }else{
            iv_staff_photo.setImageResource(R.drawable.ic_person_black_48dp);
        }

        tv_staff_num.setText(staff.getStaff_num());
        tv_staff_desc.setText(staff.getStaff_desc());

        return  selectFingerFragment;
    }


    private void selectFinger(int fingerNum){
        this.finger_num = fingerNum;
        rl_finger_one.setBackgroundColor(fingerNum==1? ResourcesCompat.getColor(getResources(), R.color.colorFingerSelect, null) : 0x00000000);
        rl_finger_two.setBackgroundColor(fingerNum==2? ResourcesCompat.getColor(getResources(), R.color.colorFingerSelect, null) : 0x00000000);
        rl_finger_three.setBackgroundColor(fingerNum==3? ResourcesCompat.getColor(getResources(), R.color.colorFingerSelect, null) : 0x00000000);
        rl_finger_four.setBackgroundColor(fingerNum==4? ResourcesCompat.getColor(getResources(), R.color.colorFingerSelect, null) : 0x00000000);
        rl_finger_five.setBackgroundColor(fingerNum==5? ResourcesCompat.getColor(getResources(), R.color.colorFingerSelect, null) : 0x00000000);

        iv_finger_num_one.setImageDrawable(ContextCompat.getDrawable(getActivity(), staff.getFingerprint_image1()!=null?(fingerNum==1?R.drawable.ic_finger_one_select:R.drawable.ic_finger_one):(fingerNum==1?R.drawable.ic_finger_one_gray_select:R.drawable.ic_finger_one_gray)));
        iv_finger_num_two.setImageDrawable(ContextCompat.getDrawable(getActivity(), staff.getFingerprint_image2()!=null?(fingerNum==2?R.drawable.ic_finger_two_select:R.drawable.ic_finger_two):(fingerNum==2?R.drawable.ic_finger_two_gray_select:R.drawable.ic_finger_two_gray)));
        iv_finger_num_three.setImageDrawable(ContextCompat.getDrawable(getActivity(), staff.getFingerprint_image3()!=null?(fingerNum==3?R.drawable.ic_finger_three_select:R.drawable.ic_finger_three):(fingerNum==3?R.drawable.ic_finger_three_gray_select:R.drawable.ic_finger_three_gray)));
        iv_finger_num_four.setImageDrawable(ContextCompat.getDrawable(getActivity(), staff.getFingerprint_image4()!=null?(fingerNum==4?R.drawable.ic_finger_four_select:R.drawable.ic_finger_four):(fingerNum==4?R.drawable.ic_finger_four_gray_select:R.drawable.ic_finger_four_gray)));
        iv_finger_num_five.setImageDrawable(ContextCompat.getDrawable(getActivity(), staff.getFingerprint_image5()!=null?(fingerNum==5?R.drawable.ic_finger_five_select:R.drawable.ic_finger_five):(fingerNum==5?R.drawable.ic_finger_five_gray_select:R.drawable.ic_finger_five_gray)));
    }

    public void startRegister(){
        FragmentManager fragmentManager = getFragmentManager();
        RegisterFingerFragment registerFingerFragment = new RegisterFingerFragment();
        registerFingerFragment.setCancelable(false);
        registerFingerFragment.setStaff(staff);
        registerFingerFragment.setAuditFactory(auditFactory);
        registerFingerFragment.setStaffFactory(staffFactory);
        registerFingerFragment.setFingerSelected(finger_num);
        registerFingerFragment.setSelectFingerFragment(this);
        registerFingerFragment.show(fragmentManager, getString(R.string.label_register_finger));
        main_container.setVisibility(View.GONE);
    }

    public void setAuditFactory(AuditFactory auditFactory){
        this.auditFactory = auditFactory;
    }
    public void setStaffFactory(StaffFactory staffFactory){
        this.staffFactory = staffFactory;
    }
    public void setStaff(Staff staff){
        this.staff = staff;
    }

    public void show(Staff staff) {
        this.staff = staff;

        iv_finger_one.setColorFilter(ResourcesCompat.getColor(getResources(), (staff.getFingerprint_image1()!=null?R.color.colorSuccess:R.color.colorMenuLight), null));
        iv_finger_two.setColorFilter(ResourcesCompat.getColor(getResources(), (staff.getFingerprint_image2()!=null?R.color.colorSuccess:R.color.colorMenuLight), null));
        iv_finger_three.setColorFilter(ResourcesCompat.getColor(getResources(), (staff.getFingerprint_image3()!=null?R.color.colorSuccess:R.color.colorMenuLight), null));
        iv_finger_four.setColorFilter(ResourcesCompat.getColor(getResources(), (staff.getFingerprint_image4()!=null?R.color.colorSuccess:R.color.colorMenuLight), null));
        iv_finger_five.setColorFilter(ResourcesCompat.getColor(getResources(), (staff.getFingerprint_image5()!=null?R.color.colorSuccess:R.color.colorMenuLight), null));

        selectFinger(finger_num);
        main_container.setVisibility(View.VISIBLE);
    }
}
