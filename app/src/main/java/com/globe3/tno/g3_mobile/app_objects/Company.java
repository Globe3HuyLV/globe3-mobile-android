package com.globe3.tno.g3_mobile.app_objects;

import java.io.Serializable;

public class Company implements Serializable{
    private long idcode;
    private String uniquenum;
    private String name;
    private String code;
    private boolean active;

    public long getIdcode() {
        return idcode;
    }

    public String getUniquenum() {
        return uniquenum;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public boolean isActive() {
        return active;
    }

    public void setIdcode(long idcode) {
        this.idcode = idcode;
    }

    public void setUniquenum(String uniquenum) {
        this.uniquenum = uniquenum;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
