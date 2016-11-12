package com.globe3.tno.g3_mobile.app_objects;

import java.io.Serializable;

public class Project implements Serializable{
    private long idcode;
    private String uniquenum;
    private String desc;
    private String code;
    private boolean active;

    public void setIdcode(long pIdcode){
        this.idcode = pIdcode;
    }

    public long getIdcode(){
        return idcode;
    }

    public void setUniquenum(String pUniquenum){
        this.uniquenum = pUniquenum;
    }

    public String getUniquenum(){
        return uniquenum;
    }

    public void setDesc(String pName){
        this.desc = pName;
    }

    public String getDesc() {
        return desc;
    }

    public void setCode(String pId){
        this.code = pId;
    }

    public String getCode(){
        return code;
    }

    public void setActive(boolean pDeleted){
        this.active = pDeleted;
    }

    public boolean getActive(){
        return active;
    }
}
