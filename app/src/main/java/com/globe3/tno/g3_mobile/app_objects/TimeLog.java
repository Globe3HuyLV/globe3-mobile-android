package com.globe3.tno.g3_mobile.app_objects;

import java.io.Serializable;
import java.util.Date;

public class TimeLog implements Serializable{
    private long idcode;
    private String uniquenumPri;
    private Date date;
    private String type;
    private Staff staff;
    private Project project;

    public long getIdcode() {
        return idcode;
    }

    public void setIdcode(long idcode) {
        this.idcode = idcode;
    }

    public String getUniquenumPri() {
        return uniquenumPri;
    }

    public void setUniquenumPri(String uniquenumPri) {
        this.uniquenumPri = uniquenumPri;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
