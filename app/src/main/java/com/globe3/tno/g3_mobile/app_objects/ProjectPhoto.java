package com.globe3.tno.g3_mobile.app_objects;

import java.util.ArrayList;

public class ProjectPhoto {
    private Project project;
    private String uniquenumPri;
    private String reference;
    private String remarks;
    private ArrayList<String> photos;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getUniquenumPri() {
        return uniquenumPri;
    }

    public void setUniquenumPri(String uniquenumPri) {
        this.uniquenumPri = uniquenumPri;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }
}
