package com.globe3.tno.g3_mobile.view_objects;

import android.graphics.Bitmap;
import android.view.View;

public class RowStaff {
    private Bitmap staffPhoto;
    private String staffCode;
    private String staffName;
    private int staffFingerCount;
    private boolean displayBottomSpacer;
    private View.OnClickListener onClickListener;

    public Bitmap getStaffPhoto() {
        return staffPhoto;
    }

    public void setStaffPhoto(Bitmap staffPhoto) {
        this.staffPhoto = staffPhoto;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public int getStaffFingerCount() {
        return staffFingerCount;
    }

    public void setStaffFingerCount(int staffFingerCount) {
        this.staffFingerCount = staffFingerCount;
    }

    public boolean isDisplayBottomSpacer() {
        return displayBottomSpacer;
    }

    public void setDisplayBottomSpacer(boolean displayBottomSpacer) {
        this.displayBottomSpacer = displayBottomSpacer;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
