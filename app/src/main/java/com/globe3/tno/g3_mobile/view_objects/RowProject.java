package com.globe3.tno.g3_mobile.view_objects;

import android.view.View;

public class RowProject {
    private String projectCode;
    private String projectName;
    private int projectPhotosCount;
    private View.OnClickListener onClickListener;

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getProjectPhotosCount() {
        return projectPhotosCount;
    }

    public void setProjectPhotosCount(int projectPhotosCount) {
        this.projectPhotosCount = projectPhotosCount;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
