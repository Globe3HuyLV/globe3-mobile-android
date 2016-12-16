package com.globe3.tno.g3_mobile.app_objects;

import java.util.ArrayList;

public class SalesOrder {
    private long idcode;
    private String uniquenumPri;
    private String desc;
    private String code;
    private boolean active;

    private ArrayList<StaffTeam> staff_team;

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ArrayList<StaffTeam> getStaffTeam() {
        return staff_team;
    }

    public void setStaffTeam(ArrayList<StaffTeam> staffTeam) {
        this.staff_team = staffTeam;
    }
}
