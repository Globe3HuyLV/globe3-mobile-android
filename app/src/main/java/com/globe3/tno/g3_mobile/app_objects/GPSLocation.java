package com.globe3.tno.g3_mobile.app_objects;

public class GPSLocation {
    public double latitude;
    public double longitude;
    public String address;
    public String city;
    public String state;
    public String country;
    public String postal_code;
    public String known_name;

    public GPSLocation(){
        latitude = 0;
        longitude = 0;
        address = "";
        city = "";
        state = "";
        country = "";
        postal_code = "";
        known_name = "";
    }

    public String getFullAddress(){
        String fullAddress = "";

        fullAddress += known_name != null && !address.contains(known_name) && !known_name.equals("") ? known_name + ", " : "";
        fullAddress += address != null && !address.equals("") ? address + ", " : "";
        fullAddress += city != null && !city.equals("") ? city + ", " : "";
        fullAddress += state != null && !state.equals("") ? state + ", " : "";
        fullAddress += country != null && !country.equals("") ? country : "";
        fullAddress += postal_code != null && !postal_code.equals("") ? " " + postal_code : "";

        return fullAddress;
    }

    public String getCoordinates(){
        return String.valueOf(latitude) + "," + String.valueOf(longitude);
    }
}
