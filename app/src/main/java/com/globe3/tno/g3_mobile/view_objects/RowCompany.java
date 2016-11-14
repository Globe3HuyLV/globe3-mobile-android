package com.globe3.tno.g3_mobile.view_objects;

import android.view.View;

public class RowCompany {
    private String companyName;
    private View.OnClickListener onClickListener;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
