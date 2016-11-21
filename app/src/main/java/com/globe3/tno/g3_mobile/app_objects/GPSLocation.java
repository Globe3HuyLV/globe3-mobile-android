package com.globe3.tno.g3_mobile.app_objects;

public class GPSLocation {
    public double Latitude;
    public double Longitude;
    public String Address;
    public String City;
    public String State;
    public String Country;
    public String PostalCode;
    public String KnownName;

    public GPSLocation(){
        Latitude = 0;
        Longitude = 0;
        Address = "";
        City = "";
        State = "";
        Country = "";
        PostalCode = "";
        KnownName = "";
    }

    public String getFullAddress(){
        String fullAddress = "";

        fullAddress += KnownName != null && !Address.contains(KnownName) && !KnownName.equals("") ? KnownName + ", " : "";
        fullAddress += Address != null && !Address.equals("") ? Address + ", " : "";
        fullAddress += City != null && !City.equals("") ? City + ", " : "";
        fullAddress += State != null && !State.equals("") ? State + ", " : "";
        fullAddress += Country != null && !Country.equals("") ? Country : "";
        fullAddress += PostalCode != null && !PostalCode.equals("") ? " " + PostalCode : "";

        return fullAddress;
    }

    public String getCoordinates(){
        return String.valueOf(Latitude) + "," + String.valueOf(Longitude);
    }
}
