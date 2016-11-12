package com.globe3.tno.g3_mobile.app_objects;

public class User {
    private String company;
    private String userid;
    private String password;
    private String MAC;
    private String companies;

    public void setCompany(String pCompany){ this.company = pCompany; }
    public String getCompany(){ return company; }

    public void setUserid(String pUserId){ this.userid = pUserId; }
    public String getUserid(){ return userid; }

    public void setPassword(String pPassword) { this.password = pPassword; }
    public String getPassword(){ return password; }

    public String getMAC() { return MAC; }
    public void setMAC(String MAC) { this.MAC = MAC; }

    public String getCompanies() { return companies; }
    public void setCompanies(String companies) { this.companies = companies; }
}
