package com.globe3.tno.g3_mobile.app_objects;

import java.io.Serializable;
import java.util.Date;

public class TimeRecord implements Serializable{
    private long idcode;
    private String uniquenumPri;
    private Staff Staff;
    private Date DateSync;
    private String SyncUnique;
    private Date DateTimePost;
    private Date DateTimeIn;
    private Date DateTimeOut;
    private String DeviceIdIn;
    private String DeviceIdOut;
    private String DeviceModelIn;
    private String DeviceModelOut;
    private String DeviceNameIn;
    private String DeviceNameOut;
    private String GPSLocationIn;
    private String GPSLocationOut;
    private String AddressIn;
    private String AddressOut;
    private String LogType;
    private Project Project;
    private SalesOrder SalesOrder;
    private boolean Synced;

    public long getIdcode() {
        return idcode;
    }

    public void setIdcode(long idcode) {
        this.idcode = idcode;
    }

    public String getUniquenumPri() {
        return uniquenumPri;
    }

    public void setUniquenumPri(String uniquenum_pri) {
        this.uniquenumPri = uniquenum_pri;
    }

    public com.globe3.tno.g3_mobile.app_objects.Staff getStaff() {
        return Staff;
    }

    public void setStaff(com.globe3.tno.g3_mobile.app_objects.Staff staff) {
        Staff = staff;
    }

    public Date getDateSync() {
        return DateSync;
    }

    public void setDateSync(Date dateSync) {
        DateSync = dateSync;
    }

    public String getSyncUnique() {
        return SyncUnique;
    }

    public void setSyncUnique(String syncUnique) {
        SyncUnique = syncUnique;
    }

    public Date getDateTimePost() {
        return DateTimePost;
    }

    public void setDateTimePost(Date dateTimePost) {
        DateTimePost = dateTimePost;
    }

    public Date getDateTimeIn() {
        return DateTimeIn;
    }

    public void setDateTimeIn(Date dateTimeIn) {
        DateTimeIn = dateTimeIn;
    }

    public Date getDateTimeOut() {
        return DateTimeOut;
    }

    public void setDateTimeOut(Date dateTimeOut) {
        DateTimeOut = dateTimeOut;
    }

    public String getDeviceIdIn() {
        return DeviceIdIn;
    }

    public void setDeviceIdIn(String deviceIdIn) {
        DeviceIdIn = deviceIdIn;
    }

    public String getDeviceIdOut() {
        return DeviceIdOut;
    }

    public void setDeviceIdOut(String deviceIdOut) {
        DeviceIdOut = deviceIdOut;
    }

    public String getDeviceModelIn() {
        return DeviceModelIn;
    }

    public void setDeviceModelIn(String deviceModelIn) {
        DeviceModelIn = deviceModelIn;
    }

    public String getDeviceModelOut() {
        return DeviceModelOut;
    }

    public void setDeviceModelOut(String deviceModelOut) {
        DeviceModelOut = deviceModelOut;
    }

    public String getDeviceNameIn() {
        return DeviceNameIn;
    }

    public void setDeviceNameIn(String deviceNameIn) {
        DeviceNameIn = deviceNameIn;
    }

    public String getDeviceNameOut() {
        return DeviceNameOut;
    }

    public void setDeviceNameOut(String deviceNameOut) {
        DeviceNameOut = deviceNameOut;
    }

    public String getGPSLocationIn() {
        return GPSLocationIn;
    }

    public void setGPSLocationIn(String GPSLocationIn) {
        this.GPSLocationIn = GPSLocationIn;
    }

    public String getGPSLocationOut() {
        return GPSLocationOut;
    }

    public void setGPSLocationOut(String GPSLocationOut) {
        this.GPSLocationOut = GPSLocationOut;
    }

    public String getAddressIn() {
        return AddressIn;
    }

    public void setAddressIn(String addressIn) {
        AddressIn = addressIn;
    }

    public String getAddressOut() {
        return AddressOut;
    }

    public void setAddressOut(String addressOut) {
        AddressOut = addressOut;
    }

    public String getLogType() {
        return LogType;
    }

    public void setLogType(String logType) {
        LogType = logType;
    }

    public com.globe3.tno.g3_mobile.app_objects.Project getProject() {
        return Project;
    }

    public void setProject(com.globe3.tno.g3_mobile.app_objects.Project project) {
        Project = project;
    }

    public com.globe3.tno.g3_mobile.app_objects.SalesOrder getSalesOrder() {
        return SalesOrder;
    }

    public void setSalesOrder(com.globe3.tno.g3_mobile.app_objects.SalesOrder salesOrder) {
        SalesOrder = salesOrder;
    }

    public boolean isSynced() {
        return Synced;
    }

    public void setSynced(boolean synced) {
        Synced = synced;
    }
}
