package com.globe3.tno.g3_mobile.model.entities;

import java.io.Serializable;
import java.util.Date;

public class scanimage implements Serializable{
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
	public String staff_unique;
	public String staff_id;
	public String staff_fullname;
	public byte[] photo1;
	public byte[] photo2;
	public byte[] fingerprint_image1;
	public byte[] fingerprint_image2;
	public byte[] fingerprint_image3;
	public byte[] fingerprint_image4;
	public byte[] fingerprint_image5;
	public Date date_register;
	public String gps_location;
	public String device_id;
	public String nvar25_01;
	public String nvar25_02;
	public String nvar25_03;
	public String nvar100_01;
	public String nvar100_02;
	public String nvar100_03;
	public Date date01;
	public Date date02;
	public float num01;
	public float num02;
}
