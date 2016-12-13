package com.globe3.tno.g3_mobile.app_objects;

public class StaffTeam {
    private long idcode;
    private String uniquenumPri;
    private String desc;
    private String code;
    private boolean active;

    public void setIdcode(long pIdcode){
        this.idcode = pIdcode;
    }

    public long getIdcode(){
        return idcode;
    }

    public void setUniquenumPri(String pUniquenum){
        this.uniquenumPri = pUniquenum;
    }

    public String getUniquenumPri(){
        return uniquenumPri;
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
