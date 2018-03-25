package com.noobs.contactjump360;

/**
 * Created by harsh on 23-03-2018.
 */

public class ContactInfo {

    public String name;
    public long number;
    public double lat,lng;

    public ContactInfo() {
    }

    public ContactInfo(String name, long number) {
        this.name = name;
        this.number = number;
    }

    public ContactInfo(String name, long number, double lat, double lng) {
        this.name = name;
        this.number = number;
        this.lat = lat;
        this.lng = lng;
    }
}
