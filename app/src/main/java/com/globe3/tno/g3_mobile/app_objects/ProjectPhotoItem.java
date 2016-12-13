package com.globe3.tno.g3_mobile.app_objects;

import android.graphics.Bitmap;

import java.util.Date;

public class ProjectPhotoItem {
    private String uniquenumPri;
    private String uniquenumSec;
    private Project project;
    private Date date_post;
    private int row_item_num;
    private String reference_num;
    private String remarks;
    private byte[] photo;

    public String getUniquenumPri() {
        return uniquenumPri;
    }

    public void setUniquenumPri(String uniquenumPri) {
        this.uniquenumPri = uniquenumPri;
    }

    public String getUniquenumSec() {
        return uniquenumSec;
    }

    public void setUniquenumSec(String uniquenumSec) {
        this.uniquenumSec = uniquenumSec;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Date getDatePost() {
        return date_post;
    }

    public void setDate_post(Date datePost) {
        this.date_post = date_post;
    }

    public int getRowItemNum() {
        return row_item_num;
    }

    public void setRowItemNum(int row_item_num) {
        this.row_item_num = row_item_num;
    }

    public String getReferenceNum() {
        return reference_num;
    }

    public void setReferenceNum(String reference_num) {
        this.reference_num = reference_num;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}
