package com.globe3.tno.g3_mobile.model.entities;

import java.io.Serializable;
import java.util.Date;

public class useraccess implements Serializable{
    public long idcode;
    public String tag_table_usage;
    public String sync_unique;
    public String uniquenum_pri;
    public String uniquenum_sec;
    public String active_yn;
    public Date date_post;
    public Date date_submit;
    public Date date_lastupdate;
    public Date date_sync;
    public String cfsqlfilename;
    public String masterfn;
    public String companyfn;
    public String companyid;
    public String user_specialnum;
    public String userid;
    public String password;
    public Date date_valid_to;
    public Date date_valid_from;
    public String staff_code;
    public String staff_desc;
    public String staff_unique;
    public String device_id;
}
