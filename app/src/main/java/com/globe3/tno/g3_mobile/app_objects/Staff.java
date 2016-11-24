package com.globe3.tno.g3_mobile.app_objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Staff implements Serializable{
    private long idcode;
    private String uniquenum;
    private String staff_desc;
    private String staff_num;
    private String job_title;
    private boolean registered;
    private Date date_posted;
    private Date date_update;
    private Date registration_date;
    private String work_permit_id;
    private Date work_permit_issued;
    private Date work_permit_expiry;
    private Date dob;
    private String nationality;
    private String gender;
    private String race;
    private byte[] fingerprint_image1;
    private byte[] fingerprint_image2;
    private byte[] fingerprint_image3;
    private byte[] fingerprint_image4;
    private byte[] fingerprint_image5;
    private byte[] photo1;
    private boolean active;

    public ArrayList<TimeLog> TimeLogs;

    public void setIdcode(long pIdcode){
        this.idcode = pIdcode;
    }

    public long getIdcode(){
        return idcode;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public void setUniquenum(String pUniquenum){
        this.uniquenum = pUniquenum;
    }

    public String getUniquenum(){
        return uniquenum;
    }

    public void setStaff_desc(String pName){
        this.staff_desc = pName;
    }

    public String getStaff_desc() {
        return staff_desc;
    }

    public void setStaff_num(String pId){
        this.staff_num = pId;
    }

    public String getStaff_num(){
        return staff_num;
    }

    public Date getDate_update() {
        return date_update;
    }

    public void setDate_update(Date date_update) {
        this.date_update = date_update;
    }

    public void setRegistered(boolean pRegistered){
        this.registered = pRegistered;
    }

    public boolean getRegistered(){
        return registered;
    }

    public void setRegisteration(Date pRegistration){
        this.registration_date = pRegistration;
    }

    public Date getRegistration(){
        return registration_date;
    }

    public Date getDate_posted() { return date_posted; }

    public void setDate_posted(Date date_posted) { this.date_posted = date_posted; }

    public void setWorkPermitId(String pWorkPermitId){
        this.work_permit_id = pWorkPermitId;
    }

    public String getWorkPermitId(){
        return work_permit_id;
    }


    public void setWorkPermitIssued(Date pWorkPermitIssued){
        this.work_permit_issued = pWorkPermitIssued;
    }

    public Date getWorkPermitIssued(){
        return work_permit_issued;
    }

    public void setWorkPermitExpiry(Date pWorkPermitExpiry){
        this.work_permit_expiry = pWorkPermitExpiry;
    }

    public Date getWorkPermitExpiry(){
        return work_permit_expiry;
    }

    public void setDob(Date pDob){
        this.dob = pDob;
    }

    public Date getDob(){
        return dob;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public byte[] getFingerprint_image1() {
        return fingerprint_image1;
    }

    public void setFingerprint_image1(byte[] fingerprint_image1) {
        this.fingerprint_image1 = fingerprint_image1;
    }

    public byte[] getFingerprint_image2() {
        return fingerprint_image2;
    }

    public void setFingerprint_image2(byte[] fingerprint_image2) {
        this.fingerprint_image2 = fingerprint_image2;
    }

    public byte[] getFingerprint_image3() {
        return fingerprint_image3;
    }

    public void setFingerprint_image3(byte[] fingerprint_image3) {
        this.fingerprint_image3 = fingerprint_image3;
    }

    public byte[] getFingerprint_image4() {
        return fingerprint_image4;
    }

    public void setFingerprint_image4(byte[] fingerprint_image4) {
        this.fingerprint_image4 = fingerprint_image4;
    }

    public byte[] getFingerprint_image5() {
        return fingerprint_image5;
    }

    public void setFingerprint_image5(byte[] fingerprint_image5) {
        this.fingerprint_image5 = fingerprint_image5;
    }

    public byte[] getPhoto1() {
        return photo1;
    }

    public void setPhoto1(byte[] photo1) {
        this.photo1 = photo1;
    }

    public void setActive(boolean pDeleted){
        this.active = pDeleted;
    }

    public boolean getActive(){
        return active;
    }
}
