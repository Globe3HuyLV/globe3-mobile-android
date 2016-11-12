package com.globe3.tno.g3_mobile.app_objects;

import java.io.Serializable;
import java.util.Date;

public class TimeLog implements Serializable{
    private long idcode;
    private String uniquenum_pri;
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

    public String getUniquenum_pri() {
        return uniquenum_pri;
    }

    public void setUniquenum_pri(String uniquenum_pri) {
        this.uniquenum_pri = uniquenum_pri;
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
