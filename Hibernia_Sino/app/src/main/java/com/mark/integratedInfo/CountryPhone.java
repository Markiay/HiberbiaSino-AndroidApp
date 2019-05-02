package com.mark.integratedInfo;

public class CountryPhone {
    private int iconid;
    private String country;
    private String phoneNo;

    public CountryPhone(int iconid, String country, String phoneNo) {
        this.iconid = iconid;
        this.country = country;
        this.phoneNo = phoneNo;
    }

    public int getIconid() {
        return iconid;
    }

    public void setIconid(int iconid) {
        this.iconid = iconid;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
