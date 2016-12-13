package com.globe3.tno.g3_mobile.model.entities;

import java.io.Serializable;
import java.util.Date;

public class team implements Serializable{
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
	public String userid_creator;
	public String masterfn;
	public String companyfn;
	public String team_code;
	public String team_name;
	public String team_unique;
}
