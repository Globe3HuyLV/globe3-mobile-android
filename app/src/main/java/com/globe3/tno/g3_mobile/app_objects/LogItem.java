package com.globe3.tno.g3_mobile.app_objects;

import java.util.Date;

public class LogItem {
    private String tableUsage;
    private String logUnique;
    private Date logDate;

    public String getTableUsage() {
        return tableUsage;
    }

    public void setTableUsage(String tableUsage) {
        this.tableUsage = tableUsage;
    }

    public String getLogUnique() {
        return logUnique;
    }

    public void setLogUnique(String logUnique) {
        this.logUnique = logUnique;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }
}
